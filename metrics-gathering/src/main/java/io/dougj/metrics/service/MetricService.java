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

    public synchronized static MetricService getInstance() {
        if (INSTANCE == null) {
            LOG.info("creating new singleton instance of MetricService!!!");
            INSTANCE = new MetricService();
        }

        LOG.info("returning existing singleton instance of MetricService...");
        return INSTANCE;
    }

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

    public JsonObject getAllRequestResponseMetrics() {
        if (MapUtils.isEmpty(metricDataRecordsStorage)) {
            LOG.info("call to getAllRequestResponseMetrics() is returning null, no data to report on");
            return null;
        }
        JsonObject payload = new JsonObject();
        payload.addProperty(HttpUtil.REQUEST_TIME_MINIMUM_KEY, String.valueOf(getRequestTimeMinimum()));
        payload.addProperty(HttpUtil.REQUEST_TIME_MAXIMUM_KEY, String.valueOf(getRequestTimeMaximum()));
        payload.addProperty(HttpUtil.REQUEST_TIME_AVERAGE_KEY, String.valueOf(getRequestTimeAverage()));
        payload.addProperty(HttpUtil.RESPONSE_SIZE_MINIMUM_KEY, String.valueOf(getResponseSizeMinimum()));
        payload.addProperty(HttpUtil.RESPONSE_SIZE_MAXIMUM_KEY, String.valueOf(getResponseSizeMaximum()));
        payload.addProperty(HttpUtil.RESPONSE_SIZE_AVERAGE_KEY, String.valueOf(getResponseSizeAverage()));

        return payload;
    }

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

    private void initMetricDataRecordsStorage() {
        metricDataRecordsStorage.put(HttpUtil.REQUEST_TIME_LIST_KEY, new ArrayList<>());
        LOG.info("created new entry in metricDataRecordsStorage map for requestTimeList");
        metricDataRecordsStorage.put(HttpUtil.RESPONSE_SIZE_LIST_KEY, new ArrayList<>());
        LOG.info("created new entry in metricDataRecordsStorage map for responseSizeList");
    }

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

    private Long getRequestTimeMinimum() {
        List<Long> requestTimes = metricDataRecordsStorage.get(HttpUtil.REQUEST_TIME_LIST_KEY);
        // Arrays.sort(requestTimes.toArray());
        // requestTimes.get(0)
        return requestTimes.stream()
                .mapToLong(a -> a)
                .min()
                .orElse(0);
    }

    private Double getRequestTimeAverage() {
        List<Long> requestTimes = metricDataRecordsStorage.get(HttpUtil.REQUEST_TIME_LIST_KEY);
        return requestTimes.stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(0.0);
    }

    private Long getRequestTimeMaximum() {
        List<Long> requestTimes = metricDataRecordsStorage.get(HttpUtil.REQUEST_TIME_LIST_KEY);
        // Arrays.sort(requestTimes.toArray());
        // requestTimes.get(requestTimes.size()-1)
        return requestTimes.stream()
                .mapToLong(a -> a)
                .max()
                .orElse(0);
    }

    private Long getResponseSizeMinimum() {
        List<Long> responseSizes = metricDataRecordsStorage.get(HttpUtil.RESPONSE_SIZE_LIST_KEY);
        return Collections.min(responseSizes);
    }

    private Long getResponseSizeMaximum() {
        List<Long> responseSizes = metricDataRecordsStorage.get(HttpUtil.RESPONSE_SIZE_LIST_KEY);
        return Collections.max(responseSizes);
    }

    private Double getResponseSizeAverage() {
        List<Long> responseSizes = metricDataRecordsStorage.get(HttpUtil.RESPONSE_SIZE_LIST_KEY);
        return responseSizes.stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(0.0);
    }
}
