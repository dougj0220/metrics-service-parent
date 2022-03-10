package io.dougj.metrics.util.errorCodes;

import org.springframework.http.ResponseEntity;

public interface ErrorCodes {
    String getErrorCode();
    String getErrorMessage();
    ResponseEntity<?> getResponseEntity();
}
