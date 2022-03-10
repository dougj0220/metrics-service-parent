package io.dougj.metrics.http.wrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

@RunWith(JUnit4.class)
public class MetricGatheringServletOutputStreamTest {

    private MetricGatheringServletOutputStream metricGatheringServletOutputStream;

    @Before
    public void setup() {
        metricGatheringServletOutputStream =
                new MetricGatheringServletOutputStream(new MockHttpServletResponse().getOutputStream());
    }

    @Test
    public void testWriteInt() throws IOException {
        metricGatheringServletOutputStream.write(10);
    }

    @Test
    public void testWriteWriteBytes() throws IOException {
        byte[] writeBytes = "hello".getBytes();
        metricGatheringServletOutputStream.write(writeBytes, 1, 4);
    }

    @Test
    public void testClose() throws IOException {
        metricGatheringServletOutputStream.close();
    }

    @Test
    public void testIsReady() {
        assertFalse(metricGatheringServletOutputStream.isReady());
    }

    @Test
    public void testSetWriteListener() {
        metricGatheringServletOutputStream.setWriteListener(null);
    }
}
