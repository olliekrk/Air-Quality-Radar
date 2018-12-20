package radar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.AirQualityIndex;
import data.MeasurementData;
import data.Sensor;
import data.Station;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CacheManager {

    private final static long UPDATE_FREQUENCY_HOURS = 2;
    private String cacheFile;

    CacheManager(String cacheFile) {
        this.cacheFile = cacheFile;
    }

    Cache loadCache(HttpExtractor extractor, RadarTranslator translator, boolean updatesAllowed) {

        Cache cache;
        try (Reader reader = new FileReader(cacheFile)) {
            Gson gson = new GsonBuilder().create();
            cache = gson.fromJson(reader, Cache.class);
        } catch (FileNotFoundException e) {
            System.out.println("File not found under given path: " + cacheFile);
            System.out.println("A cache file will be created now. Please be patient.");
            cache = refreshCache(extractor, translator);
            saveCache(cache);
            System.out.println("Cache file created successfully.");
            return cache;
        } catch (IOException e) {
            System.out.println("Failed to load cache from file!");
            System.out.println(e.getMessage());
            return null;
        }
        System.out.println("Last update date: " + cache.getUpdateDate().toString());
        if (needsUpdate(cache)) {
            System.out.println("Cache is not up-to-date. An update is required.");
            if (updatesAllowed) {
                System.out.println("Update has started. Please be patient.");
                cache = refreshCache(extractor, translator);
                saveCache(cache);
                System.out.println("Cache update finished successfully.");
            }
        } else {
            System.out.println("Cache is up-to-date. No update is required.");
        }
        return cache;
    }

    private Cache refreshCache(HttpExtractor extractor, RadarTranslator translator) {
        Station[] stations = translator.readStationsData(extractor.extractAllStationsData());

        //all stations data (key is station name)
        Map<String, Station> allStations = new HashMap<>();

        for (Station s : stations) {
            allStations.put(s.getStationName(), s);
        }

        //all sensors data (key is station id)
        Map<Integer, List<Sensor>> allSensors = new HashMap<>();

        for (Station s : stations) {
            List<Sensor> stationSensors = Arrays.asList(translator.readSensorsData(extractor.extractAllSensorsData(s.getId())));
            allSensors.put(s.getId(), stationSensors);
        }

        //all air quality index data (key is station id)
        Map<Integer, AirQualityIndex> allIndices = new HashMap<>();

        for (Station s : stations) {
            AirQualityIndex index;
            try {
                index = translator.readIndexData(extractor.extractIndexData(s.getId()));
            } catch (IOException e) {
                index = null;
            }
            allIndices.put(s.getId(), index);
        }

        //all measurement data (key is sensor id)
        Map<Integer, MeasurementData> allData = new HashMap<>();

        for (List<Sensor> sensorList : allSensors.values()) {
            for (Sensor sensor : sensorList) {
                MeasurementData data = translator.readMeasurementData(extractor.extractMeasurementData(sensor.getId()));
                allData.put(sensor.getId(), data);
            }
        }

        //save current date
        return new Cache(LocalDateTime.now(), allStations, allSensors, allData, allIndices);
    }

    private void saveCache(Cache cache) {
        //try with resources
        try (Writer writer = new FileWriter(cacheFile)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(cache, writer);
        } catch (IOException e) {
            System.out.println("Failed to save cache file!");
            System.out.println(e.getMessage());
        }
    }

    private boolean needsUpdate(Cache cache) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = cache.getUpdateDate();
        long diff = Math.abs(Duration.between(then, now).toHours());
        return diff >= UPDATE_FREQUENCY_HOURS;
    }
}

