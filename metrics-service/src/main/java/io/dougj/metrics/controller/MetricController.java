package io.dougj.metrics.controller;

import com.google.gson.JsonObject;
import io.dougj.metrics.model.MetricDataRecord;
import io.dougj.metrics.service.MetricService;
import io.dougj.metrics.util.HttpUtil;
import io.dougj.metrics.util.errorCodes.MetricErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HttpUtil.METRICS_API)
public class MetricController {

    private static final Logger LOG = LoggerFactory.getLogger(MetricController.class);

    private final MetricService metricService;

    @Autowired
    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRequestResponseMetrics() {
        try {
            LOG.info("getAllRequestResponseMetrics() API called...");
            JsonObject responsePayload = metricService.getAllRequestResponseMetrics();
            if (responsePayload == null) {
                LOG.info("no metric data records found");
                return HttpUtil.getResponse(MetricErrorCodes.METRIC_ERROR_NO_DATA);
            }

            LOG.info("getAllRequestResponseMetrics() returning responsePayload");
            return ResponseEntity.ok(responsePayload);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpUtil.getErrorResponse(MetricErrorCodes.METRIC_ERROR_GENERAL);
        }
    }

    @GetMapping(value = "/{requestId}",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByUniqueIdentifier(@PathVariable String requestId) {

        try {
            if (StringUtils.isBlank(requestId)) {
                LOG.info("findByUniqueIdentifier() missing required requestId");
                return HttpUtil.getResponse(MetricErrorCodes.METRIC_ERROR_UUID_REQUIRED);
            }
            LOG.info("getting metric data record for requestId: {}", requestId);
            MetricDataRecord payload = metricService.findMetricDataRecordByRequestId(requestId);
            if (payload == null) {
                LOG.info("No MetricDataRecord found for requestId: {}", requestId);
                return HttpUtil.getResponse(MetricErrorCodes.METRIC_ERROR_NO_DATA);
            }

            return ResponseEntity.ok(payload);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpUtil.getErrorResponse(MetricErrorCodes.METRIC_ERROR_GENERAL);
        }
    }
}
