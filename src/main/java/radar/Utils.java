package radar;

import data.MeasurementData;
import data.MeasurementValue;

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

    static MeasurementValue latestMeasurement(MeasurementData measurementData) throws ParseException {
        MeasurementValue latest = null;
        for (MeasurementValue value : measurementData.getValues()) {
            if (value.getValue() != null && ((latest == null) || (compareDates(latest.getDate(), value.getDate()) < 0))) {
                latest = value;
            }
        }
        return latest;
    }

    static double averageMeasurement(MeasurementData measurementData, String fromDate, String toDate) throws ParseException {
        int count = 0;
        double sum = 0;
        for (MeasurementValue value : measurementData.getValues()) {
            if (value.getValue() != null) {
                String date = value.getDate();
                if (compareDates(fromDate, date) <= 0 && compareDates(date, toDate) <= 0) {
                    count++;
                    sum += value.getValue();
                }
            }
        }
        if (count == 0) return -1;
        return sum / count;
    }

    static MeasurementValue getExtremeValue(MeasurementData data, String fromDate, String toDate, String dateCheckType, String extremeType) throws ParseException {
        if (data == null) return null;
        MeasurementValue result = null;
        for (MeasurementValue value : data.getValues()) {
            if (value != null && value.getValue() != null) {
                boolean OK = false;
                if ("in".equals(dateCheckType)) {
                    String date = fromDate;
                    fromDate = date + " 00:00:00";
                    toDate = date + " 23:59:00";
                    if ((compareDates(fromDate, value.getDate()) <= 0 && compareDates(value.getDate(), toDate) <= 0))
                        OK = true;
                } else if ("between".equals(dateCheckType)) {
                    if ((compareDates(fromDate, value.getDate()) <= 0 && compareDates(value.getDate(), toDate) <= 0))
                        OK = true;
                } else if ("from".equals(dateCheckType)) {
                    if ((compareDates(fromDate, value.getDate()) <= 0))
                        OK = true;
                } else {
                    OK = true;
                }
                if (OK && "min".equals(extremeType)) {
                    if (result == null || result.getValue() > value.getValue()) {
                        result = value;
                    }
                } else if (OK && "max".equals(extremeType)) {
                    if (result == null || result.getValue() < value.getValue()) {
                        result = value;
                    }
                }
            }
        }
        return result;
    }

    private static int compareDates(String date1, String date2) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = format.parse(date1);
        Date d2 = format.parse(date2);
        return d1.compareTo(d2);
        //-1 jesli d1 jest wczesniej
    }

    //if we would like to input i.e. only hour
    public static String todayDate(String someTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();

        //if only single digit representing hour is given
        if (someTime.length() == 1) someTime = "0" + someTime;

        //if only hour is given
        if (someTime.length() < 3) someTime += ":00:00";

        return dateFormat.format(today).replaceFirst("\\d{2}:\\d{2}:\\d{2}", someTime);
    }
}
