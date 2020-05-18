package net.nlacombe.nlacombenetws.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import net.nlacombe.authlib.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class TestHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TestHttpClient.class);

    private final TestConfig testConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper jsonConverter;

    public TestHttpClient(TestConfig testConfig, TestRestTemplate restTemplate, ObjectMapper jsonConverter) {
        this.testConfig = testConfig;
        this.jsonConverter = jsonConverter;
        this.restTemplate = getTestRestTemplateWithAuthInterceptor(restTemplate);
    }

    public List<String> getResourceListFromLoggedRequests() {
        var response = restTemplate.exchange("/analytics/resourceListFromLoggedRequests", HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
        });

        validateNotAnErrorResponse(response);

        return response.getBody();
    }

    public boolean resourceExistsFollowRedirects(String uri) {
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);

        return response.getStatusCode().is2xxSuccessful();
    }

    private void validateNotAnErrorResponse(ResponseEntity<?> response) {
        if (response.getStatusCode().isError())
            throw new RuntimeException("Error getting resource list from logged requests");
    }

    private RestTemplate getTestRestTemplateWithAuthInterceptor(TestRestTemplate testRestTemplate) {
        var restTemplate = testRestTemplate.getRestTemplate();

        restTemplate.setInterceptors(Collections.singletonList((httpRequest, body, clientHttpRequestExecution) -> {
            var authToken = testConfig.getAuthToken();

            logWarningIfAuthTokenIsExpired(authToken);

            httpRequest.getHeaders().add("Authorization", "Bearer " + authToken);

            return clientHttpRequestExecution.execute(httpRequest, body);
        }));

        return restTemplate;
    }

    private void logWarningIfAuthTokenIsExpired(String authToken) {
        try {
            new JwtUtil(jsonConverter).parseAndValidate(authToken).getExpiration();
        } catch (ExpiredJwtException e) {
            logger.warn("Expired auth token.", e);
        }
    }
}
