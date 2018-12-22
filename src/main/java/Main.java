import org.apache.commons.cli.*;
import radar.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CommandLine cmd = prepareCMD(args);

        CacheUser cacheUser = new CacheUser(new CacheRadarGov());
        CacheRadar radar = cacheUser.getRadar();

        boolean stationArg = cmd.hasOption("stations");
        boolean paramCodeArg = cmd.hasOption("parameter");
        boolean dateSinceArg = cmd.hasOption("since");
        boolean dateUntilArg = cmd.hasOption("until");

        String[] stationNames = (stationArg) ? cmd.getOptionValues("stations") : null;
        String paramCode = (paramCodeArg) ? cmd.getOptionValue("parameter") : null;
        LocalDateTime sinceDate = dateSinceArg ? DataAnalyzer.intoDateTime(cmd.getOptionValue("since")) : null;
        LocalDateTime untilDate = dateUntilArg ? DataAnalyzer.intoDateTime(cmd.getOptionValue("until")) : null;

        List<AirRadarOption> availableOptions = new ArrayList<>();
        for (AirRadarOption option : AirRadarOption.values()) {
            boolean available = true;
            if (option.paramCodeRequired && !paramCodeArg) available = false;
            if (option.stationNameRequired && !stationArg) available = false;
            if (option.sinceRequired && !dateSinceArg) available = false;
            if (option.untilRequired && !dateUntilArg) available = false;
            if (available) availableOptions.add(option);
        }

        Scanner in = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            System.out.println("List of all available options: ");
            for (AirRadarOption option : availableOptions) {
                System.out.println(option.optionNo + ": " + option.description);
            }
            boolean correct = false;
            AirRadarOption chosenOption = null;
            while (!correct) {
                System.out.println("Enter a number of option: ");
                String opt = in.next();
                int optNo = Integer.parseInt(opt);
                for (AirRadarOption option : availableOptions) {
                    if (option.optionNo == optNo) {
                        correct = true;
                        chosenOption = option;
                    }
                }
                if (!correct) {
                    System.out.println("Incorrect option number. Please try again.");
                }
            }
            if (stationNames != null) {
                for (String stationName : stationNames) {
                    try {
                        switch (chosenOption) {
                            case AIR_QUALITY_INDEX:
                                radar.getAirQualityIndexForStation(stationName);
                                break;
                            case MEASUREMENT_VALUE:
                                radar.getParamValueForStation(stationName, paramCode, sinceDate);
                                break;
                            case AVERAGE_MEASUREMENT_VALUE:
                                radar.getAverageParamValuePeriod(stationName, paramCode, sinceDate, untilDate);
                                break;
                            case EXTREME_MEASUREMENT_VALUE_STATION:
                                radar.getExtremeParamValuePeriod(stationName, sinceDate);
                                break;
                            case MINIMAL_MEASUREMENT_VALUE_PARAM:
                                radar.getParamOfMinimalValue(stationName, sinceDate);
                                break;
                            case N_SENSORS_MAXIMUM_MEASUREMENT_VALUE:
                                //todo missing n arg
                                radar.getNSensorsWithMaximumParamValue(stationName, paramCode, sinceDate, 2);
                                break;
                            case QUIT:
                                loop = false;
                        }
                    } catch (UnknownParameterException | MissingDataException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            if (chosenOption == AirRadarOption.MEASUREMENT_GRAPH) {
                try {
                    radar.drawGraph(stationNames, paramCode, sinceDate, untilDate);
                } catch (UnknownParameterException | MissingDataException e) {
                    System.out.println(e.getMessage());
                }
            } else if (chosenOption == AirRadarOption.EXTREME_MEASUREMENT_VALUE_PARAM) {
                try {
                    radar.getExtremeParamValueWhereAndWhen(paramCode);
                } catch (UnknownParameterException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Closing the application.");
    }
    /*
    TODO: uruchamianie z IO
    TODO: jar
    TODO: testy - Mockito
    TODO: wzorzec projektowy
    TODO: jedna funkcjonalność kompatybilna z Airly
     */

    private static CommandLine prepareCMD(String[] args) {
        Options options = new Options();

        Option help = Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("displays help")
                .build();

        Option stations = Option.builder("s")
                .longOpt("stations")
                .hasArgs()
                .argName("stations' names")
                .desc("stations for which data will be extracted")
                .valueSeparator('|')
                .build();

        Option param = Option.builder("p")
                .longOpt("parameter")
                .hasArg()
                .argName("parameter's code")
                .desc("code of parameter")
                .build();

        Option date1 = Option.builder("d1")
                .longOpt("since")
                .hasArg()
                .argName("datetime")
                .desc("date since when measurement values should be analyzed")
                .build();

        Option date2 = Option.builder("d2")
                .longOpt("until")
                .hasArg()
                .argName("datetime")
                .desc("date until when measurement values should be analyzed")
                .build();

        options
                .addOption(help)
                .addOption(stations)
                .addOption(param)
                .addOption(date1)
                .addOption(date2);


        String header = "\nList of options available to apply: \n\n";
        String footer = "\nThis application was made by Olgierd Królik.\n\n";
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.out.println("Inappropriate use of application.");
            System.out.println("Please follow syntax displayed below.");
            helpFormatter.printHelp("airquality", header, options, footer, true);
            System.exit(1);
            return null;
        }

        if (commandLine.hasOption("h")) {
            helpFormatter.printHelp("airquality", header, options, footer, true);
        }
        if (args.length == 0) {
            System.out.println("No arguments were passed into application.");
            System.out.println("Please follow syntax displayed below.");
            helpFormatter.printHelp("airquality", header, options, footer, true);
        }

        return commandLine;
    }
}