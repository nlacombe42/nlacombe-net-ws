package net.nlacombe.nlacombenetws.features.httplog;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "http_request_logs")
public class HttpRequestLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long httpRequestId;

    private Instant createdAtTimestamp;
    private String method;
    private String uri;
    private Integer responseStatus;

    @OneToMany(mappedBy = "httpRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HttpRequestLogHeaderEntity> headers;

    public HttpRequestLogEntity() {
    }

    public HttpRequestLogEntity(Instant createdAtTimestamp, String method, String uri) {
        this.createdAtTimestamp = createdAtTimestamp;
        this.method = method;
        this.uri = uri;
        this.headers = new ArrayList<>();
    }

    public Map<String, List<String>> getHeadersMap() {
        return getHeaders().stream()
            .collect(Collectors.groupingBy(HttpRequestLogHeaderEntity::getHeaderName, Collectors.mapping(
                HttpRequestLogHeaderEntity::getHeaderValue,
                Collectors.toList()
            )));
    }

    public String getHeaderWithOnlyOneValue(String headerName) {
        var headerValues = getHeaders().stream()
            .filter(httpRequestLogHeaderEntity -> StringUtils.equalsIgnoreCase(httpRequestLogHeaderEntity.getHeaderName(), headerName))
            .map(HttpRequestLogHeaderEntity::getHeaderValue)
            .collect(Collectors.toSet());

        if (headerValues.isEmpty())
            return null;

        if (headerValues.size() > 1)
            throw new RuntimeException("Header has multiple values.");

        return headerValues.iterator().next();
    }

    public long getHttpRequestId() {
        return httpRequestId;
    }

    public void setHttpRequestId(long httpRequestId) {
        this.httpRequestId = httpRequestId;
    }

    public Instant getCreatedAtTimestamp() {
        return createdAtTimestamp;
    }

    public void setCreatedAtTimestamp(Instant createdAtTimestamp) {
        this.createdAtTimestamp = createdAtTimestamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public List<HttpRequestLogHeaderEntity> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HttpRequestLogHeaderEntity> headers) {
        this.headers = headers;
    }
}
