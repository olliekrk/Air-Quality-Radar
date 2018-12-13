package radar;

import data.MeasurmentData;
import data.MeasurmentValue;
import data.Station;
import http.HttpExtractor;
import qualityIndex.AirQualityIndex;

import java.io.IOException;
import java.text.ParseException;

public abstract class AirQualityRadar {

    HttpExtractor extractor;
    RadarAdapter adapter;
    RadarPrinter printer;
    RadarTranslator translator;

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
        MeasurmentData measurmentData;
        MeasurmentValue latestMeasurment;
        try {
            measurmentData = adapter.findData(stationName, paramName, extractor, translator);
            latestMeasurment = Utils.latestMeasurment(measurmentData);
        } catch (IOException | ParseException e) {
            System.out.println("Exception thrown while getting current parameter value for given station.");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        printer.printCurrentMeasurment(stationName, paramName, latestMeasurment);
    }

    //3. średnia wartość podanego parametru za podany okres czasu
    public void getAverageParamValueForPeriod(String stationName, String paramName, String fromDate, String toDate) {
        MeasurmentData measurmentData;
        double averageMeasurment = 0;
        try {
            measurmentData = adapter.findData(stationName, paramName, extractor, translator);
            averageMeasurment = Utils.averageMeasurment(measurmentData, fromDate, toDate);
        } catch (IOException | ParseException e) {
            System.out.println("Exception thrown while getting average parameter value for given period");
            System.out.println(e.getMessage());
            return;
        }
        printer.printAverageMeasurment(stationName, paramName, fromDate, toDate, averageMeasurment);
    }

    //4. odszukanie parametru którego wartość, począwszy od podanego dnia uległa największym wahaniom
    void getParamWithMaxAmplitudeForPeriod(String stationName, String fromDate) {

    }

    //5. odszukanie parametru którego wartość była najmniejsza w podanym dniu
    void getParamWithMinValueForDay(String stationName, String date) {

    }

    //6. wypisanie N stanowisk pomiarowych, posortowanych rosnąco, które odnotowały największą wartość podanego parametru w podanym dniu
    void getStationsWithMaxParamValueForDay(String stationName, String paramName, String date) {

    }

    //7. dla podanego parametry wypisanie kiedy i gdzie miał on największą i najmniejszą wartość
    void getParamMeasurmentInfo(String paramName) {

    }

    //8. wykres
    //...
}