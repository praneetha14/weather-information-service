package com.weather.info;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = BaseControllerTest.TestConfig.class)
@ActiveProfiles("test")
public class BaseControllerTest {

    @TestConfiguration
    public static class TestConfig {

    }
}
