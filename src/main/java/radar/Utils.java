package radar;

import data.MeasurmentData;
import data.MeasurmentValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    private static final int paramsCount = 8;
    private static final String[] params = new String[]{"st", "so2", "no2", "co", "pm10", "pm25", "o3", "c6h6"};

    public static int getParamsCount() {
        return paramsCount;
    }

    public static String[] getParams() {
        return params;
    }

    public static MeasurmentValue latestMeasurment(MeasurmentData measurmentData) throws ParseException {
        MeasurmentValue latest = null;
        for (MeasurmentValue value : measurmentData.getValues()) {
            if (value.getValue() != null && ((latest == null) || (compareDates(latest.getDate(), value.getDate()) < 0))) {
                latest = value;
            }
        }
        return latest;
    }

    public static double averageMeasurment(MeasurmentData measurmentData, String fromDate, String toDate) throws ParseException {
        int count = 0;
        double sum = 0;
        for (MeasurmentValue value : measurmentData.getValues()) {
            if (value.getValue() != null) {
                String date = value.getDate();
                if (compareDates(fromDate, date) <= 0 && compareDates(date, toDate) <= 0) {
                    count++;
                    sum+=value.getValue();
                }
            }
        }
        if(count==0) return -1;
        return sum/count;
    }

    private static int compareDates(String date1, String date2) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = format.parse(date1);
        Date d2 = format.parse(date2);
        return d1.compareTo(d2);
        //-1 jesli d1 jest wczesniej
    }
}
