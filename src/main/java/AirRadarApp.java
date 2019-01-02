import exceptions.MissingDataException;
import exceptions.UnknownParameterException;
import org.apache.commons.cli.*;
import radar.AirRadar;
import radar.AirRadarFactory;
import radar.AirRadarOption;
import radar.DataAnalyzer;
import radar.GIOS.AirRadarFactoryGIOS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Main method class, delivering user interactive interface to run application.
 */
public class AirRadarApp {

    /**
     * Main program's method.
     *
     * @param args array of arguments with whom application was run
     */
    public static void main(String[] args) {

        //creating radar instance
        AirRadarFactory radarFactory = new AirRadarFactoryGIOS();
        AirRadar radar = radarFactory.createAirRadar();

        //preparing cmd
        CommandLine cmd = prepareCMD(args);

        //checking which options are available
        List<AirRadarOption> availableOptions = AirRadarOption.getAvailableOptions(cmd);

        //parsing program arguments
        String[] stationNames = cmd.hasOption("stations") ? cmd.getOptionValues("stations") : null;
        String paramCode = cmd.hasOption("parameter") ? cmd.getOptionValue("parameter") : null;
        LocalDateTime sinceDate = cmd.hasOption("since") ? DataAnalyzer.intoDateTime(cmd.getOptionValue("since")) : null;
        LocalDateTime untilDate = cmd.hasOption("until") ? DataAnalyzer.intoDateTime(cmd.getOptionValue("until")) : null;

        //check if arguments are valid
        try {
            checkCMD(cmd);
        } catch (MissingArgumentException e) {
            System.out.println("~ " + e.getMessage());
            return;
        }

        //loop which allows to run application interactively
        Scanner in = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            AirRadarOption.printAvailableOptions(availableOptions, stationNames, paramCode, sinceDate, untilDate);
            AirRadarOption chosenOption = AirRadarOption.getValidOption(availableOptions, in);
            loop = runOption(radar, stationNames, paramCode, sinceDate, untilDate, chosenOption, in);
        }

        System.out.println("(!) Closing the application.");
    }

    /**
     * Loop method used to deliver interactiveness.
     * Runs selected by user air radar option and returns true or closes
     * the application if user decided to do it, then it returns false.
     *
     * @param radar        air quality radar used to perform operations on cache file
     * @param stationNames names of stations passed as arguments
     * @param paramCode    code of parameter passed as argument
     * @param sinceDate    date since passed as argument
     * @param untilDate    date until passed as argument
     * @param chosenOption one of air radar options chosen by user
     * @param in           scanner to read user input
     * @return true if there is an option to be performed, false if application should be closed
     */
    private static boolean runOption(AirRadar radar, String[] stationNames, String paramCode, LocalDateTime sinceDate, LocalDateTime untilDate, AirRadarOption chosenOption, Scanner in) {
        boolean loop = true;

        if (stationNames != null) {
            int N = 0;
            boolean setN = false;
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
                            if (!setN) {
                                System.out.print("Enter N: ");
                                N = in.nextInt();
                                setN = true;
                            }
                            radar.getNSensorsWithMaximumParamValue(stationName, paramCode, sinceDate, N);
                            break;
                        case QUIT:
                            loop = false;
                    }
                } catch (UnknownParameterException | MissingDataException e) {
                    System.out.println("~ " + e.getMessage());
                }
            }
        }

        if (chosenOption == AirRadarOption.MEASUREMENT_GRAPH) {
            try {
                radar.drawGraph(stationNames, paramCode, sinceDate, untilDate);
            } catch (UnknownParameterException | MissingDataException e) {
                System.out.println("~ " + e.getMessage());
            }
        } else if (chosenOption == AirRadarOption.EXTREME_MEASUREMENT_VALUE_PARAM) {
            try {
                radar.getExtremeParamValueWhereAndWhen(paramCode);
            } catch (UnknownParameterException | MissingDataException e) {
                System.out.println("~ " + e.getMessage());
            }
        }
        return loop;
    }

    /**
     * Prepares CMD with help of Apache Commons CLI library.
     *
     * @param args arguments passed to application
     * @return prepared command line
     */
    private static CommandLine prepareCMD(String[] args) {
        final String cmdSyntax = "java -jar \"Air Quality Radar.jar\"";
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


        String header = "\nList of arguments available to run application with: \n\n";
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
            helpFormatter.printHelp(cmdSyntax, header, options, footer, true);
            System.exit(1);
            return null;
        }

        if (commandLine.hasOption("h")) {
            helpFormatter.printHelp(cmdSyntax, header, options, footer, true);
        }
        if (args.length == 0) {
            System.out.println("No arguments were passed into application.");
            System.out.println("Please follow syntax displayed below.");
            helpFormatter.printHelp(cmdSyntax, header, options, footer, true);
            System.exit(1);
        }

        return commandLine;
    }

    /**
     * Validation method, used to check whether arguments passed to command line
     * are all correct.
     *
     * @param cmd command line to which arguments were passed
     * @throws MissingArgumentException if at least one of arguments passed to application does not have correct format
     */
    private static void checkCMD(CommandLine cmd) throws MissingArgumentException {
        if (cmd.hasOption("stations")) {
            if (cmd.getOptionValues("stations").length == 0)
                throw new MissingArgumentException("No station was given as an argument!");
        }

        if (cmd.hasOption("parameter")) {
            if (cmd.getOptionValue("parameter") == null)
                throw new MissingArgumentException("No parameter was given as an argument!");
        }

        if (cmd.hasOption("since")) {
            LocalDateTime since = DataAnalyzer.intoDateTime(cmd.getOptionValue("since"));
            LocalDateTime now = LocalDateTime.now();
            if (since.isAfter(now)) throw new IllegalArgumentException("\"since\" option's argument is invalid!");
        }

        if (cmd.hasOption("until")) {
            LocalDateTime until = DataAnalyzer.intoDateTime(cmd.getOptionValue("until"));
            LocalDateTime now = LocalDateTime.now();
            if (until.isAfter(now)) throw new IllegalArgumentException("\"until\" option's argument is invalid!");
        }
    }

}