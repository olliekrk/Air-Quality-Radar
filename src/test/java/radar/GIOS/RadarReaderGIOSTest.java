package radar.GIOS;

import data.AirQualityIndex;
import data.MeasurementData;
import data.Sensor;
import data.Station;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RadarReaderGIOSTest {

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

    private RadarReaderGIOS reader;

    @Before
    public void setUp() {
        reader = new RadarReaderGIOS();
    }

    @Test
    public void readIndexData() throws IOException {
        AirQualityIndex index = reader.readIndexData(INDEX_DATA_EXAMPLE);
        assertNotNull(index);
        assertEquals(index.getId(), Integer.valueOf(52));
        assertEquals(8, index.getParamIndex().length);
    }

    @Test
    public void readMeasurementData() {
        MeasurementData data = reader.readMeasurementData(MEASUREMENT_DATA_EXAMPLE);
        assertNotNull(data);
        assertEquals(data.getKey(), "PM10");
        assertEquals(data.getValues().size(), 2);
    }

    @Test
    public void readSensorsData() {
        Sensor[] sensors = reader.readSensorsData(SENSORS_DATA_EXAMPLE);
        assertNotNull(sensors);
        assertEquals(2, sensors.length);
        assertEquals(Integer.valueOf(92), sensors[0].getId());
        assertEquals(Integer.valueOf(88), sensors[1].getId());
    }

    @Test
    public void readStationsData() {
        Station[] stations = reader.readStationsData(STATIONS_DATA_EXAMPLE);
        assertNotNull(stations);
        assertEquals(1, stations.length);
        assertEquals("Działoszyn", stations[0].getStationName());
    }
}