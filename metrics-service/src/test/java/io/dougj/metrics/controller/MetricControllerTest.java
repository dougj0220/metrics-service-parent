package io.dougj.metrics.controller;

import com.google.gson.JsonObject;
import io.dougj.metrics.model.MetricDataRecord;
import io.dougj.metrics.service.MetricService;
import io.dougj.metrics.util.HttpUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetricController.class)
public class MetricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricService metricService;

    private final static String REQUEST_ID = UUID.randomUUID().toString();

    @Test
    public void testGetAllRequestResponseMetrics() throws Exception {
        JsonObject responsePayload = new JsonObject();
        when(metricService.getAllRequestResponseMetrics()).thenReturn(responsePayload);
        MvcResult result = mockMvc.perform(get(HttpUtil.METRICS_API))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }
    @Test
    public void testGetAllRequestResponseMetrics_noPayload() throws Exception {
        when(metricService.getAllRequestResponseMetrics()).thenReturn(null);
        mockMvc.perform(get(HttpUtil.METRICS_API))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void testGetAllRequestResponseMetrics_internalError() throws Exception {
        when(metricService.getAllRequestResponseMetrics()).thenThrow(new RuntimeException());
        mockMvc.perform(get(HttpUtil.METRICS_API))
                .andExpect(status().isServiceUnavailable()).andReturn();
    }

    @Test
    public void testFindByUniqueIdentifier() throws Exception{
        MetricDataRecord mdr = new MetricDataRecord();
        when((metricService.findMetricDataRecordByRequestId(anyString()))).thenReturn(mdr);
        MvcResult result = mockMvc.perform(get(HttpUtil.METRICS_API + "/" + REQUEST_ID))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testFindByUniqueIdentifier_noRequestId() throws Exception{
        mockMvc.perform(get(HttpUtil.METRICS_API + "/ "))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void testFindByUniqueIdentifier_noDataFound() throws Exception{
        when((metricService.findMetricDataRecordByRequestId(anyString()))).thenReturn(null);
        mockMvc.perform(get(HttpUtil.METRICS_API + "/12345"))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void testFindByUniqueIdentifier_internalError() throws Exception {
        when(metricService.findMetricDataRecordByRequestId(anyString())).thenThrow(new RuntimeException());
        mockMvc.perform(get(HttpUtil.METRICS_API + "/12345"))
                .andExpect(status().isServiceUnavailable()).andReturn();
    }
}
