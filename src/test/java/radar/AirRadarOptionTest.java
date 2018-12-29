package radar;

import org.apache.commons.cli.CommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AirRadarOptionTest {

    private CommandLine cmd;
    private List<AirRadarOption> optionList;

    @Before
    public void setUp() {
        cmd = mock(CommandLine.class);
    }

    @Test
    public void getAvailableOptions() {
        when(cmd.hasOption(anyString())).thenReturn(true);
        optionList = AirRadarOption.getAvailableOptions(cmd);
        assertEquals(AirRadarOption.values().length, optionList.size());

        when(cmd.hasOption(anyString())).thenReturn(false);
        optionList = AirRadarOption.getAvailableOptions(cmd);
        assertEquals(1, optionList.size());
    }

    @Test
    public void getValidOption() {
        assertEquals(AirRadarOption.AIR_QUALITY_INDEX, AirRadarOption.getValidOption(Arrays.asList(AirRadarOption.values()), new Scanner("1")));
        assertEquals(AirRadarOption.MEASUREMENT_VALUE, AirRadarOption.getValidOption(Arrays.asList(AirRadarOption.values()), new Scanner("0 2")));
        assertEquals(AirRadarOption.AVERAGE_MEASUREMENT_VALUE, AirRadarOption.getValidOption(Arrays.asList(AirRadarOption.values()), new Scanner("0 0 3")));
        assertEquals(AirRadarOption.QUIT, AirRadarOption.getValidOption(Arrays.asList(AirRadarOption.values()), new Scanner("0 0 0 9")));
    }
}