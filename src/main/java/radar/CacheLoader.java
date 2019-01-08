package radar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.AirQualityIndex;
import data.MeasurementData;
import data.Sensor;
import data.Station;
import exceptions.HttpConnectionException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for loading local cache file, containing deserialized {@link Cache} object.
 * Also responsible for updating cache file with given frequency.
 */
public class CacheLoader {

    /**
     * Frequency in hours, telling how often cache file should be updated.
     */
    private static final long UPDATE_FREQUENCY_HOURS = 2;
    /**
     * Flag telling whether updates of cache are allowed.
     */
    private static boolean UPDATES_ALLOWED = true;
    /**
     * Path to a cache file.
     */
    private final String cachePath;
    /**
     * Loaded cache.
     */
    private Cache cache;

    /**
     * Class constructor for default local cache file localization.
     */
    public CacheLoader() {
        this.cachePath = "radarCache.json";
    }

    /**
     * Class constructor for custom local cache file localization.
     *
     * @param cachePath path to a destination where cache file should be located
     */
    //for custom cache file localization
    public CacheLoader(String cachePath) {
        this.cachePath = cachePath;
    }

    /**
     * Loads {@link Cache} from cache path.
     * Performs cache update operation if it is necessary, saves cache to a file afterwards.
     *
     * @param extractor  {@link HttpExtractor} used to extract data to be saved
     * @param translator {@link RadarReader} used to translate JSON data into POJO
     */
    public void loadCache(HttpExtractor extractor, RadarReader translator, boolean updatesAllowed) {
        setUpdatesAllowed(updatesAllowed);
        try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cachePath), StandardCharsets.UTF_8))) {
            Gson gson = new GsonBuilder().create();
            this.cache = gson.fromJson(reader, Cache.class);
        } catch (FileNotFoundException e) {
            System.out.println("(!) File not found under given path: " + cachePath);
            System.out.println("(!) A cache file will be created now, this may take few minutes.");
            try {
                refreshCache(extractor, translator);
                System.out.println("(!) Cache file created successfully.");
            } catch (HttpConnectionException e1) {
                System.out.println("(!) Cache update was impossible due to error with HTTP connection with: " + e1.getMessage());
                System.out.println("(!) Locally stored version of cache will be used.");
            } finally {
                saveCache();
            }
            return;
        } catch (IOException e) {
            System.out.println("(!) Failed to load cache from file: " + cachePath);
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("(!) Last update date: " + DataAnalyzer.fromDateTime(cache.getUpdateDate()));
        if (needsUpdate()) {
            System.out.println("(!) Cache is not up-to-date.");
            System.out.println("(!) An update is required.");
            if (UPDATES_ALLOWED) {
                System.out.println("(!) Update has started, this may take few minutes.");
                try {
                    refreshCache(extractor, translator);
                    System.out.println("(!) Cache update finished successfully.");
                } catch (HttpConnectionException e1) {
                    System.out.println("(!) Cache update was impossible due to error with HTTP connection with: " + e1.getMessage());
                    System.out.println("(!) Locally stored version of cache will be used.");
                } finally {
                    saveCache();
                }
            } else {
                System.out.println("(!) Updates are not allowed.");
            }
        } else {
            System.out.println("(!) Cache is up-to-date.");
            System.out.println("(!) No update is required.");
        }
    }

    public Cache getCache() {
        return cache;
    }

    void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * Setter for parameter {@value UPDATES_ALLOWED}. Specifies whether cache updates can be performed.
     *
     * @param updatesAllowed flag telling whether cache updates can be performed, true when they are
     */
    private void setUpdatesAllowed(boolean updatesAllowed) {
        UPDATES_ALLOWED = updatesAllowed;
    }

    /**
     * Method telling whether cache file is up-to-date or not.
     * Prints information how many hours passed since cache was updated.
     *
     * @return true if cache needs update, false otherwise
     */
    boolean needsUpdate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = cache.getUpdateDate();
        long diff = Math.abs(Duration.between(then, now).toHours());
        System.out.println("(!) Hours since last update: " + diff);
        return diff >= UPDATE_FREQUENCY_HOURS;
    }

    /**
     * Method responsible for updating cache.
     *
     * @param extractor  {@link HttpExtractor} used to extract data to be saved
     * @param translator {@link RadarReader} used to translate JSON data into POJO
     */
    private void refreshCache(HttpExtractor extractor, RadarReader translator) throws HttpConnectionException {
        //all stations data (key is station name)
        Map<String, Station> allStations = new HashMap<>();
        //all sensors data (key is station id)
        Map<Integer, List<Sensor>> allSensors = new HashMap<>();
        //all air quality index data (key is station id)
        Map<Integer, AirQualityIndex> allIndices = new HashMap<>();
        //all measurement data (key is sensor id)
        Map<Integer, MeasurementData> allData = new HashMap<>();

        Station[] stations = translator.readStationsData(extractor.extractAllStationsData());

        for (Station s : stations) {
            allStations.put(s.getStationName(), s);
        }

        for (Station s : stations) {
            List<Sensor> stationSensors = Arrays.asList(translator.readSensorsData(extractor.extractAllSensorsData(s.getId())));
            allSensors.put(s.getId(), stationSensors);
        }

        for (Station s : stations) {
            AirQualityIndex index;
            try {
                index = translator.readIndexData(extractor.extractIndexData(s.getId()));
            } catch (IOException e) {
                index = null;
            }
            allIndices.put(s.getId(), index);
        }

        for (List<Sensor> sensorList : allSensors.values()) {
            for (Sensor sensor : sensorList) {
                MeasurementData data = translator.readMeasurementData(extractor.extractMeasurementData(sensor.getId()));
                allData.put(sensor.getId(), data);
            }
        }

        //save current date
        this.cache = new Cache(LocalDateTime.now(), allStations, allSensors, allData, allIndices);
    }

    /**
     * Method responsible for saving loaded cache into file under cache path.
     */
    private void saveCache() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(cachePath), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(cache, writer);
        } catch (IOException e) {
            System.out.println("(!) Failed to save cache file!");
            System.out.println(e.getMessage());
        }
    }
}

