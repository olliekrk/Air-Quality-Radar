package radar;

import data.*;
import exceptions.MissingDataException;

import java.util.List;

/**
 * Class used to seek cache for wanted air quality information and to extract data from it.
 */
public class CacheSeeker {

    /**
     * Object form of local cache file in JSON format.
     */
    private Cache cache;

    /**
     * Constructor with cache object as parameter.
     *
     * @param cache cache to be seeked information for
     */
    public CacheSeeker(Cache cache) {
        this.cache = cache;
    }

    /**
     * Returns cache
     *
     * @return cache
     */
    Cache getCache() {
        return cache;
    }

    /**
     * Method to extract measurement data for given sensor from cache
     *
     * @param sensorId id of a sensor from which data should be extracted
     * @return measurement data collected by sensor of given id
     * @throws MissingDataException when cache does not contain data collected by sensor of given id
     */
    MeasurementData findData(Integer sensorId) throws MissingDataException {
        MeasurementData data = cache.getAllData().get(sensorId);
        if (data != null) return data;
        throw new MissingDataException("Failed to find data of sensor with id " + sensorId);
    }

    /**
     * Method to extract air quality index data for given station from cache.
     *
     * @param stationId id of a station from which data be extracted
     * @return air quality index for given station
     * @throws MissingDataException when cache does not contain air quality index for given station id
     */
    AirQualityIndex findIndex(Integer stationId) throws MissingDataException {
        AirQualityIndex index = cache.getAllIndices().get(stationId);
        if (index != null) return index;
        throw new MissingDataException("Failed to find air quality index on station with id " + stationId);
    }

    /**
     * Method to find station stored in cache by name of that station.
     *
     * @param stationName name of a station which should be found
     * @return station object representing station of given name
     * @throws MissingDataException when cache does not contain station of given name
     */
    Station findStation(String stationName) throws MissingDataException {
        Station station = cache.getAllStations().get(stationName);
        if (station != null) return station;
        throw new MissingDataException("Failed to find station named " + stationName);
    }

    /**
     * Method to find all sensors located on station of given id.
     *
     * @param stationId id of a station
     * @return list of sensors located on station of given id
     * @throws MissingDataException when cache does not contain information about sensors of given station
     */
    List<Sensor> findStationSensors(Integer stationId) throws MissingDataException {
        List<Sensor> sensors = cache.getAllSensors().get(stationId);
        if (sensors != null && !sensors.isEmpty()) return sensors;
        throw new MissingDataException("Failed to find any sensor on station with id " + stationId);
    }

    /**
     * Method to find sensor of given parameter on given station.
     *
     * @param stationId id of a station
     * @param param     parameter which sensor should be found
     * @return sensor of given parameter located on given station
     * @throws MissingDataException when cache does not contain information about sensor of given parameter located on given station
     */
    Sensor findStationSensorParam(Integer stationId, ParamType param) throws MissingDataException {
        List<Sensor> stationSensors = findStationSensors(stationId);
        for (Sensor sensor : stationSensors) {
            if (sensor.getParam().getParamFormula().compareToIgnoreCase(param.getParamFormula()) == 0)
                return sensor;
        }
        throw new MissingDataException("Failed to find sensor of " + param.getParamFormula() + " on station with id: " + stationId);
    }
}
