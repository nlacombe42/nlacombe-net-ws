package net.nlacombe.nlacombenetws.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import net.nlacombe.authlib.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class TestHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(TestHttpClient.class);

    private final TestConfig testConfig;
    private final int applicationPort;
    private final HttpClient httpClient;
    private final ObjectMapper jsonConverter;

    public TestHttpClient(TestConfig testConfig, int applicationPort, ObjectMapper jsonConverter) {
        this.testConfig = testConfig;
        this.jsonConverter = jsonConverter;
        this.applicationPort = applicationPort;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    }

    public List<String> getResourceListFromLoggedRequests() {
        try {
            var httpResponse = getResourceResponse(getAbsoluteUri("/analytics/resourceListFromLoggedRequests"));

            validateNotAnErrorResponse(httpResponse);

            return jsonConverter.readValue(httpResponse.body(), new TypeReference<>() {});
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public HttpResponse<InputStream> getResourceResponse(String uri) {
        try {
            var httpRequestBuilder = HttpRequest.newBuilder(URI.create(getAbsoluteUri(uri)));
            httpRequestBuilder = addAuthHeader(httpRequestBuilder);
            var httpRequest = httpRequestBuilder.build();

            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean resourceExists(String uri) {
        var httpResponse = getResourceResponse(getAbsoluteUri(uri));

        return httpResponse.statusCode() >= 200 && httpResponse.statusCode() <= 299;
    }

    private void validateNotAnErrorResponse(HttpResponse<?> response) {
        if (response.statusCode() >= 400 && response.statusCode() <= 599)
            throw new RuntimeException("Error getting resource list from logged requests");
    }

    private HttpRequest.Builder addAuthHeader(HttpRequest.Builder httpRequestBuilder) {
        var authToken = testConfig.getAuthToken();

        logWarningIfAuthTokenIsExpired(authToken);

        return httpRequestBuilder.header("Authorization", "Bearer " + authToken);
    }

    private String getAbsoluteUri(String uriText) {
        var uri = URI.create(uriText);

        return uri.isAbsolute() ? uri.toString() : URI.create(getBaseUrl() + uriText).toString();
    }

    private String getBaseUrl() {
        return "http://localhost:" + applicationPort;
    }

    private void logWarningIfAuthTokenIsExpired(String authToken) {
        try {
            new JwtUtil(jsonConverter).parseAndValidate(authToken).getExpiration();
        } catch (ExpiredJwtException e) {
            logger.warn("Expired auth token.", e);
        }
    }
}
