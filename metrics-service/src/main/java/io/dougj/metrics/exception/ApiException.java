package io.dougj.metrics.exception;

import org.springframework.http.ResponseEntity;

public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final ResponseEntity<?> responseEntity;

    public ApiException(String message, ResponseEntity<?> responseEntity) {
        super(message);
        this.responseEntity = responseEntity;
    }

    public ResponseEntity<?> getResponseEntity() {return responseEntity;}
}
