package io.dougj.metrics.service;

import com.google.gson.JsonObject;
import io.dougj.metrics.http.HttpUtil;
import io.dougj.metrics.http.wrapper.MetricGatheringResponseWrapper;
import io.dougj.metrics.model.MetricDataRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MetricServiceTest {

    private final static String REQUEST_ID = UUID.randomUUID().toString();
    private MetricService metricService;

    private MetricGatheringResponseWrapper responseWrapper;

    @Before
    public void setup() throws IOException {
        metricService = MetricService.getInstance();
        MockHttpServletResponse response = new MockHttpServletResponse();
        responseWrapper = new MetricGatheringResponseWrapper(response, REQUEST_ID);
        responseWrapper.setProcessingStartTime(Instant.now());
    }

    @Test
    public void testGetInstance() {
        Assert.assertNotNull(metricService);
    }

    @Test
    public void testGetInstance_existingInstance() {
        MetricService metricService2 = MetricService.getInstance();
        assertEquals(metricService.hashCode(), metricService2.hashCode());
    }

    @Test
    public void testCollectRequestResponseData() throws IOException  {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/app/version");
        request.setMethod("GET");
        metricService.collectRequestResponseData(request, responseWrapper, REQUEST_ID);
        MetricDataRecord mdr = metricService.findMetricDataRecordByRequestId(REQUEST_ID);
        assertNotNull(mdr);
        assertEquals("GET", mdr.getHttpMethod());
        assertEquals("/api/v1/app/version", mdr.getRequestURI());
    }

    @Test(expected = IOException.class)
    public void testCollectRequestResponseData_missingData() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MetricGatheringResponseWrapper responseWrapper = mock(MetricGatheringResponseWrapper.class);
        when(responseWrapper.getResponseByteCount()).thenThrow(new IOException());
        metricService.collectRequestResponseData(request, responseWrapper, REQUEST_ID);
        verify(responseWrapper, times(1)).getResponseByteCount();
    }

    @Test
    public void testGetAllRequestResponseMetrics() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        metricService.collectRequestResponseData(request, responseWrapper, REQUEST_ID);
        JsonObject payload = metricService.getAllRequestResponseMetrics();
        assertNotNull(payload);
        assertEquals("0", payload.get(HttpUtil.RESPONSE_SIZE_MINIMUM_KEY).getAsString());
    }

    @Test
    public void testFindMetricDataRecordByRequestId() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        metricService.collectRequestResponseData(request, responseWrapper, REQUEST_ID);
        MetricDataRecord mdr = metricService.findMetricDataRecordByRequestId(REQUEST_ID);
        assertNotNull(mdr);
    }

    @Test
    public void testFindMetricDataRecordByRequestId_noRequestId() {
        MetricDataRecord mdr = metricService.findMetricDataRecordByRequestId(null);
        assertNull(mdr);
    }

    @Test
    public void testFindMetricDataRecordByRequestId_noMetricDataRecord() {
        MetricDataRecord mdr = metricService.findMetricDataRecordByRequestId("12345");
        assertNull(mdr);
    }
}
