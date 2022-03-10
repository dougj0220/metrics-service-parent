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

    /**
     * Create DTO that can be returned to client with
     * proper error message and http status code
     *
     * @param errorCodes - ErrorCodes
     * @return ResponseEntity with the proper http status code
     */
    public static ResponseEntity<?> getErrorResponse(ErrorCodes errorCodes) {
        ApiError apiError = new ApiError(errorCodes.getErrorCode(), errorCodes.getErrorMessage());

        return new ResponseEntity<>(apiError, errorCodes.getResponseEntity().getStatusCode());
    }

    /**
     * Create ResponseEntity that can be returned with proper http status code
     * @param errorCodes - ErrorCodes
     * @return
     */
    public static ResponseEntity<?> getResponse(ErrorCodes errorCodes) {
        return new ResponseEntity<>(errorCodes.getResponseEntity().getStatusCode());
    }
}
