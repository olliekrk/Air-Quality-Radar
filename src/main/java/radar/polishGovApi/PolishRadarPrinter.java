package radar.polishGovApi;

import data.MeasurementValue;
import data.Param;
import data.Sensor;
import data.Station;
import data.qualityIndex.AirQualityIndex;
import data.qualityIndex.ParamIndex;
import data.qualityIndex.ParamIndexLevel;
import radar.RadarPrinter;

import java.util.Arrays;
import java.util.Collections;

public class PolishRadarPrinter implements RadarPrinter {
    //this methods are actually universal and they work not only with Polish Gov API
    //rename this just to DefaultRadarPrinter?

    private static final String measurementInfoFormat = "Station: %s, \n     Parameter: %s, \n     Date: %s, \n";
    private static final String separator = String.join("", Collections.nCopies(40, "~")) + '\n';
    private static final String indexParamFormat = "Parameter: %s, \n     CalculationDate: %s, \n     IndexLevelName: %s, \n     SourceDataDate: %s, \n";
    private static final String stationParamFormat = "StationName: %s, \n     Parameter: %s, \n";
    private static final String defaultMeasurementInfoFormat = "     MeasurementDate: %s, \n     MeasurementValue: %s, \n";
    private static final String averageMeasurementInfoFormat = "     FromDate: %s, \n     ToDate: %s, \n     AverageMeasurementValue: %s, \n";
    private static final String latestMeasurementInfoFormat = "     LatestMeasurementDate: %s, \n     LatestMeasurementValue: %s, \n";
    private static final String stationSensorParamFormat = "     StationId: %s, \n     Sensor: %s, \n     Parameter: %s, \n";
    private static final String stationSensorFormat = "StationName: %s, \n     Sensor: %s, \n";

    @Override
    public void printIndexData(AirQualityIndex index) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Air quality index for given station info. \n")
                .append("Id: ")
                .append(index.getId())
                .append("\n")
                .append(separator);

        ParamIndex[] params = (ParamIndex[]) index.getParamIndex();
        for (ParamIndex param : params) {
            ParamIndexLevel paramLevel = (ParamIndexLevel) param.getParamIndexLevel();
            result.append(String.format(indexParamFormat, param.getParamName(), param.getCalcDate(), (paramLevel == null) ? null : paramLevel.getIndexLevelName(), param.getSourceDataDate()));
            result.append(separator);
        }
        System.out.println(result.toString());
    }

    @Override
    public void printCurrentMeasurement(String stationName, String paramName, MeasurementValue latestMeasurement) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Parameter current measurement info. \n")
                .append(String.format(stationParamFormat, stationName, paramName))
                .append(String.format(latestMeasurementInfoFormat, latestMeasurement.getDate(), latestMeasurement.getValue()))
                .append(separator);
        System.out.println(result.toString());
    }

    @Override
    public void printAverageMeasurement(String stationName, String paramName, String fromDate, String toDate, double averageMeasurement) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Parameter average measurement info. \n")
                .append(String.format(stationParamFormat, stationName, paramName))
                .append(String.format(averageMeasurementInfoFormat, fromDate, toDate, (averageMeasurement == -1) ? null : averageMeasurement))
                .append(separator);
        System.out.println(result.toString());
    }

    @Override
    public void printMaxAmplitudeParameter(String stationName, String fromDate, Sensor sensor, MeasurementValue maxValue, MeasurementValue minValue) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Parameter with maximum amplitude info. \n")
                .append(String.format(measurementInfoFormat, stationName, sensor.getParam().getParamName(), fromDate))
                .append(separator)
                .append("Maximum: \n")
                .append(String.format(defaultMeasurementInfoFormat, maxValue.getDate(), maxValue.getValue()))
                .append(separator)
                .append("Minimum: \n")
                .append(String.format(defaultMeasurementInfoFormat, minValue.getDate(), minValue.getValue()))
                .append(separator);

        System.out.println(result.toString());
    }

    @Override
    public void printMinMeasurementParameter(String stationName, MeasurementValue minimalValue, Param minimalParam, String date) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Parameter with minimal measurement value for given station and day info. \n")
                .append(String.format(measurementInfoFormat, stationName, minimalParam.getParamName(), date))
                .append(separator)
                .append("Minimal measured value: \n")
                .append(String.format(defaultMeasurementInfoFormat, minimalValue.getDate(), minimalValue.getValue()))
                .append(separator);

        System.out.println(result.toString());
    }

    @Override
    public void printNSensorsWithMaxParamValueForDay(int N, String paramName, String day, Station[] stations, Sensor[] sensors, MeasurementValue[] maxValues) {
        class Tmp implements Comparable {
            Station station;
            Sensor sensor;
            MeasurementValue value;

            public Tmp(Station station, Sensor sensor, MeasurementValue value) {
                this.station = station;
                this.sensor = sensor;
                this.value = value;
            }

            @Override
            public int compareTo(Object o) {
                Tmp other = (Tmp) o;
                if (this.value == null) return 1;
                if (other.value == null) return -1;
                if (this.value == other.value) return 0;
                return this.value.getValue() < other.value.getValue() ? -1 : 1;
            }
        }
        Tmp[] tmps = new Tmp[stations.length];
        for (int i = 0; i < stations.length; i++) {
            tmps[i] = new Tmp(stations[i], sensors[i], maxValues[i]);
        }
        tmps = Arrays
                .stream(tmps)
                .filter(x -> x.value != null)
                .sorted()
                .toArray(Tmp[]::new);

        //order after sorting:
        //1,2,3,4,5

        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append(N + " sensors with maximum parameter value for given day info. \n")
                .append(String.format(measurementInfoFormat, "any", paramName, day))
                .append(separator);

        int start = tmps.length <= N ? 0 : tmps.length - N;
        for (int i = start; i < tmps.length; i++) {
            result
                    .append("Nr " + (i + 1) + " from " + tmps.length + " \n")
                    .append(String.format(stationSensorFormat, tmps[i].station.getStationName(), tmps[i].sensor.getId()))
                    .append(String.format(defaultMeasurementInfoFormat, tmps[i].value.getDate(), tmps[i].value.getValue()))
                    .append(separator);
        }

        System.out.println(result.toString());
    }

    @Override
    public void printParamExtremeValues(Sensor minSensor, MeasurementValue minValue, Sensor maxSensor, MeasurementValue maxValue) {
        StringBuilder result = new StringBuilder();
        result
                .append(separator)
                .append("Minimum and maximum measurement values for given parameter info. \n")
                .append(separator)
                .append("Minimum: \n")
                .append(String.format(stationSensorParamFormat, minSensor.getStationId(), minSensor.getId(), minSensor.getParam().getParamName()))
                .append(String.format(defaultMeasurementInfoFormat, minValue.getDate(), minValue.getValue()))
                .append(separator)
                .append("Maximum: \n")
                .append(String.format(stationSensorParamFormat, maxSensor.getStationId(), maxSensor.getId(), maxSensor.getParam().getParamName()))
                .append(String.format(defaultMeasurementInfoFormat, maxValue.getDate(), maxValue.getValue()))
                .append(separator);
        System.out.println(result.toString());
    }
}
