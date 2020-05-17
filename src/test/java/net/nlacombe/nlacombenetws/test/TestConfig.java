package net.nlacombe.nlacombenetws.test;

import org.springframework.core.env.Environment;

import java.nio.file.Path;

public class TestConfig {

    public static final String APPLICATION_CONFIGURATION_FOLDER_PATH = "/opt/net.nlacombe/nlacombe-net-ws/";
    public static final String TESTED_APPLICATION_SPRING_CONFIG_FILE_PATH = APPLICATION_CONFIGURATION_FOLDER_PATH + "application.yaml";
    public static final String TEST_APPLICATION_CONFIG_FILE_PATH = APPLICATION_CONFIGURATION_FOLDER_PATH + "testConfig.yaml";

    private final Environment environment;

    public TestConfig(Environment environment) {
        this.environment = environment;
    }

    public static Path getApplicationConfigurationFolderPath() {
        return Path.of(APPLICATION_CONFIGURATION_FOLDER_PATH);
    }

    public static Path getTestApplicationConfigFilePath() {
        return getApplicationConfigurationFolderPath().resolve("testConfig.yaml");
    }

    public String getAuthToken() {
        return environment.getProperty("authToken");
    }
}
