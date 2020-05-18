package net.nlacombe.nlacombenetws.features.analytics;

import java.time.ZonedDateTime;

public class HttpRequestLogSummaryDto {

    private long httpRequestId;
    private ZonedDateTime createdAtDatetime;
    private String method;
    private String uri;
    private Integer responseStatus;
    private String host;

    public long getHttpRequestId() {
        return httpRequestId;
    }

    public void setHttpRequestId(long httpRequestId) {
        this.httpRequestId = httpRequestId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ZonedDateTime getCreatedAtDatetime() {
        return createdAtDatetime;
    }

    public void setCreatedAtDatetime(ZonedDateTime createdAtDatetime) {
        this.createdAtDatetime = createdAtDatetime;
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
}
