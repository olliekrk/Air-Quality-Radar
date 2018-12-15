package radar;

import data.MeasurementData;
import data.MeasurementValue;
import radar.polishGovApi.PolishHttpExtractor;
import radar.polishGovApi.PolishRadarTranslator;

import java.text.ParseException;

public class UtilsTest {

    @org.junit.Test
    public void getExtremeValue() {
        String[] exampleDate = new String[]{
                "2018-12-15 01:00:00",
                "2018-12-15 18:00:00",
                "2018-12-15 24:00:00",
                "2018-12-15"
        };
        HttpExtractor extractor = new PolishHttpExtractor();
        RadarTranslator translator = new PolishRadarTranslator();
        try {
            MeasurementData data = translator.readMeasurementData(extractor.extractMeasurementData(92));
            MeasurementValue val1 = Utils.getExtremeValue(data, exampleDate[0], exampleDate[2], "between", "min");
            MeasurementValue val2 = Utils.getExtremeValue(data, exampleDate[0], null, "from", "max");
            MeasurementValue val3 = Utils.getExtremeValue(data, exampleDate[3], null, "in", "max");
            MeasurementValue val4 = Utils.getExtremeValue(data, null, null, "none", "min");
            System.out.println(val1);
            System.out.println(val2);
            System.out.println(val3);
            System.out.println(val4);
            System.out.println(val1.getValue() + '\n' + val1.getDate());
            System.out.println(val2.getValue() + '\n' + val2.getDate());
            System.out.println(val3.getValue() + '\n' + val3.getDate());
            System.out.println(val4.getValue() + '\n' + val4.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}