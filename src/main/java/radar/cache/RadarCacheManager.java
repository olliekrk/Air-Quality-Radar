package radar.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.MeasurementData;
import data.Sensor;
import data.Station;
import data.qualityIndex.AirQualityIndex;
import radar.HttpExtractor;
import radar.RadarAdapter;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadarCacheManager {

    private String cacheFile;
    //in hours
    private final static long updateFrequency = 2;

    public RadarCacheManager(String cacheFile) {
        this.cacheFile = cacheFile;
    }

    private CacheSerializer loadCache(HttpExtractor extractor, RadarAdapter adapter) {
        //try with resources
        try (Reader reader = new FileReader(cacheFile)) {
            Gson gson = new GsonBuilder().create();
            CacheSerializer cache = gson.fromJson(reader, CacheSerializer.class);
            if (needsUpdate(cache)) {
                cache = refreshCache(extractor, adapter);
                saveCache(cache);
            }
            return cache;
        } catch (FileNotFoundException e) {
            System.out.println("File not found under given path: " + cacheFile);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Failed to load cache from file!");
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void saveCache(CacheSerializer cache) {
        //try with resources
        try (Writer writer = new FileWriter(cacheFile)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(cache, writer);
        } catch (IOException e) {
            System.out.println("Failed to save cache file!");
            System.out.println(e.getMessage());
            return;
        }
    }

    private boolean needsUpdate(CacheSerializer cache) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = cache.getUpdateDate();
        long diff = Math.abs(Duration.between(then, now).toHours());
        return diff > updateFrequency;
    }

    //TODO:to parsowanie powinien potem robić adapter, ale ok
    private CacheSerializer refreshCache(HttpExtractor extractor, RadarAdapter adapter) {
        Station[] stations = new Gson().fromJson(extractor.extractAllStationsData(), Station[].class);

        //all stations data (key is station name)
        Map<String, Station> allStations = new HashMap<>();

        for (Station s : stations) {
            allStations.put(s.getStationName(), s);
        }

        //all sensors data (key is station id)
        Map<Integer, List<Sensor>> allSensors = new HashMap<>();

        for (Station s : stations) {
            List<Sensor> stationSensors = Arrays.asList(new Gson().fromJson(extractor.extractAllSensorsData(s.getId()), Sensor[].class));
            allSensors.put(s.getId(), stationSensors);
        }

        //all air quality index data (key is station id)
        Map<Integer, AirQualityIndex> allIndices = new HashMap<>();

        for (Station s : stations) {
            AirQualityIndex index = new Gson().fromJson(extractor.extractIndexData(s.getId()), AirQualityIndex.class);
            allIndices.put(s.getId(), index);
        }

        //all measurement data (key is sensor id)
        Map<Integer, MeasurementData> allData = new HashMap<>();

        for (List<Sensor> sensorList : allSensors.values()) {
            for (Sensor sensor : sensorList) {
                MeasurementData data = new Gson().fromJson(extractor.extractMeasurementData(sensor.getId()), MeasurementData.class);
                allData.put(sensor.getId(), data);
            }
        }

        //save current date
        return new CacheSerializer(LocalDateTime.now(), allStations, allSensors, allData, allIndices);
    }
}

