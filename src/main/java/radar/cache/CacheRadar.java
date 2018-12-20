package radar.cache;

import data.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class CacheRadar {

    //todo: this
    //initialize!
    protected HttpExtractor extractor;
    protected RadarPrinter printer;
    protected RadarTranslator translator;
    protected CacheSeeker seeker;

    //1
    public void getAirQualityIndexForStation(String stationName) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        AirQualityIndex index = seeker.findIndex(station.getId());
        printer.printIndexData(station, index);
    }

    //2
    public void getCurrentParamValueForStation(String stationName, String paramName) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        Sensor sensor = seeker.findStationSensorParam(station.getId(), ParamType.getParam(paramName));
        MeasurementData data = seeker.findData(sensor.getId());
        MeasurementValue value = DataAnalyzer.getLatestMeasurementValue(data);
        printer.printCurrentMeasurement(station.getStationName(), sensor.getParam().getParamName(), value);
    }

    //3
    public void getAverageParamValuePeriod(String stationName, String paramName, String fromDate, String toDate) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        Sensor sensor = seeker.findStationSensorParam(station.getId(), ParamType.getParam(paramName));
        MeasurementData data = seeker.findData(sensor.getId());
        double averageValue = DataAnalyzer.getAverageMeasurementValue(data, fromDate, toDate);
        printer.printAverageMeasurement(station.getStationName(), sensor.getParam().getParamName(), fromDate, toDate, averageValue);
    }

    //4
    public void getExtremeParamValuePeriod(String stationName, String fromDate, String toDate) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        List<Sensor> sensors = seeker.findStationSensors(station.getId());
        MeasurementValue currentMin = null;
        MeasurementValue currentMax = null;
        Sensor currentSensor = null;
        double currentAmplitude = -1;
        for (Sensor sensor : sensors) {
            MeasurementData data = seeker.findData(sensor.getId());
            MeasurementValue min = DataAnalyzer.getExtremeMeasurementValue(data, fromDate, toDate, "min");
            MeasurementValue max = DataAnalyzer.getExtremeMeasurementValue(data, fromDate, toDate, "max");
            double amplitude = max.getValue() - min.getValue();
            if (amplitude > currentAmplitude) {
                currentSensor = sensor;
                currentMin = min;
                currentMax = max;
                currentAmplitude = amplitude;
            }
        }
        printer.printExtremeParamValuesPeriod(station, fromDate, toDate, currentSensor, currentMin, currentMax);
    }

    //5 (o podanej godzinie podanego dnia?) zrobiłem dla podanego dnia
    public void getParamOfMinimalValue(String stationName, String date) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        List<Sensor> sensors = seeker.findStationSensors(station.getId());
        MeasurementValue currentMin = null;
        Sensor currentSensor = null;
        for (Sensor sensor : sensors) {
            MeasurementData data = seeker.findData(sensor.getId());
            MeasurementValue min = DataAnalyzer.getExtremeMeasurementValue(data, date + " 0:00:00", date + " 23:59:00", "min");
            if (min != null && min.getValue() != null && (currentMin == null || currentMin.getValue() > min.getValue())) {
                currentMin = min;
                currentSensor = sensor;
            }
        }
        printer.printParamMinimalValue(station, currentSensor, currentMin, date);
    }

    //6
    //    Dla podanej stacji, wypisanie N stanowisk pomiarowych, posortowanych (rosnąco), które o podanej godzinie określonego dnia, zanotowały największą wartość podanego parametru
    public void getNSensorsWithMaximumParamValue(String stationName, String dateHour, String paramCode, int n) throws MissingDataException {
        Station station = seeker.findStation(stationName);
        List<Sensor> sensors = seeker
                .findStationSensors(station.getId())
                .stream()
                .filter(x -> x.getParam().getParamCode().compareToIgnoreCase(paramCode) == 0)
                .collect(Collectors.toList());

        class TmpClass implements Comparable {
            Sensor sensor;
            MeasurementValue value;

            public TmpClass(Sensor sensor, MeasurementValue value) {
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
            MeasurementData measurementData = seeker.findData(sensor.getId());
            MeasurementValue maxValue = DataAnalyzer.getExtremeMeasurementValue(measurementData, dateHour, dateHour, "max");
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
        for(int i = 0; i<sortedTmp.length;i++){
            sortedSensors[i]=sortedTmp[i].sensor;
            sortedValues[i]=sortedTmp[i].value;
        }
        printer.printNSensors(station, sortedSensors, sortedValues, dateHour, paramCode, n);
    }

    //7
    public void getExtremeParamValueWhereAndWhen(String paramName) throws MissingDataException {
        Station minStation = null, maxStation = null;
        Sensor minSensor = null, maxSensor = null;
        MeasurementValue minValue = null, maxValue = null;
        List<Station> allStations = (List<Station>) seeker.getCache().getAllStations().values();

        for (Station station : allStations) {
            List<Sensor> sensors = seeker.findStationSensors(station.getId());
            for (Sensor sensor : sensors) {
                if (sensor.getParam().getParamCode().equals(paramName)) {
                    MeasurementData data = seeker.findData(sensor.getId());
                    MeasurementValue min = DataAnalyzer.getExtremeMeasurementValue(data, null, null, "min");
                    MeasurementValue max = DataAnalyzer.getExtremeMeasurementValue(data, null, null, "max");
                    if (min != null && (minValue == null || minValue.getValue() > min.getValue())) {
                        minSensor = sensor;
                        minValue = min;
                        minStation = station;
                    }
                    if (max != null && (maxValue == null || maxValue.getValue() < max.getValue())) {
                        maxSensor = sensor;
                        maxValue = max;
                        maxStation = station;
                    }
                }
            }
        }
        printer.printExtremeParamValuesWhereAndWhen(paramName, minStation, minSensor, minValue, maxStation, maxSensor, maxValue);
    }

    public HttpExtractor getExtractor() {
        return extractor;
    }

    public RadarTranslator getTranslator() {
        return translator;
    }
}
