package data;

//todo: use this
public enum ParamType {
    PM10("PM10"),
    PM25("PM2.5"),
    O3("O3"),
    NO2("NO2"),
    SO2("SO2"),
    C6H6("C6H6"),
    CO("CO");
    private final String paramFormula;

    ParamType(String paramFormula) {
        this.paramFormula = paramFormula;
    }
}
