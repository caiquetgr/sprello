package br.com.caiqueborges.sprello.util;

import org.springframework.test.web.servlet.ResultMatcher;

import static br.com.caiqueborges.sprello.util.TestUtils.readFile;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;

public class JsonUnitUtils {

    public static final String JSON_FOLDER = "json/";

    public static ResultMatcher jsonIsEqualToFile(String jsonFileName) {
        return json().isEqualTo(readFile(jsonFileName));
    }

}
