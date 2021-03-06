package io.dougj.metrics.service;

import com.google.gson.JsonObject;
import io.dougj.metrics.http.HttpUtil;
import io.dougj.metrics.http.wrapper.MetricGatheringResponseWrapper;
import io.dougj.metrics.model.MetricDataRecord;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetricService {

    private static final Logger LOG = LoggerFactory.getLogger(MetricService.class);

    private static volatile MetricService INSTANCE = null;

    private final Map<String, List<Long>> metricDataRecordsStorage;
    private final Map<String, MetricDataRecord> uniqueRequestDataRecords;

    private MetricService() {
        if (INSTANCE != null) {
            throw new RuntimeException("use getInstance() to get instance of this object");
        }
        metricDataRecordsStorage = new ConcurrentHashMap<>();
        uniqueRequestDataRecords = new ConcurrentHashMap<>();
        initMetricDataRecordsStorage();
    }

    public static MetricService getInstance() {
        if (INSTANCE != null) {
            LOG.info("returning existing singleton instance of MetricService...");
            return INSTANCE;
        }
        synchronized (MetricService.class) {
            if (INSTANCE == null) {
                LOG.info("creating new singleton instance of MetricService!!!");
                INSTANCE = new MetricService();
            }
        }

        return INSTANCE;
    }

    /**
     * Utility method to capture the request time processing lifecycle,
     * response size in bytes and a record of each unique requestId and its
     * request/response data and store in a thread-safe in-memory data store
     *
     * @param httpRequest - HttpServletRequest
     * @param responseWrapper - MetricGatheringResponseWrapper
     * @param requestId - String
     * @throws IOException
     */
    public void collectRequestResponseData(HttpServletRequest httpRequest,
                                           MetricGatheringResponseWrapper responseWrapper,
                                           String requestId) throws IOException {
        try {
            metricDataRecordsStorage.get(HttpUtil.REQUEST_TIME_LIST_KEY).add(responseWrapper.getProcessingTime());
            metricDataRecordsStorage.get(HttpUtil.RESPONSE_SIZE_LIST_KEY).add(responseWrapper.getResponseByteCount());
            MetricDataRecord individualMetricDataRecord = buildMetricDataRecord(httpRequest, responseWrapper);
            uniqueRequestDataRecords.put(requestId, individualMetricDataRecord);
            LOG.info("requestURI: {}; headerRequestId: {}, responseSize: {} bytes; requestTime: {} millis; " +
                            "httpStatus: {}; httpMethod: {}",
                    httpRequest.getRequestURI(), requestId, responseWrapper.getResponseByteCount(),
                    responseWrapper.getProcessingTime(), responseWrapper.getStatus(), httpRequest.getMethod());
        } catch (IOException e) {
            LOG.error("error occurred collecting request/response data for requestId: {}", requestId, e);
            throw e;
        }
    }

    /**
     * Service method for to calculate minimum, average, and maximum
     * for request time and response size that can be returned to
     * calling client to display the metrics
     *
     * @return - JsonObject with map of all metrics data
     */
    public JsonObject getAllRequestResponseMetrics() {
        if (MapUtils.isEmpty(metricDataRecordsStorage)) {
            LOG.info("call to getAllRequestResponseMetrics() is returning null, no data to report on");
            return null;
        }
        List<Long> requestTimes = metricDataRecordsStorage.get(HttpUtil.REQUEST_TIME_LIST_KEY);
        List<Long> responseSizes = metricDataRecordsStorage.get(HttpUtil.RESPONSE_SIZE_LIST_KEY);
        JsonObject payload = new JsonObject();
        payload.addProperty(HttpUtil.REQUEST_TIME_MINIMUM_KEY, String.valueOf(getRequestTimeMinimum(requestTimes)));
        payload.addProperty(HttpUtil.REQUEST_TIME_MAXIMUM_KEY, String.valueOf(getRequestTimeMaximum(requestTimes)));
        payload.addProperty(HttpUtil.REQUEST_TIME_AVERAGE_KEY, String.valueOf(getRequestTimeAverage(requestTimes)));
        payload.addProperty(HttpUtil.RESPONSE_SIZE_MINIMUM_KEY, String.valueOf(getResponseSizeMinimum(responseSizes)));
        payload.addProperty(HttpUtil.RESPONSE_SIZE_MAXIMUM_KEY, String.valueOf(getResponseSizeMaximum(responseSizes)));
        payload.addProperty(HttpUtil.RESPONSE_SIZE_AVERAGE_KEY, String.valueOf(getResponseSizeAverage(responseSizes)));

        return payload;
    }

    /**
     * Lookup individual MetricDataRecord by unique requestId
     * for calling client to display request/response data record
     *
     * @param requestId - String
     * @return unique requestId MetricDataRecord
     */
    public MetricDataRecord findMetricDataRecordByRequestId(String requestId) {
        if (StringUtils.isEmpty(requestId)) {
            LOG.info("findMetricDataRecordByRequestId() returning no requestId value passed");
            return null;
        }
        MetricDataRecord metricDataRecord = uniqueRequestDataRecords.get(requestId);
        if (metricDataRecord == null) {
            LOG.info("findMetricDataRecordByRequestId() no value found for requestId: {}", requestId);
            return null;
        }

        return metricDataRecord;
    }

    /**
     * Set new array list in map to track request times and response sizes
     * in bytes so calculations can be performed on data
     */
    private void initMetricDataRecordsStorage() {
        metricDataRecordsStorage.put(HttpUtil.REQUEST_TIME_LIST_KEY, new ArrayList<>());
        LOG.info("created new entry in metricDataRecordsStorage map for requestTimeList");
        metricDataRecordsStorage.put(HttpUtil.RESPONSE_SIZE_LIST_KEY, new ArrayList<>());
        LOG.info("created new entry in metricDataRecordsStorage map for responseSizeList");
    }

    /**
     * Create a MetricDataRecord to be stored in-memory for that
     * unique requestId
     *
     * @param httpRequest - HttpServletRequest
     * @param responseWrapper - MetricGatheringResponseWrapper
     * @return the MetricDataRecord
     * @throws IOException
     */
    private MetricDataRecord buildMetricDataRecord(HttpServletRequest httpRequest,
                                                   MetricGatheringResponseWrapper responseWrapper) throws IOException {
        MetricDataRecord metricDataRecord = new MetricDataRecord();
        metricDataRecord.setRequestTimeMillis(responseWrapper.getProcessingTime());
        metricDataRecord.setResponseSizeBytes(responseWrapper.getResponseByteCount());
        metricDataRecord.setRequestURI(httpRequest.getRequestURI());
        metricDataRecord.setHttpMethod(httpRequest.getMethod());
        metricDataRecord.setHttpStatus(responseWrapper.getStatus());

        return metricDataRecord;
    }

    /**
     * @param requestTimes - List<Long>
     * @return the minimum request time for all requests or default to 0
     */
    private Long getRequestTimeMinimum(List<Long> requestTimes) {
        // Arrays.sort(requestTimes.toArray());
        // requestTimes.get(0)
        return requestTimes.stream()
                .mapToLong(a -> a)
                .min()
                .orElse(0);
    }

    /**
     * @param requestTimes - List<Long>
     * @return the average request time for all requests or default to 0.0
     */
    private Double getRequestTimeAverage(List<Long> requestTimes) {
        return requestTimes.stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(0.0);
    }

    /**
     * @param requestTimes - List<Long>
     * @return the maximum request time for all requests or default to 0
     */
    private Long getRequestTimeMaximum(List<Long> requestTimes) {
        // Arrays.sort(requestTimes.toArray());
        // requestTimes.get(requestTimes.size()-1)
        return requestTimes.stream()
                .mapToLong(a -> a)
                .max()
                .orElse(0);
    }

    /**
     * responseSizes - List<Long>
     * @return the minimum response size for all responses or default to 0
     */
    private Long getResponseSizeMinimum(List<Long> responseSizes) {
        return Collections.min(responseSizes);
    }

    /**
     * responseSizes - List<Long>
     * @return the maximum response size for all responses or default to 0
     */
    private Long getResponseSizeMaximum(List<Long> responseSizes) {
        return Collections.max(responseSizes);
    }

    /**
     * responseSizes - List<Long>
     * @return the average response size for all responses or default to 0.0
     */
    private Double getResponseSizeAverage(List<Long> responseSizes) {
        return responseSizes.stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(0.0);
    }
}
