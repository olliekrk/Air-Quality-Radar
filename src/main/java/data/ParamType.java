package data;

import java.util.Arrays;

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

    public static ParamType getParam(String paramName) {
        return ParamType.valueOf(paramName.toUpperCase());
    }

    public String getParamFormula() {
        return paramFormula;
    }

    public static String[] getAllParamCodes() {
        return Arrays.stream(ParamType.values()).map(Enum::name).toArray(String[]::new);
    }

    public String[] getAllParamFormulas() {
        return Arrays.stream(ParamType.values()).map(x -> getParamFormula()).toArray(String[]::new);
    }

    public static int getParamCount() {
        return ParamType.values().length;
    }
}