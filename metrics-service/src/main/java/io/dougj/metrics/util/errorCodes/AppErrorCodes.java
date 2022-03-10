package io.dougj.metrics.util.errorCodes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum AppErrorCodes implements ErrorCodes {
    APP_ERROR_NO_DATA("metrics.service.data", "no data found", new ResponseEntity<>(HttpStatus.NOT_FOUND));

    private final String errorCode;
    private final String errorMessage;
    private final ResponseEntity<?> responseEntity;

    AppErrorCodes(String errorCode, String errorMessage,ResponseEntity<?> responseEntity) {
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
