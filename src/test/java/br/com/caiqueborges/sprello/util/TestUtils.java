package br.com.caiqueborges.sprello.util;

import lombok.SneakyThrows;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    @SneakyThrows
    public static String readFile(String jsonFileName) {
        URL resource = TestUtils.class.getClassLoader().getResource(jsonFileName);
        return Files.readString(Paths.get(resource.toURI()));
    }

}
