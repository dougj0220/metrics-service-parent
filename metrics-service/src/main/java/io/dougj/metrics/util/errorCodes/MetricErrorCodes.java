package io.dougj.metrics.util.errorCodes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum MetricErrorCodes implements ErrorCodes {
    METRIC_ERROR_GENERAL("metrics.service.general", "a general error occurred", new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)),
    METRIC_ERROR_NO_DATA("metrics.service.data","no data found", new ResponseEntity<>(HttpStatus.NOT_FOUND)),
    METRIC_ERROR_UUID_REQUIRED("metrics.service.data", "requestId required", new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    private final String errorCode;
    private final String errorMessage;
    private final ResponseEntity<?> responseEntity;

    MetricErrorCodes(String errorCode, String errorMessage, ResponseEntity<?> responseEntity) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.responseEntity = responseEntity;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public ResponseEntity<?>  getResponseEntity() {
        return responseEntity;
    }
}
