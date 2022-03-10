package io.dougj.metrics.util;

import io.dougj.metrics.dto.ApiError;
import io.dougj.metrics.util.errorCodes.ErrorCodes;
import org.springframework.http.ResponseEntity;

public class HttpUtil {
    // API root URI
    public static final String API_ROOT = "/api/v1";

    // ENDPOINT URI's
    public static final String METRICS_API = API_ROOT + "/metrics";
    public static final String APP_API = API_ROOT + "/app";
    public static final String APP_VERSION_ENDPOINT = "/version";
    public static final String APP_DATA_ENDPOINT = "/data";

    // JSON keys
    public static final String APP_VERSION_KEY = "app-version";

    public static ResponseEntity<?> getErrorResponse(ErrorCodes errorCodes) {
        ApiError apiError = new ApiError(errorCodes.getErrorCode(), errorCodes.getErrorMessage());

        return new ResponseEntity<>(apiError, errorCodes.getResponseEntity().getStatusCode());
    }

    public static ResponseEntity<?> getResponse(ErrorCodes errorCodes) {
        return new ResponseEntity<>(errorCodes.getResponseEntity().getStatusCode());
    }
}
