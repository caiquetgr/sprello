package br.com.caiqueborges.sprello;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration-test")
public abstract class AbstractIT {

    protected static final String JSON_FOLDER = "json/";
    protected static final String SQL_FOLDER = "/sql/";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected Flyway flyway;

    static final PostgreSQLContainer postgreSQLContainer;

    static {

        postgreSQLContainer = new PostgreSQLContainer("postgres:13")
                .withDatabaseName("sprello")
                .withUsername("it")
                .withPassword("it");

        postgreSQLContainer.start();

    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @AfterEach
    private void afterEach() {
        cleanAndMigrateDatabase();
    }

    private void cleanAndMigrateDatabase() {
        flyway.clean();
        flyway.migrate();
        flyway.validate();
    }

    @SneakyThrows
    protected String readFile(String jsonFileName) {
        URL resource = getJsonResourceUrl(jsonFileName);
        return Files.readString(Paths.get(resource.toURI()));
    }

    private URL getJsonResourceUrl(String jsonPath) {
        return AbstractIT.class.getClassLoader().getResource(jsonPath);
    }

    @SneakyThrows
    protected <T> T readJsonToObject(String jsonPath, Class<T> objectClass) {
        return this.objectMapper.readValue(getJsonResourceUrl(jsonPath), objectClass);
    }

}
