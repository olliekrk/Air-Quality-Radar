package radar;

import data.MeasurmentData;
import data.MeasurmentValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final int paramsCount = 8;
    private static final String[] params = new String[]{"st", "so2", "no2", "co", "pm10", "pm25", "o3", "c6h6"};

    public static int getParamsCount() {
        return paramsCount;
    }

    public static String[] getParams() {
        return params;
    }

    public static MeasurmentValue latestMeasurment(MeasurmentData measurmentDataObj) {
        MeasurmentValue latest = null;
        for (MeasurmentValue value : measurmentDataObj.getValues()) {
            try {
                if ((value.getValue() != null && (latest == null || compareDates(latest.getDate(), value.getDate()) < 0))) {
                    latest = value;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return latest;
    }

    private static int compareDates(String date1, String date2) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = format.parse(date1);
        Date d2 = format.parse(date2);
        return d1.compareTo(d2);
    }
}
