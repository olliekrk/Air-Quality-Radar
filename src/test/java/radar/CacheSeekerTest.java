package radar;

import data.*;
import exceptions.MissingDataException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class CacheSeekerTest {

    private Cache cacheMock;

    private CacheSeeker cacheSeeker;

    @Before
    public void setUp() {
        cacheMock = mock(Cache.class);
        cacheSeeker = new CacheSeeker(cacheMock);
    }

    @Test
    public void findData() throws MissingDataException {
        Map<Integer, MeasurementData> dataMap = new HashMap<>();
        MeasurementData data1 = new MeasurementData(), data2 = new MeasurementData(), data3 = new MeasurementData();
        dataMap.put(1, data1);
        dataMap.put(2, data2);
        dataMap.put(3, data3);

        when(cacheMock.getAllData()).thenReturn(dataMap);

        assertEquals(cacheSeeker.findData(1), data1);
        assertEquals(cacheSeeker.findData(2), data2);
        assertEquals(cacheSeeker.findData(3), data3);
        verify(cacheMock, times(3)).getAllData();
    }

    @Test(expected = MissingDataException.class)
    public void findDataShouldThrowException() throws MissingDataException {
        Map<Integer, MeasurementData> emptyMap = Map.of();

        when(cacheMock.getAllData()).thenReturn(emptyMap);

        cacheSeeker.findData(1);
        verify(cacheMock, times(1)).getAllData();
    }

    @Test
    public void findIndex() throws MissingDataException {
        Map<Integer, AirQualityIndex> indexMap = new HashMap<>();
        AirQualityIndex index1 = new AirQualityIndex();
        AirQualityIndex index2 = new AirQualityIndex();
        AirQualityIndex index3 = new AirQualityIndex();
        indexMap.put(1, index1);
        indexMap.put(2, index2);
        indexMap.put(3, index3);

        when(cacheMock.getAllIndices()).thenReturn(indexMap);

        assertEquals(cacheSeeker.findIndex(1), index1);
        assertEquals(cacheSeeker.findIndex(2), index2);
        assertEquals(cacheSeeker.findIndex(3), index3);
        verify(cacheMock, times(3)).getAllIndices();
    }

    @Test(expected = MissingDataException.class)
    public void findIndexShouldThrowException() throws MissingDataException {
        Map<Integer, AirQualityIndex> emptyMap = Map.of();

        when(cacheMock.getAllIndices()).thenReturn(emptyMap);

        cacheSeeker.findIndex(1);
        verify(cacheMock, times(1)).getAllIndices();
    }

    @Test
    public void findStation() throws MissingDataException {
        Map<String, Station> stationMap = Map.of("station1", new Station());

        when(cacheMock.getAllStations()).thenReturn(stationMap);

        assertNotNull(cacheSeeker.findStation("station1"));
        verify(cacheMock, times(1)).getAllStations();
    }

    @Test(expected = MissingDataException.class)
    public void findStationShouldThrowException() throws MissingDataException {
        Map<String, Station> stationMap = Map.of();

        when(cacheMock.getAllStations()).thenReturn(stationMap);

        cacheSeeker.findStation("any");
        verify(cacheMock, times(1)).getAllStations();
    }

    @Test
    public void findStationSensors() throws MissingDataException {
        List<Sensor> list = List.of(new Sensor());
        Map<Integer, List<Sensor>> map = Map.of(1, list);
        when(cacheMock.getAllSensors()).thenReturn(map);
        assertEquals(cacheSeeker.findStationSensors(1), list);
        verify(cacheMock, times(1)).getAllSensors();
    }

    //null value in map
    @Test(expected = MissingDataException.class)
    public void findStationSensorsShouldThrowException() throws MissingDataException {
        Map<Integer, List<Sensor>> map = Map.of();
        when(cacheMock.getAllSensors()).thenReturn(map);
        cacheSeeker.findStationSensors(1);
        verify(cacheMock).getAllSensors();
    }

    //empty sensors list
    @Test(expected = MissingDataException.class)
    public void findStationSensorsShouldThrowException2() throws MissingDataException {
        Map<Integer, List<Sensor>> map = Map.of(1, List.of());
        when(cacheMock.getAllSensors()).thenReturn(map);
        cacheSeeker.findStationSensors(1);
        verify(cacheMock).getAllSensors();
    }

    @Test
    public void findStationSensorParam() throws MissingDataException {
        Sensor sensorMock1 = Mockito.mock(Sensor.class);
        Sensor sensorMock2 = Mockito.mock(Sensor.class);
        Param param1 = new Param();
        Param param2 = new Param();
        param1.setParamFormula(ParamType.PM10.getParamFormula());
        param2.setParamFormula(ParamType.SO2.getParamFormula());
        Map<Integer, List<Sensor>> map = Map.of(1, List.of(sensorMock1, sensorMock2));

        when(sensorMock1.getParam()).thenReturn(param1);
        when(sensorMock2.getParam()).thenReturn(param2);
        when(cacheMock.getAllSensors()).thenReturn(map);

        assertEquals(sensorMock1, cacheSeeker.findStationSensorParam(1, ParamType.PM10));
        assertEquals(sensorMock2, cacheSeeker.findStationSensorParam(1, ParamType.SO2));
        verify(cacheMock, atLeastOnce()).getAllSensors();
    }

    @Test(expected = MissingDataException.class)
    public void findStationSensorParamShouldThrowException() throws MissingDataException {
        Map<Integer, List<Sensor>> map = Map.of(1, List.of());
        when(cacheMock.getAllSensors()).thenReturn(map);
        cacheSeeker.findStationSensorParam(1, ParamType.SO2);
    }
}