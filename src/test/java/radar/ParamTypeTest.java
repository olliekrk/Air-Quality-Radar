package radar;

import data.ParamType;
import exceptions.UnknownParameterException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ParamTypeTest {


    private List<String> codes =
            Arrays.stream(ParamType.values()).map(Enum::name).collect(Collectors.toList());

    @Test
    public void getParamType() throws UnknownParameterException {
        List<String> possibleCases = new ArrayList<>();

        possibleCases.addAll(codes); //codes in uppercase
        possibleCases.addAll(codes.stream().map(String::toLowerCase).collect(Collectors.toList())); //codes in lowercase

        for (String param : possibleCases) {
            assertNotNull(ParamType.getParamType(param)); //throws exception if fails
        }
    }

    @Test(expected = UnknownParameterException.class)
    public void getParamTypeException() throws UnknownParameterException {
        ParamType.getParamType("x111"); //some unknown parameter
    }

    @Test
    public void getAllParamCodes() {
        for (String code : codes) {
            assertTrue(List.of(ParamType.getAllParamCodes()).contains(code));
        }
        assertEquals(ParamType.getParamCount(), codes.size());
    }
}