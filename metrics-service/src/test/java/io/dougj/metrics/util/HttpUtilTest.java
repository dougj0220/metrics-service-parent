package io.dougj.metrics.util;

import io.dougj.metrics.util.errorCodes.AppErrorCodes;
import io.dougj.metrics.util.errorCodes.MetricErrorCodes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class HttpUtilTest {

    @Test
    public void testHttpUtil() {
        HttpUtil httpUtil = new HttpUtil();
        Assertions.assertNotNull(httpUtil);
    }

    @Test
    public void testGetErrorResponse() {
        ResponseEntity<?> response = HttpUtil.getErrorResponse(MetricErrorCodes.METRIC_ERROR_GENERAL);
        Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    public void testGetResponse() {
        ResponseEntity<?> response = HttpUtil.getResponse(AppErrorCodes.APP_ERROR_NO_DATA);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
