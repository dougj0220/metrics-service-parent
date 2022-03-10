package io.dougj.metrics.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class MetricDataRecord {

    private Long requestTimeMillis;
    private Long responseSizeBytes;
    private String requestURI;
    private String httpMethod;
    private Integer httpStatus;
    private final Date createdTimeStampUtc;

    public MetricDataRecord() {
        this.createdTimeStampUtc = Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("Z")));
    }

    public Long getRequestTimeMillis() {return requestTimeMillis;}

    public void setRequestTimeMillis(Long requestTimeMillis) {
        this.requestTimeMillis = requestTimeMillis;
    }

    public Long getResponseSizeBytes() {
        return responseSizeBytes;
    }

    public void setResponseSizeBytes(Long responseSizeBytes) {
        this.responseSizeBytes = responseSizeBytes;
    }

    public String getRequestURI() {return requestURI;}

    public void setRequestURI(String requestURI) {this.requestURI = requestURI;}

    public String getHttpMethod() {return httpMethod;}

    public void setHttpMethod(String httpMethod) {this.httpMethod = httpMethod;}

    public Integer getHttpStatus() {return httpStatus;}

    public void setHttpStatus(Integer httpStatus) {this.httpStatus = httpStatus;}

    public Date getCreatedTimeStampUtc() {return createdTimeStampUtc;}
}
