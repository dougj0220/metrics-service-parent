package io.dougj.metrics.http.wrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class MetricGatheringResponseWrapperTest {

    private final static String REQUEST_ID = UUID.randomUUID().toString();

    private MetricGatheringResponseWrapper responseWrapper;

    @Before
    public void setup() throws IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        responseWrapper = new MetricGatheringResponseWrapper(response, REQUEST_ID);
    }

    @Test
    public void testGetOutputStream() {
        ServletOutputStream servletOutputStream = responseWrapper.getOutputStream();
        assertNotNull(servletOutputStream);
    }

    @Test
    public void testGetPrintWriter() {
        PrintWriter printWriter = responseWrapper.getWriter();
        assertNotNull(printWriter);
    }

    @Test
    public void testGetResponse() {
        HttpServletResponse response = responseWrapper.getResponse();
        assertNotNull(response);
    }

    @Test
    public void testGetProcessingStartTime() {
        Instant responseProcessingStartTime = responseWrapper.getProcessingStartTime();
        assertNotNull(responseProcessingStartTime);
    }
}
