package io.dougj.metrics.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ApiErrorTest {

    private static ApiError apiError;

    @BeforeAll
    public static void setup() {
        apiError = new ApiError("metrics.service.data", "no data found");
    }

    @Test
    public void testGetErrorCode() {
        Assertions.assertEquals("metrics.service.data", apiError.getErrorCode());
    }

    @Test
    public void testGetErrorMessage() {
        Assertions.assertEquals("no data found", apiError.getErrorMessage());
    }

    @Test
    public void testSetErrorCode() {
       ApiError apiError = new ApiError("metrics.service.data", "no data found");
       apiError.setErrorCode("new.error.code");
       Assertions.assertEquals("new.error.code", apiError.getErrorCode());
    }

    @Test
    public void testSetErrorMessage() {
        ApiError apiError = new ApiError("metrics.service.data", "no data found");
        apiError.setErrorMessage("new error message");
        Assertions.assertEquals("new error message", apiError.getErrorMessage());
    }
}
