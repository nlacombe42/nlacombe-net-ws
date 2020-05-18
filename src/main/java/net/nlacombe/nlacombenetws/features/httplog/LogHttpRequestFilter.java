package net.nlacombe.nlacombenetws.features.httplog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LogHttpRequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LogHttpRequestFilter.class);

    private static final List<String> HEADERS_NOT_STORED = List.of("postman-token", "cache-control", "connection", "authorization", "cookie", "x-csrf-token");

    private HttpRequestLogJpaRepository requestLogJpaRepository;

    @Inject
    public LogHttpRequestFilter(HttpRequestLogJpaRepository requestLogJpaRepository) {
        this.requestLogJpaRepository = requestLogJpaRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpServletResponse = (HttpServletResponse) servletResponse;

        logger.info("Http request started: " + httpRequest.getMethod() + " " + httpRequest.getRequestURL());

        var httpRequestEntity = new HttpRequestLogEntity(Instant.now(), httpRequest.getMethod(), httpRequest.getRequestURL().toString());
        var headersEntites = Collections.list(httpRequest.getHeaderNames()).stream()
            .map(String::toLowerCase)
            .filter(headerName -> !HEADERS_NOT_STORED.contains(headerName))
            .flatMap(headerName -> Collections.list(httpRequest.getHeaders(headerName)).stream()
                .map(headerValue -> new HttpRequestLogHeaderEntity(httpRequestEntity, headerName, headerValue)))
            .collect(Collectors.toList());

        httpRequestEntity.setHeaders(headersEntites);

        try {
            filterChain.doFilter(servletRequest, servletResponse);

            httpRequestEntity.setResponseStatus(httpServletResponse.getStatus());
            storeHttpRequestLogOrLogError(httpRequestEntity);
        } catch (IOException | ServletException | RuntimeException exception) {
            httpRequestEntity.setResponseStatus(null);
            storeHttpRequestLogOrLogError(httpRequestEntity);

            throw exception;
        }
    }

    private void storeHttpRequestLogOrLogError(HttpRequestLogEntity httpRequestEntity) {
        try {
            requestLogJpaRepository.save(httpRequestEntity);
        } catch (Exception e) {
            logger.error("Error storing http request to DB.", e);
        }
    }
}
