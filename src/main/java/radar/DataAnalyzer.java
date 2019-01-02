package radar;

import data.MeasurementData;
import data.MeasurementValue;
import exceptions.MissingDataException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * All static method abstract class used to convert string representation of dates into datetime format
 * and to extract specific measurement values from measurement data object.
 */
public abstract class DataAnalyzer {

    /**
     * Pattern used to parse strings into datetime and datetime into strings.
     */
    private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * Method used to extract specific measurement value from given measurement data object.
     * Applies date check criteria to every measurement value and then returns specific result.
     *
     * @param data          data to be analyzed
     * @param date1         date since / in
     * @param date2         date until
     * @param dateCheckType what date check criteria should satisfy measurement value
     * @param resultType    what result should be returned
     * @return measurement value specified by {@link DateCheckType} and {@link ResultType} criteria
     * @throws MissingDataException when it was impossible to find data which meets specified criteria
     */
    public static MeasurementValue getValue(MeasurementData data, LocalDateTime date1, LocalDateTime date2, DateCheckType dateCheckType, ResultType resultType) throws MissingDataException {
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
            throw new MissingDataException("Failed to find measurement value for given sensor date(s) and time!");

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

    /**
     * Formats string representation of date and time into {@link LocalDateTime} object.
     *
     * @param dateTime string representation of {@link LocalDateTime}
     * @return {@link LocalDateTime} object representing given string
     */
    public static LocalDateTime intoDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * Formats {@link LocalDateTime} object into string using pattern {@value dateTimePattern}
     *
     * @param dateTime {@link LocalDateTime} object to be formatted
     * @return string representation of given object
     */
    public static String fromDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return dateTime.format(formatter);
    }

    /**
     * Enumeration used to define what date check should be performed while extracting specific measurement value.
     */
    public enum DateCheckType {
        /**
         * No date check.
         */
        ANY,
        /**
         * Measurement date should be between two given dates.
         */
        BETWEEN,
        /**
         * Measurement date should be the first given date.
         */
        IN,
        /**
         * Measurement date should be in or after first given date.
         */
        SINCE
    }

    /**
     * Enumeration used to define what should be type of result returned while extracting specific measurement value.
     */
    public enum ResultType {
        /**
         * Average measurement value.
         */
        AVERAGE,
        /**
         * Single measurement value.
         */
        DEFAULT,
        /**
         * Highest measured value.
         */
        MAX,
        /**
         * Lowest measured value.
         */
        MIN
    }
}
