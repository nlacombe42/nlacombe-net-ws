package net.nlacombe.nlacombenetws.features.httplog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "http_request_log_headers")
public class HttpRequestLogHeaderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int httpRequestHeaderId;

    private String headerName;
    private String headerValue;

    @ManyToOne
    @JoinColumn(name = "http_request_id")
    private HttpRequestLogEntity httpRequest;

    public HttpRequestLogHeaderEntity() {
    }

    public HttpRequestLogHeaderEntity(HttpRequestLogEntity httpRequest, String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
        this.httpRequest = httpRequest;
    }

    public int getHttpRequestHeaderId() {
        return httpRequestHeaderId;
    }

    public void setHttpRequestHeaderId(int httpRequestHeaderId) {
        this.httpRequestHeaderId = httpRequestHeaderId;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public HttpRequestLogEntity getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequestLogEntity httpRequest) {
        this.httpRequest = httpRequest;
    }
}
