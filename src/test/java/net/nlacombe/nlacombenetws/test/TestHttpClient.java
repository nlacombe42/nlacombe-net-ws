package net.nlacombe.nlacombenetws.test;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class TestHttpClient {

    private final TestConfig testConfig;
    private final RestTemplate restTemplate;

    public TestHttpClient(TestConfig testConfig, TestRestTemplate restTemplate) {
        this.testConfig = testConfig;
        this.restTemplate = getTestRestTemplateWithAuthInterceptor(restTemplate);
    }

    public List<String> getResourceListFromLoggedRequests() {
        var response = restTemplate.exchange("/analytics/resourceListFromLoggedRequests", HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});

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
            httpRequest.getHeaders().add("Authorization", "Bearer " + testConfig.getAuthToken());

            return clientHttpRequestExecution.execute(httpRequest, body);
        }));

        return restTemplate;
    }
}
