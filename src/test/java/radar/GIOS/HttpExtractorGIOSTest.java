package radar.GIOS;

import exceptions.HttpConnectionException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import radar.HttpConnection;
import radar.HttpConnectionFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class HttpExtractorGIOSTest {

    private final String INDEX_DATA_EXAMPLE = "{\n" +
            "    \"id\": 52,\n" +
            "    \"stCalcDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"stIndexLevel\": {\n" +
            "        \"id\": 2,\n" +
            "        \"indexLevelName\": \"Umiarkowany\"\n" +
            "    },\n" +
            "    \"stSourceDataDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"so2CalcDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"so2IndexLevel\": null,\n" +
            "    \"so2SourceDataDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"no2CalcDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"no2IndexLevel\": null,\n" +
            "    \"no2SourceDataDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"coCalcDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"coIndexLevel\": null,\n" +
            "    \"coSourceDataDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"pm10CalcDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"pm10IndexLevel\": null,\n" +
            "    \"pm10SourceDataDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"pm25CalcDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"pm25IndexLevel\": null,\n" +
            "    \"pm25SourceDataDate\": null,\n" +
            "    \"o3CalcDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"o3IndexLevel\": null,\n" +
            "    \"o3SourceDataDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"c6h6CalcDate\": \"2017-03-28 12:00:00\",\n" +
            "    \"c6h6IndexLevel\": null,\n" +
            "    \"c6h6SourceDataDate\": \"2017-03-28 12:00:00\"\n" +
            "}";
    private final String STATIONS_DATA_EXAMPLE = "[{\n" +
            "    \"id\": 14,\n" +
            "    \"stationName\": \"Działoszyn\",\n" +
            "    \"gegrLat\": \"50.972167\",\n" +
            "    \"gegrLon\": \"14.941319\",\n" +
            "    \"city\": {\n" +
            "        \"id\": 192,\n" +
            "        \"name\": \"Działoszyn\",\n" +
            "        \"commune\": {\n" +
            "            \"communeName\": \"Bogatynia\",\n" +
            "            \"districtName\": \"zgorzelecki\",\n" +
            "            \"provinceName\": \"DOLNOŚLĄSKIE\"\n" +
            "        }\n" +
            "    },\n" +
            "    \"addressStreet\": null\n" +
            "}]";
    private final String SENSORS_DATA_EXAMPLE = "[{\n" +
            "    \"id\": 92,\n" +
            "    \"stationId\": 14,\n" +
            "    \"param\": {\n" +
            "        \"paramName\": \"pył zawieszony PM10\",\n" +
            "        \"paramFormula\": \"PM10\",\n" +
            "        \"paramCode\": \"PM10\",\n" +
            "        \"idParam\": 3\n" +
            "    }\n" +
            "},\n" +
            "{\n" +
            "    \"id\": 88,\n" +
            "    \"stationId\": 14,\n" +
            "    \"param\": {\n" +
            "        \"paramName\": \"dwutlenek azotu\",\n" +
            "        \"paramFormula\": \"NO2\",\n" +
            "        \"paramCode\": \"NO2\",\n" +
            "        \"idParam\": 6\n" +
            "    }\n" +
            "}]";
    private final String MEASUREMENT_DATA_EXAMPLE = "{\n" +
            "    \"key\": \"PM10\",\n" +
            "    \"values\": [\n" +
            "    {\n" +
            "        \"date\": \"2017-03-28 11:00:00\",\n" +
            "        \"value\": 30.3018\n" +
            "    },\n" +
            "    {\n" +
            "        \"date\": \"2017-03-28 12:00:00\",\n" +
            "        \"value\": 27.5946\n" +
            "    }]\n" +
            "}";

    private HttpConnectionFactory factoryMock;
    private HttpConnection connectionMock;
    private HttpExtractorGIOS extractor = new HttpExtractorGIOS();

    @Before
    public void setUp() throws HttpConnectionException {
        factoryMock = mock(HttpConnectionFactory.class);
        connectionMock = mock(HttpConnection.class);
        extractor.setHttpConnectionFactory(factoryMock);
        when(factoryMock.build(anyString())).thenReturn(connectionMock);
    }

    @Test
    public void extractIndexData() throws HttpConnectionException {
        when(connectionMock.getResponseAsString()).thenReturn(INDEX_DATA_EXAMPLE);

        assertEquals(extractor.extractIndexData(1), INDEX_DATA_EXAMPLE);
        Mockito.verify(factoryMock, times(1)).build(String.format(HttpExtractorGIOS.AIR_QUALITY_INDEX_URL_TEMPLATE, 1));
        Mockito.verify(connectionMock, times(1)).getResponseAsString();
        Mockito.verify(connectionMock, times(1)).close();
    }

    @Test
    public void extractAllStationsData() throws HttpConnectionException {
        when(connectionMock.getResponseAsString()).thenReturn(STATIONS_DATA_EXAMPLE);

        assertEquals(extractor.extractAllStationsData(), STATIONS_DATA_EXAMPLE);
        Mockito.verify(factoryMock, times(1)).build(HttpExtractorGIOS.STATIONS_URL);
        Mockito.verify(connectionMock, times(1)).getResponseAsString();
        Mockito.verify(connectionMock, times(1)).close();
    }

    @Test
    public void extractAllSensorsData() throws HttpConnectionException {
        when(connectionMock.getResponseAsString()).thenReturn(SENSORS_DATA_EXAMPLE);

        assertEquals(extractor.extractAllSensorsData(1), SENSORS_DATA_EXAMPLE);
        Mockito.verify(factoryMock, times(1)).build(String.format(HttpExtractorGIOS.SENSORS_URL_TEMPLATE, 1));
        Mockito.verify(connectionMock, times(1)).getResponseAsString();
        Mockito.verify(connectionMock, times(1)).close();
    }

    @Test
    public void extractMeasurementData() throws HttpConnectionException {
        when(connectionMock.getResponseAsString()).thenReturn(MEASUREMENT_DATA_EXAMPLE);

        assertEquals(extractor.extractMeasurementData(1), MEASUREMENT_DATA_EXAMPLE);
        Mockito.verify(factoryMock, times(1)).build(String.format(HttpExtractorGIOS.DATA_URL_TEMPLATE, 1));
        Mockito.verify(connectionMock, times(1)).getResponseAsString();
        Mockito.verify(connectionMock, times(1)).close();
    }

    @Test
    public void connectAndExtract() throws HttpConnectionException {
        String response = "some response";
        String url = "some url";
        when(connectionMock.getResponseAsString()).thenReturn(response);
        assertEquals(extractor.connectAndExtract(url), response);
        Mockito.verify(factoryMock, times(1)).build(url);
        Mockito.verify(connectionMock, times(1)).getResponseAsString();
        Mockito.verify(connectionMock, times(1)).close();
    }
}