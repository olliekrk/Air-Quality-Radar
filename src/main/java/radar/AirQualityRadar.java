package radar;

import data.*;
import data.qualityIndex.AirQualityIndex;

import java.io.IOException;
import java.text.ParseException;

public abstract class AirQualityRadar {

    protected HttpExtractor extractor;
    protected RadarAdapter adapter;
    protected RadarPrinter printer;
    protected RadarTranslator translator;

    //1. indeks jakości powietrza dla podanej nazwy stacji pommiarowej
    public void getAirQualityIndexForStation(String stationName) {
        Station station;
        AirQualityIndex index;
        try {
            station = adapter.findStationByName(stationName, extractor);
            String data = extractor.extractIndexData(station.getId());
            index = translator.readIndexData(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        printer.printIndexData(index);
    }

    //2. aktualną wartość parametry dla podanej nazwy stacji i nazwy parametru
    public void getCurrentParamValueForStation(String stationName, String paramName) {
        Station station;
        MeasurementData measurementData;
        MeasurementValue latestMeasurment;
        try {
            station = adapter.findStationByName(stationName, extractor);
            measurementData = adapter.findData(station.getId(), paramName, extractor, translator);
            latestMeasurment = Utils.latestMeasurement(measurementData);
        } catch (IOException | ParseException e) {
            System.out.println("Exception thrown while getting current parameter value for given station.");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        printer.printCurrentMeasurement(stationName, paramName, latestMeasurment);
    }

    //3. średnia wartość podanego parametru za podany okres czasu
    public void getAverageParamValueForPeriod(String stationName, String paramName, String fromDate, String toDate) {
        Station station;
        MeasurementData measurementData;
        double averageMeasurment = 0;
        try {
            station = adapter.findStationByName(stationName, extractor);
            measurementData = adapter.findData(station.getId(), paramName, extractor, translator);
            averageMeasurment = Utils.averageMeasurement(measurementData, fromDate, toDate);
        } catch (IOException | ParseException e) {
            System.out.println("Exception thrown while getting average parameter value for given period.");
            System.out.println(e.getMessage());
            return;
        }
        printer.printAverageMeasurement(stationName, paramName, fromDate, toDate, averageMeasurment);
    }

    //4. odszukanie parametru którego wartość, począwszy od podanej godziny(danego dnia) uległa największym wahaniom
    public void getParamWithMaxAmplitudeForPeriod(String stationName, String fromDate) {
        Station station;
        String sensorsData;
        Sensor[] sensors;
        try {
            station = adapter.findStationByName(stationName, extractor);
            sensorsData = extractor.extractAllSensorsData(station.getId());
            sensors = translator.readSensorsData(sensorsData);
        } catch (IOException e) {
            System.out.println("Exception thrown while getting parameter with max amplitude.");
            System.out.println(e.getMessage());
            return;
        }
        int N = sensors.length;
        MeasurementData[] datas = new MeasurementData[N];
        MeasurementValue[] maxValues = new MeasurementValue[N];
        MeasurementValue[] minValues = new MeasurementValue[N];
        double[] amplitudes = new double[N];

        for (int i = 0; i < N; i++) {
            Sensor sensor = sensors[i];
            try {
                MeasurementData data = adapter.findData(station.getId(), sensor.getParam().getParamCode(), extractor, translator);
                datas[i] = data;
            } catch (IOException e) {
                datas[i] = null;
            }
        }
        for (int i = 0; i < N; i++) {
            try {
                maxValues[i] = Utils.getMaxValue(datas[i], fromDate);
                minValues[i] = Utils.getMinValue(datas[i], fromDate);
                amplitudes[i] = maxValues[i] != null && minValues[i] != null ? Math.abs(maxValues[i].getValue() - minValues[i].getValue()) : -1;
            } catch (ParseException e) {
                System.out.println("Exception thrown while calculating amplitude for: " + sensors[i].getParam().getParamName());
                System.out.println(e.getMessage());
            }
        }
        double maxAmplitude = -1;
        int index = -1;
        for (int i = 0; i < N; i++) {
            if (index == -1 || amplitudes[i] > maxAmplitude) {
                index = i;
                maxAmplitude = amplitudes[i];
            }
        }
        printer.printMaxAmplitudeParameter(station.getStationName(), fromDate, sensors[index], maxValues[index], minValues[index]);
    }

    //5. odszukanie parametru którego wartość była najmniejsza w podanym dniu
    public void getParamWithMinValueForDay(String stationName, String date) {
        Station station;
        Sensor[] sensors;
        String sensorsData;
        MeasurementValue minimalValue = null;
        Param minimalParam = null;
        try {
            station = adapter.findStationByName(stationName, extractor);
            sensorsData = extractor.extractAllSensorsData(station.getId());
            sensors = translator.readSensorsData(sensorsData);
        } catch (IOException e) {
            System.out.println("Exception thrown while getting parameter with minimum value for given station and day.");
            System.out.println(e.getMessage());
            return;
        }
        for (Sensor sensor : sensors) {
            String currentMeasurment = extractor.extractMeasurementData(sensor.getId());
            MeasurementData currentData = translator.readMeasurementData(currentMeasurment);
            MeasurementValue minimalSensorValue;
            try {
                minimalSensorValue = Utils.getMinValueInDate(currentData, date);
            } catch (ParseException e) {
                minimalSensorValue = null;
            }
            if (minimalValue == null || (minimalSensorValue != null && minimalSensorValue.getValue() < minimalValue.getValue())) {
                minimalValue = minimalSensorValue;
                minimalParam = sensor.getParam();
            }
        }
        printer.printMinMeasurementParameter(station.getStationName(), minimalValue, minimalParam, date);
    }

    //6. wypisanie N stanowisk pomiarowych, posortowanych rosnąco, które odnotowały największą wartość podanego parametru w podanym dniu
    void getNStationsWithMaxParamValueForDay(String stationName, String paramName, String day) {

    }

    //7. dla podanego parametry wypisanie kiedy i gdzie miał on największą i najmniejszą wartość
    void getParamMeasurementInfo(String paramName) {

    }

    //8. wykres
    //...
}