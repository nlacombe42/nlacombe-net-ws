package net.nlacombe.nlacombenetws.features.analytics;

import java.util.Map;

public class HttpRequestLogDetailsDto extends HttpRequestLogSummaryDto {

    private Map<String, String> requestHeaders;

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }
}
