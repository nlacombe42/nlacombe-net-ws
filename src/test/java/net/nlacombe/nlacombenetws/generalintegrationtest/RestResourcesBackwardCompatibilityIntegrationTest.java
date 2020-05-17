package net.nlacombe.nlacombenetws.generalintegrationtest;

import net.nlacombe.nlacombenetws.test.TestConfig;
import net.nlacombe.nlacombenetws.test.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, args = "--spring.config.location=" + TestConfig.TESTED_APPLICATION_SPRING_CONFIG_FILE_PATH)
@TestPropertySource(locations = "file:" + TestConfig.TEST_APPLICATION_CONFIG_FILE_PATH)
public class RestResourcesBackwardCompatibilityIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(RestResourcesBackwardCompatibilityIntegrationTest.class);

    @Inject
    private Environment environment;

    @Inject
    private TestRestTemplate testRestTemplate;

    private TestHttpClient testHttpClient;

    @BeforeEach
    public void init() {
        var testConfig = new TestConfig(environment);

        testHttpClient = new TestHttpClient(testConfig, testRestTemplate);
    }

    @Test
    public void past_accessed_resources_must_still_return_successful_response() {
        var resourcesUris = testHttpClient.getResourceListFromLoggedRequests();

        var nonExitingLoggedResources = resourcesUris.stream()
            .filter(resourceUri -> !testHttpClient.resourceExistsFollowRedirects(resourceUri))
            .collect(Collectors.toList());

        if (!nonExitingLoggedResources.isEmpty())
            throw new RuntimeException("Following logged resources no longer exists: " + nonExitingLoggedResources);
    }
}
