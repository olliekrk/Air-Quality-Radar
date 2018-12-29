package data;

import exceptions.UnknownParameterException;

import java.util.Arrays;

public enum ParamType {
    C6H6("C6H6"),
    CO("CO"),
    NO2("NO2"),
    O3("O3"),
    PM10("PM10"),
    PM25("PM2.5"),
    SO2("SO2"),
    ST("ST");
    private final String paramFormula;

    ParamType(String paramFormula) {
        this.paramFormula = paramFormula;
    }

    public String getParamFormula() {
        return paramFormula;
    }

    public static ParamType getParamType(String paramCode) throws UnknownParameterException {
        for (ParamType param : ParamType.values()) {
            if (param.name().compareToIgnoreCase(paramCode) == 0)
                return param;
        }
        throw new UnknownParameterException("Unknown parameter: " + paramCode + "!");
    }

    public static String[] getAllParamCodes() {
        return Arrays.stream(ParamType.values()).map(Enum::name).toArray(String[]::new);
    }

    public static int getParamCount() {
        return ParamType.values().length;
    }
}
