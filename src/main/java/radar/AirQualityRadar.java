package radar;

public interface AirQualityRadar {
    //1. indeks jakości powietrza dla podanej nazwy stacji pommiarowej
    void getAirQualityIndexForStation(String stationName);

    //2. aktualną wartość parametry dla podanej nazwy stacji i nazwy parametru
    void getParamValueForStation(String stationName, String paramName);

    //3. średnia wartość podanego parametru za podany okres czasu
    void getAverageParamValueForPeriod(String stationName, String paramName, String fromDate, String toDate);

    //4. odszukanie parametru którego wartość, począwszy od podanego dnia uległa największym wahaniom
    void getParamWithMaxAmplitudeForPeriod(String stationName, String fromDate);

    //5. odszukanie parametru którego wartość była najmniejsza w podanym dniu
    void getParamWithMinValueForDay(String stationName, String date);

    //6. wypisanie N stanowisk pomiarowych, posortowanych rosnąco, które odnotowały największą wartość podanego parametru w podanym dniu
    void getStationsWithMaxParamValueForDay(String stationName, String paramName, String date);

    //7. dla podanego parametry wypisanie kiedy i gdzie miał on największą i najmniejszą wartość
    void getParamMeasurmentInfo(String paramName);

    //8. wykres
    //...
}