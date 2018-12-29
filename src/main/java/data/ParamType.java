package data;

import exceptions.UnknownParameterException;

import java.util.Arrays;

/**
 * Enumeration for storing all supported air quality parameters types.
 */
public enum ParamType {
    C6H6("C6H6"),
    CO("CO"),
    NO2("NO2"),
    O3("O3"),
    PM10("PM10"),
    PM25("PM2.5"),
    SO2("SO2"),
    ST("ST");
    /**
     * Field to link parameter's type with its formula.
     */
    private final String paramFormula;

    ParamType(String paramFormula) {
        this.paramFormula = paramFormula;
    }

    /**
     * Returns corresponding {@link ParamType} value if given code represents one of supported parameters type.
     *
     * @param paramCode unique code of parameter
     * @return corresponding {@link ParamType} value if given code represents one of supported parameters type
     * @throws UnknownParameterException if given code does not represent any of supported parameters type
     */
    public static ParamType getParamType(String paramCode) throws UnknownParameterException {
        for (ParamType param : ParamType.values()) {
            if (param.name().compareToIgnoreCase(paramCode) == 0)
                return param;
        }
        throw new UnknownParameterException("Unknown parameter: " + paramCode + "!");
    }

    /**
     * @return array of all parameters codes
     */
    public static String[] getAllParamCodes() {
        return Arrays.stream(ParamType.values()).map(Enum::name).toArray(String[]::new);
    }

    /**
     * @return unique parameters count
     */
    public static int getParamCount() {
        return ParamType.values().length;
    }

    /**
     * Returns unique parameter's formula.
     *
     * @return parameter's formula as String
     */
    public String getParamFormula() {
        return paramFormula;
    }
}
