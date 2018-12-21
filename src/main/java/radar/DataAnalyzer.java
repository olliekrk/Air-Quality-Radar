package radar;

import data.MeasurementData;
import data.MeasurementValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DataAnalyzer {

    static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public enum DateCheckType {
        IN,
        BETWEEN,
        SINCE,
        ANY

    }

    public enum ResultType {
        DEFAULT,
        AVERAGE,
        MIN,
        MAX

    }

    static MeasurementValue getValue(MeasurementData data, LocalDateTime date1, LocalDateTime date2, DateCheckType dateCheckType, ResultType resultType) throws MissingDataException {
        if (data == null || data.getValues() == null || data.getValues().size() == 0)
            throw new MissingDataException("Failed to analyze measurement data!");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);

        List<MeasurementValue> checkedDateValues = data.getValues().stream()
                .filter(x -> x.getValue() != null && x.getDate() != null)
                .filter(x -> {
                    LocalDateTime xDateTime = LocalDateTime.parse(x.getDate(), formatter);
                    switch (dateCheckType) {
                        case IN:
                            if (xDateTime.isEqual(date1))
                                return true;
                            break;
                        case SINCE:
                            if (xDateTime.isEqual(date1) || xDateTime.isAfter(date1))
                                return true;
                            break;
                        case BETWEEN:
                            if ((xDateTime.isEqual(date1) || xDateTime.isAfter(date1)) && (xDateTime.isEqual(date2) || xDateTime.isBefore(date2)))
                                return true;
                            break;
                        case ANY:
                            return true;
                    }
                    return false;
                }).collect(Collectors.toList());

        if (checkedDateValues.size() == 0)
            throw new MissingDataException("Failed to find measurement value for given sensor, date(s) and time!");

        switch (resultType) {
            case DEFAULT:
                //list now should contain 1 value
                return checkedDateValues.get(0);

            case AVERAGE:
                int count = checkedDateValues.size();
                double total = 0;
                for (MeasurementValue value : checkedDateValues) {
                    total += value.getValue();
                }
                MeasurementValue resultValue = new MeasurementValue();
                resultValue.setDate(null);
                resultValue.setValue(total / count);
                return resultValue;

            case MAX:
                MeasurementValue maxValue = null;
                for (MeasurementValue value : checkedDateValues) {
                    if (maxValue == null || value.getValue() > maxValue.getValue())
                        maxValue = value;
                }
                return maxValue;

            case MIN:
                MeasurementValue minValue = null;
                for (MeasurementValue value : checkedDateValues) {
                    if (minValue == null || value.getValue() < minValue.getValue())
                        minValue = value;
                }
                return minValue;
        }

        throw new MissingDataException("Failed to analyze measurement data!");
    }

    public static LocalDateTime intoDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static String fromDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return dateTime.format(formatter);
    }

}
