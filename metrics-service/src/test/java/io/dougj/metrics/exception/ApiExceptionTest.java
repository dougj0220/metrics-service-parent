package io.dougj.metrics.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiExceptionTest {

    private static ApiException exception;

    @BeforeAll
    public static void setup() {
        exception = new ApiException("general error occurred", new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @Test
    public void testApiException() {
        Assertions.assertNotNull(exception);
    }

    @Test
    public void testGetResponseEntity() {
        ResponseEntity<?> responseEntity = exception.getResponseEntity();
        Assertions.assertNotNull(responseEntity);
    }
}
