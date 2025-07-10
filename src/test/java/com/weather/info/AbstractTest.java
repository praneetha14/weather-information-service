package com.weather.info;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest(classes = {WeatherInfoAutoConfiguration.class, WeatherInfoServiceApplication.class,
        AbstractTest.TestConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EnableAutoConfiguration
public class AbstractTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @AfterEach
    public void cleanup() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        List<String> tables = jdbcTemplate.queryForList(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'",
                String.class
        );

        for (String table : tables) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @TestConfiguration
    public static class TestConfig {

    }
}
