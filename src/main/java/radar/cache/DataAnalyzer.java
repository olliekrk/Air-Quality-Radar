package radar.cache;

import data.MeasurementData;
import data.MeasurementValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DataAnalyzer {

    static MeasurementValue getLatestMeasurementValue(MeasurementData data) {
        MeasurementValue latest = null;
        for (MeasurementValue value : data.getValues()) {
            if (value.getValue() != null && ((latest == null) || (compareDates(latest.getDate(), value.getDate()) < 0))) {
                latest = value;
            }
        }
        return latest;
    }

    static double getAverageMeasurementValue(MeasurementData data, String fromDate, String toDate) {
        int count = 0;
        double sum = 0;
        for (MeasurementValue value : data.getValues()) {
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

    private static int compareDates(String date1, String date2) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (ParseException e) {
            //unlikely to happen
        }
        return d1.compareTo(d2);
        //-1 if d1 is earlier than d2
    }

    static MeasurementValue getExtremeMeasurementValue(MeasurementData data, String fromDate, String toDate, String type) {
        if (data == null) return null;
        MeasurementValue extreme = null;
        for (MeasurementValue value : data.getValues()) {
            if (value != null && value.getValue() != null) {
                boolean OK = false;
                //beetween two dates
                if (fromDate != null && toDate != null) {
                    if ((compareDates(fromDate, value.getDate()) <= 0 && compareDates(value.getDate(), toDate) <= 0))
                        OK = true;
                }
                //from one date
                else if (fromDate != null) {
                    if ((compareDates(fromDate, value.getDate()) <= 0))
                        OK = true;
                }
                //no date constraints
                else {
                    OK = true;
                }

                if (OK) {
                    if ("min".equals(type)) {
                        if (extreme == null || extreme.getValue() > value.getValue()) {
                            extreme = value;
                        }
                    } else if ("max".equals(type)) {
                        if (extreme == null || extreme.getValue() < value.getValue()) {
                            extreme = value;
                        }
                    }
                }
            }
        }
        return extreme;
    }
}
