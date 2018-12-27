package radar;

import data.*;
import radar.exceptions.MissingDataException;
import radar.exceptions.UnknownParameterException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AirRadar {

    protected HttpExtractor extractor;
    protected RadarPrinter printer;
    protected RadarReader reader;
    protected CacheSeeker seeker;

    //1. Wypisanie aktualnego indeksu jakości powietrza dla podanej (nazwy) stacji pomiarowej
    public void getAirQualityIndexForStation(String stationName) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        AirQualityIndex index = seeker.findIndex(station.getId());
        printer.printIndexData(station, index);
    }

    //2. Wypisanie dla podanego dnia, godziny oraz stacji pomiarowej (czytelna nazwa stacji) aktualnej wartości danego parametru (np. PM10)
    public void getParamValueForStation(String stationName, String paramCode, LocalDateTime date) throws MissingDataException, UnknownParameterException {
        Station station = seeker.findStation(stationName);
        ParamType paramType = ParamType.getParamType(paramCode);
        Sensor sensor = seeker.findStationSensorParam(station.getId(), paramType);
        MeasurementData data = seeker.findData(sensor.getId());

        MeasurementValue resultValue = DataAnalyzer.getValue(data, date, null, DataAnalyzer.DateCheckType.IN, DataAnalyzer.ResultType.DEFAULT);
        printer.printMeasurement(station, sensor, resultValue);
    }

    //3. Obliczenie średniej wartości zanieczyszczenia / parametru (np. SO2) za podany okres dla danej stacji
    public void getAverageParamValuePeriod(String stationName, String paramCode, LocalDateTime since, LocalDateTime until) throws MissingDataException, UnknownParameterException {
        Station station = seeker.findStation(stationName);
        ParamType paramType = ParamType.getParamType(paramCode);
        Sensor sensor = seeker.findStationSensorParam(station.getId(), paramType);
        MeasurementData data = seeker.findData(sensor.getId());

        MeasurementValue resultValue = DataAnalyzer.getValue(data, since, until, DataAnalyzer.DateCheckType.BETWEEN, DataAnalyzer.ResultType.AVERAGE);
        printer.printAverageMeasurement(station, sensor, since, until, resultValue.getValue());
    }

    //4. Odszukanie, dla wymienionych stacji, parametru którego wartość, począwszy od podanej godziny (danego dnia), uległa największym wahaniom
    public void getExtremeParamValuePeriod(String stationName, LocalDateTime since) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        List<Sensor> sensors = seeker.findStationSensors(station.getId());
        MeasurementValue currentMin = null;
        MeasurementValue currentMax = null;
        Sensor currentSensor = null;
        Double currentAmplitude = (double) -1;
        for (Sensor sensor : sensors) {
            MeasurementData data = seeker.findData(sensor.getId());

            MeasurementValue maybeMin = DataAnalyzer.getValue(data, since, null, DataAnalyzer.DateCheckType.SINCE, DataAnalyzer.ResultType.MIN);
            MeasurementValue maybeMax = DataAnalyzer.getValue(data, since, null, DataAnalyzer.DateCheckType.SINCE, DataAnalyzer.ResultType.MAX);
            Double amplitude = maybeMax.getValue() - maybeMin.getValue();

            if (amplitude > currentAmplitude) {
                currentSensor = sensor;
                currentMin = maybeMin;
                currentMax = maybeMax;
                currentAmplitude = amplitude;
            }
        }
        printer.printExtremeParamValuesSince(station, since, currentSensor, currentMin, currentMax);
    }

    //5. Odszukanie parametru, którego wartość była najmniejsza o podanej godzinie podanego dnia
    public void getParamOfMinimalValue(String stationName, LocalDateTime date) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        List<Sensor> sensors = seeker.findStationSensors(station.getId());
        MeasurementValue minValue = null;
        Sensor minSensor = null;
        for (Sensor sensor : sensors) {
            MeasurementData data = seeker.findData(sensor.getId());
            MeasurementValue maybeMin = DataAnalyzer.getValue(data, date, null, DataAnalyzer.DateCheckType.IN, DataAnalyzer.ResultType.MIN);
            if (maybeMin != null && maybeMin.getValue() != null && (minValue == null || minValue.getValue() > maybeMin.getValue())) {
                minValue = maybeMin;
                minSensor = sensor;
            }
        }
        printer.printParamMinimalValue(station, minSensor, minValue, date);
    }

    //6. Dla podanej stacji, wypisanie N stanowisk pomiarowych, posortowanych (rosnąco), które o podanej godzinie określonego dnia, zanotowały największą wartość podanego parametru
    public void getNSensorsWithMaximumParamValue(String stationName, String paramCode, LocalDateTime date, int n) throws MissingDataException, UnknownParameterException {
        Station station = seeker.findStation(stationName);
        ParamType paramType = ParamType.getParamType(paramCode);

        List<Sensor> sensors = seeker
                .findStationSensors(station.getId())
                .stream()
                .filter(x -> x.getParam().getParamFormula().compareToIgnoreCase(paramType.getParamFormula()) == 0)
                .collect(Collectors.toList());

        class TmpClass implements Comparable {
            private Sensor sensor;
            private MeasurementValue value;

            private TmpClass(Sensor sensor, MeasurementValue value) {
                this.sensor = sensor;
                this.value = value;
            }

            @Override
            public int compareTo(Object o) {
                TmpClass other = (TmpClass) o;
                if (this.value == null) return 1;
                if (other.value == null) return -1;
                if (this.value == other.value) return 0;
                return this.value.getValue() < other.value.getValue() ? -1 : 1;
            }
        }

        List<TmpClass> tmpClassList = new ArrayList<>();

        for (Sensor sensor : sensors) {
            MeasurementData data = seeker.findData(sensor.getId());
            MeasurementValue maxValue = DataAnalyzer.getValue(data, date, null, DataAnalyzer.DateCheckType.IN, DataAnalyzer.ResultType.MAX);
            tmpClassList.add(new TmpClass(sensor, maxValue));
        }

        TmpClass[] sortedTmp = tmpClassList
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> x.value != null)
                .sorted()
                .toArray(TmpClass[]::new);

        if (sortedTmp.length > n) {
            sortedTmp = Arrays.copyOfRange(sortedTmp, sortedTmp.length - n, sortedTmp.length);
        }

        Sensor[] sortedSensors = new Sensor[sortedTmp.length];
        MeasurementValue[] sortedValues = new MeasurementValue[sortedTmp.length];
        for (int i = 0; i < sortedTmp.length; i++) {
            sortedSensors[i] = sortedTmp[i].sensor;
            sortedValues[i] = sortedTmp[i].value;
        }
        printer.printNSensors(station, sortedSensors, sortedValues, date, paramCode, n);
    }

    //7. Dla podanego parametru wypisanie informacji: kiedy (dzień, godzina) i gdzie (stacja), miał on największą wartość, a kiedy i gdzie najmniejszą
    public void getExtremeParamValueWhereAndWhen(String paramCode) throws UnknownParameterException, MissingDataException {
        Station minStation = null, maxStation = null;
        Sensor minSensor = null, maxSensor = null;
        MeasurementValue minValue = null, maxValue = null;
        Map<String, Station> allStationsMap = seeker.getCache().getAllStations();
        ParamType paramType = ParamType.getParamType(paramCode);

        for (Station station : allStationsMap.values()) {
            try {
                Sensor sensor = seeker.findStationSensorParam(station.getId(), paramType);
                MeasurementData data = seeker.findData(sensor.getId());
                MeasurementValue maybeMin = DataAnalyzer.getValue(data, null, null, DataAnalyzer.DateCheckType.ANY, DataAnalyzer.ResultType.MIN);
                MeasurementValue maybeMax = DataAnalyzer.getValue(data, null, null, DataAnalyzer.DateCheckType.ANY, DataAnalyzer.ResultType.MAX);
                if (maybeMin != null && maybeMin.getValue() != null && (minValue == null || minValue.getValue() > maybeMin.getValue())) {
                    minSensor = sensor;
                    minValue = maybeMin;
                    minStation = station;
                }
                if (maybeMax != null && maybeMax.getValue() != null && (maxValue == null || maxValue.getValue() < maybeMax.getValue())) {
                    maxSensor = sensor;
                    maxValue = maybeMax;
                    maxStation = station;
                }
            } catch (MissingDataException e) {
                //just continue searching
            }
        }
        if(minStation==null || maxStation == null || minValue == null || maxValue == null || minValue.getValue() == null || maxValue.getValue() == null)
            throw new MissingDataException("No sufficient data is available for parameter: " + paramType.getParamFormula());
        printer.printExtremeParamValuesWhereAndWhen(paramCode, minStation, minSensor, minValue, maxStation, maxSensor, maxValue);
    }

    //8. Rysowanie (w trybie tekstowym) wspólnego (dla wszystkich podanych godzin) wykresu zmian wartości
    // (np. wykres słupkowy, za pomocą różnorodnych znaków ASCII) podanego parametru w układzie godzinowym, tzn. jaka było zanieczyszczenie (np. SO2)
    //Dla punktu 8, jako dane wejściowe programu podajemy: nazwę parametru, nazwy stacji pomiarowych oraz dwa czasy: godzinę początkową oraz końcową
    public void drawGraph(String[] stationNames, String paramCode, LocalDateTime since, LocalDateTime until) throws UnknownParameterException, MissingDataException {
        ParamType paramType = ParamType.getParamType(paramCode);

        List<Station> stations = new ArrayList<>();
        for (String name : stationNames) {
            try {
                Station station = seeker.findStation(name);
                stations.add(station);
            } catch (MissingDataException e) {
                System.out.println(e.getMessage());
                //and do not add to list
            }
        }
        //key is stationId
        Map<Integer, Sensor> sensors = new HashMap<>();
        for (Station station : stations) {
            try {
                Sensor sensor = seeker.findStationSensorParam(station.getId(), paramType);
                sensors.put(station.getId(), sensor);
            } catch (MissingDataException e) {
                System.out.println(e.getMessage());
                //and do not add
            }
        }
        //key is sensorId
        Map<Integer, MeasurementData> dataMap = new HashMap<>();
        for (Sensor sensor : sensors.values()) {
            try {
                MeasurementData data = seeker.findData(sensor.getId());
                dataMap.put(sensor.getId(), data);
            } catch (MissingDataException e) {
                System.out.println(e.getMessage());
                //and do not add
            }
        }

        MeasurementValue maxValue = null;
        MeasurementValue minValue = null;

        //finding min and max measured values
        for (MeasurementData data : dataMap.values()) {
            try {
                MeasurementValue maybeMax = DataAnalyzer.getValue(data, since, until, DataAnalyzer.DateCheckType.BETWEEN, DataAnalyzer.ResultType.MAX);
                MeasurementValue maybeMin = DataAnalyzer.getValue(data, since, until, DataAnalyzer.DateCheckType.BETWEEN, DataAnalyzer.ResultType.MIN);
                if (maxValue == null || maxValue.getValue() < maybeMax.getValue()) {
                    maxValue = maybeMax;
                }
                if (minValue == null || minValue.getValue() > maybeMin.getValue()) {
                    minValue = maybeMin;
                }
            } catch (MissingDataException e) {
                //just do not compare
            }
        }

        if (maxValue == null || minValue == null)
            throw new MissingDataException("No sufficient data is available to draw a graph.");

        //drawing graph
        for (Station station : stations) {
            if (sensors.containsKey(station.getId())) {
                Sensor sensor = sensors.get(station.getId());
                if (dataMap.containsKey(sensor.getId())) {
                    MeasurementData data = dataMap.get(sensor.getId());
                    printer.printGraph(station, sensor, data, since, until, paramType, maxValue.getValue());
                }
            }
        }
    }
}
