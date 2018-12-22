package radar;

import org.apache.commons.cli.CommandLine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public enum AirRadarOption {
    AIR_QUALITY_INDEX(1, true, false, false, false, "Show air quality index for given station(s)."),
    MEASUREMENT_VALUE(2, true, true, true, false, "Show measurement value for given station(s), parameter and date."),
    AVERAGE_MEASUREMENT_VALUE(3, true, true, true, true, "Show average measurement value for given station(s), parameter, date since and date until."),
    EXTREME_MEASUREMENT_VALUE_STATION(4, true, false, true, false, "Show which parameter's measurement values had biggest amplitude on given station(s) and since given date."),
    MINIMAL_MEASUREMENT_VALUE_PARAM(5, true, false, true, false, "Show which parameter's measurement values had minimal value measured on given station(s) and since given date."),
    N_SENSORS_MAXIMUM_MEASUREMENT_VALUE(6, true, true, true, false, "Show N sensors which measured highest given parameter values on given station(s) since given date."),
    EXTREME_MEASUREMENT_VALUE_PARAM(7, false, true, false, false, "Show station(s) which measured extreme given parameter's values."),
    MEASUREMENT_GRAPH(8, true, true, true, true, "Draw a bar chart(s) showing changes of given parameter's measurements on given station(s) and in given period."),
    QUIT(9, false, false, false, false, "Close the application.");

    public final int optionNo;
    public final boolean stationNameRequired;
    public final boolean paramCodeRequired;
    public final boolean sinceRequired;
    public final boolean untilRequired;
    public final String description;

    AirRadarOption(int optionNo, boolean stationNameRequired, boolean paramCodeRequired, boolean sinceRequired, boolean untilRequired, String description) {
        this.optionNo = optionNo;
        this.stationNameRequired = stationNameRequired;
        this.paramCodeRequired = paramCodeRequired;
        this.sinceRequired = sinceRequired;
        this.untilRequired = untilRequired;
        this.description = description;
    }

    public static List<AirRadarOption> getAvailableOptions(CommandLine cmd) {
        boolean stationArg = cmd.hasOption("stations");
        boolean paramCodeArg = cmd.hasOption("parameter");
        boolean dateSinceArg = cmd.hasOption("since");
        boolean dateUntilArg = cmd.hasOption("until");

        List<AirRadarOption> airRadarOptions = new ArrayList<>();
        for (AirRadarOption option : AirRadarOption.values()) {
            boolean available = true;
            if (option.paramCodeRequired && !paramCodeArg) available = false;
            if (option.stationNameRequired && !stationArg) available = false;
            if (option.sinceRequired && !dateSinceArg) available = false;
            if (option.untilRequired && !dateUntilArg) available = false;
            if (available) airRadarOptions.add(option);
        }
        return airRadarOptions;
    }

    public static void printAvailableOptions(List<AirRadarOption> availableOptions, String[] stationNames, String paramCode, LocalDateTime sinceDate, LocalDateTime untilDate) {
        System.out.println("List of all available options: ");
        for (AirRadarOption option : availableOptions) {
            System.out.println(option.optionNo + ": " + option.description);
        }
        System.out.print('\n');
        if (stationNames != null) {
            System.out.print("Stations: ");
            for (String stationName : stationNames)
                System.out.print(" (" + stationName + ") ");
            System.out.println();
        }
        if (paramCode != null) System.out.println("Parameter: " + paramCode);
        if (sinceDate != null) System.out.println("1st date (since/in): " + DataAnalyzer.fromDateTime(sinceDate));
        if (untilDate != null) System.out.println("2nd date (until): " + DataAnalyzer.fromDateTime(untilDate));
        System.out.println();

    }

    public static AirRadarOption getValidOption(List<AirRadarOption> availableOptions, Scanner in) {
        while (true) {
            System.out.print("Enter a number of option: ");
            String input = in.next();
            int optionNo = Integer.parseInt(input);
            for (AirRadarOption availableOption : availableOptions) {
                if (availableOption.optionNo == optionNo) {
                    return availableOption;
                }
            }
            System.out.println("Incorrect option number. Please try again.");
        }
    }
}
