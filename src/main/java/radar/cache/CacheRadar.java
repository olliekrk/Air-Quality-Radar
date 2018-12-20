package radar.cache;

import data.*;
import data.AirQualityIndex;
import radar.RadarPrinter;

import java.util.List;

public class CacheRadar {

    //todo: refactor this
    //initialize!
    private CacheSeeker seeker;
    private RadarPrinter printer;

    //1
    public void getAirQualityIndexForStation(String stationName) {
        try {
            Station station = seeker.findStation(stationName);
            AirQualityIndex index = seeker.findIndex(station.getId());
            this.printer.printIndexData(index);
        } catch (MissingDataException e) {
            //...
        }
    }

    //2
    public void getCurrentParamValueForStation(String stationName, String paramName) {
        try {
            Station station = seeker.findStation(stationName);
            Sensor sensor = seeker.findStationSensorParam(station.getId(), ParamType.getParam(paramName));
            MeasurementData data = seeker.findData(sensor.getId());
            MeasurementValue value = DataAnalyzer.getLatestMeasurementValue(data);
            this.printer.printCurrentMeasurement(station.getStationName(), sensor.getParam().getParamName(), value);
        } catch (MissingDataException e) {
            //...
        }
    }

    //3
    public void getAverageParamValuePeriod(String stationName, String paramName, String fromDate, String toDate) {
        try {
            Station station = seeker.findStation(stationName);
            Sensor sensor = seeker.findStationSensorParam(station.getId(), ParamType.getParam(paramName));
            MeasurementData data = seeker.findData(sensor.getId());
            double averageValue = DataAnalyzer.getAverageMeasurementValue(data, fromDate, toDate);
            this.printer.printAverageMeasurement(station.getStationName(), sensor.getParam().getParamName(), fromDate, toDate, averageValue);
        } catch (MissingDataException e) {
            //...
        }
    }

    //4
    public void getExtremeParamValuePeriod(String stationName, String fromDate, String toDate) {
        try {
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
            //print all info
        } catch (MissingDataException e) {
            //...
        }
    }

    //5 (o podanej godzinie podanego dnia?) zrobiłem dla podanego dnia
    public void getParamOfMinimalValue(String stationName, String date) {
        try {
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
            //print all info
        } catch (MissingDataException e) {
            //...
        }
    }

    //6 (o podanej godzinie podanego dnia?)
    public void getNStationsWithMaxMeasurementValue() {

    }

    //7
    public void getExtremeParamValueWhereAndWhen(String paramName) {
        Sensor minSensor, maxSensor;
        MeasurementValue minValue = null, maxValue = null;
        List<Station> allStations = (List<Station>) seeker.getCache().getAllStations().values();
        for (Station station : allStations) {
            try {
                List<Sensor> sensors = seeker.findStationSensors(station.getId());
                for (Sensor sensor : sensors) {
                    MeasurementData data = seeker.findData(sensor.getId());
                    MeasurementValue min = DataAnalyzer.getExtremeMeasurementValue(data, null, null, "min");
                    MeasurementValue max = DataAnalyzer.getExtremeMeasurementValue(data, null, null, "max");
                    if (min != null && (minValue == null || minValue.getValue() > min.getValue())) {
                        minSensor = sensor;
                        minValue = min;
                    }
                    if (max != null && (maxValue == null || maxValue.getValue() < max.getValue())) {
                        maxSensor = sensor;
                        maxValue = max;
                    }
                }
                //print all info
            } catch (MissingDataException e) {
                //...
            }
        }
    }
}
