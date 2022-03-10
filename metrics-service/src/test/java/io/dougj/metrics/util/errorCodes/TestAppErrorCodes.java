package io.dougj.metrics.util.errorCodes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAppErrorCodes {

    @Test
    public void testGetErrorCode() {
        ErrorCodes errorCodes = AppErrorCodes.APP_ERROR_NO_DATA;
        Assertions.assertEquals("metrics.service.data", errorCodes.getErrorCode());
        Assertions.assertEquals("no data found", errorCodes.getErrorMessage());
    }
}
