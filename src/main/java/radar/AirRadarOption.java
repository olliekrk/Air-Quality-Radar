package radar;

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
}
