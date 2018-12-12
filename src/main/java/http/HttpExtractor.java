package http;

import java.io.InputStream;

public interface HttpExtractor {

    InputStream extractIndexData(Integer stationId);

}