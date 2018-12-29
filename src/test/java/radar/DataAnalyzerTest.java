package radar;

import data.MeasurementData;
import data.MeasurementValue;
import exceptions.MissingDataException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataAnalyzerTest {

    private static String[] stringDates = {
            "2018-12-01 06:26:17",
            "2018-12-05 11:51:28",
            "2018-12-07 04:49:02",
            "2018-12-07 16:21:23",
            "2018-12-11 06:38:50",
            "2018-12-14 07:28:44",
            "2018-12-15 03:05:46",
            "2018-12-15 06:53:33",
            "2018-12-21 11:37:08",
            "2018-12-21 20:33:30"
    };
    private static LocalDateTime[] dates = {
            LocalDateTime.of(2018, 12, 1, 6, 26, 17),
            LocalDateTime.of(2018, 12, 5, 11, 51, 28),
            LocalDateTime.of(2018, 12, 7, 4, 49, 2),
            LocalDateTime.of(2018, 12, 7, 16, 21, 23),
            LocalDateTime.of(2018, 12, 11, 6, 38, 50),
            LocalDateTime.of(2018, 12, 14, 7, 28, 44),
            LocalDateTime.of(2018, 12, 15, 3, 5, 46),
            LocalDateTime.of(2018, 12, 15, 6, 53, 33),
            LocalDateTime.of(2018, 12, 21, 11, 37, 8),
            LocalDateTime.of(2018, 12, 21, 20, 33, 30)
    };
    private static Double[] values = {
            6.22,
            15.62,
            28.12,
            49.312,
            102.123,
            129.536,
            155.89,
            183.11,
            187.23,
            188.66
    };

    private static List<MeasurementValue> EXAMPLE_VALUES = new ArrayList<>();
    private static MeasurementData data;

    @BeforeClass
    public static void setUp() {
        data = Mockito.mock(MeasurementData.class);
        for (int i = 0; i < values.length; i++) {
            MeasurementValue value = new MeasurementValue();
            value.setValue(values[i]);
            value.setDate(stringDates[i]);
            EXAMPLE_VALUES.add(value);
        }
        Mockito.when(data.getValues()).thenReturn(EXAMPLE_VALUES);
    }

    @Test
    public void getValueTest() throws MissingDataException {
        MeasurementValue anyMin = DataAnalyzer.getValue(data, null, null, DataAnalyzer.DateCheckType.ANY, DataAnalyzer.ResultType.MIN);
        assertEquals(anyMin.getValue(), values[0]);

        MeasurementValue anyMax = DataAnalyzer.getValue(data, null, null, DataAnalyzer.DateCheckType.ANY, DataAnalyzer.ResultType.MAX);
        assertEquals(anyMax.getValue(), values[values.length - 1]);

        MeasurementValue anyAvg = DataAnalyzer.getValue(data, null, null, DataAnalyzer.DateCheckType.ANY, DataAnalyzer.ResultType.AVERAGE);
        assertTrue(anyAvg.getValue() > values[0] && anyAvg.getValue() < values[values.length - 1]);

        MeasurementValue inDefault = DataAnalyzer.getValue(data, dates[3], null, DataAnalyzer.DateCheckType.IN, DataAnalyzer.ResultType.DEFAULT);
        assertEquals(inDefault.getValue(), values[3]);

        MeasurementValue betweenMin = DataAnalyzer.getValue(data, dates[0], dates[6], DataAnalyzer.DateCheckType.BETWEEN, DataAnalyzer.ResultType.MIN);
        assertEquals(betweenMin.getValue(), values[0]);

        MeasurementValue betweenMax = DataAnalyzer.getValue(data, dates[0], dates[6], DataAnalyzer.DateCheckType.BETWEEN, DataAnalyzer.ResultType.MAX);
        assertEquals(betweenMax.getValue(), values[6]);

        MeasurementValue sinceMin = DataAnalyzer.getValue(data, dates[3], null, DataAnalyzer.DateCheckType.SINCE, DataAnalyzer.ResultType.MIN);
        assertEquals(sinceMin.getValue(), values[3]);

        MeasurementValue sinceMax = DataAnalyzer.getValue(data, dates[3], null, DataAnalyzer.DateCheckType.SINCE, DataAnalyzer.ResultType.MAX);
        assertEquals(sinceMax.getValue(), values[values.length - 1]);
    }

    @Test(expected = MissingDataException.class)
    public void getValueFailsTest() throws MissingDataException {
        Mockito.when(data.getValues()).thenReturn(null);
        DataAnalyzer.getValue(data, null, null, DataAnalyzer.DateCheckType.ANY, DataAnalyzer.ResultType.MIN);
    }

    @Test(expected = MissingDataException.class)
    public void getValueFailsTest1() throws MissingDataException {
        Mockito.when(data.getValues()).thenReturn(EXAMPLE_VALUES);
        LocalDateTime date = DataAnalyzer.intoDateTime("2018-12-30 12:00:00");
        DataAnalyzer.getValue(data, date, null, DataAnalyzer.DateCheckType.SINCE, DataAnalyzer.ResultType.MIN);
    }

    @Test
    public void intoDateTime() {
        for (int i = 0; i < dates.length; i++) {
            assertEquals(DataAnalyzer.intoDateTime(stringDates[i]), dates[i]);
        }
    }

    @Test
    public void fromDateTime() {
        for (int i = 0; i < stringDates.length; i++) {
            assertEquals(DataAnalyzer.fromDateTime(dates[i]), stringDates[i]);
        }
    }
}