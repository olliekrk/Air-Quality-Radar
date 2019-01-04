package data;

import exceptions.UnknownParameterException;

import java.util.Arrays;

/**
 * Enumeration for storing all supported air quality parameters types.
 */
public enum ParamType {
    C6H6("C6H6", 5),
    CO("CO", 10000),
    NO2("NO2", 40),
    O3("O3", 120),
    PM10("PM10", 40),
    PM25("PM2.5", 25),
    SO2("SO2", 125),
    ST("ST", 100);
    /**
     * Field to link parameter's type with its formula.
     */
    private final String paramFormula;
    /**
     * Field to link parameter with its acceptable level, expressed in ug/m^3/
     */
    private final Double acceptableLevel;

    ParamType(String paramFormula, double acceptableLevel) {
        this.paramFormula = paramFormula;
        this.acceptableLevel = acceptableLevel;
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
            if (param.name().compareToIgnoreCase(paramCode) == 0 || param.getParamFormula().compareToIgnoreCase(paramCode) == 0)
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

    /**
     * Returns parameter's acceptable level in ug/m^3.
     *
     * @return parameter's acceptable level.
     */
    public Double getAcceptableLevel() {
        return acceptableLevel;
    }
}
