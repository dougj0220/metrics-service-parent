package io.dougj.metrics.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MetricDataRecordTest {

    @Test
    public void testConstructor() {
        MetricDataRecord mdr = new MetricDataRecord();
        Assert.assertNotNull(mdr);
        Assert.assertNotNull(mdr.getCreatedTimeStampUtc());
    }

    @Test
    public void testGettersAndSetters(){
        MetricDataRecord mdr = new MetricDataRecord();
        mdr.setRequestTimeMillis(100L);
        mdr.setResponseSizeBytes(10L);
        mdr.setHttpStatus(200);
        mdr.setHttpMethod("GET");
        mdr.setRequestURI("/api/va/app/version");
        Assert.assertEquals(Long.valueOf(100L), mdr.getRequestTimeMillis());
        Assert.assertEquals(Long.valueOf(10L), mdr.getResponseSizeBytes());
        Assert.assertEquals(Integer.valueOf(200), mdr.getHttpStatus());
        Assert.assertEquals("GET", mdr.getHttpMethod());
        Assert.assertEquals("/api/va/app/version", mdr.getRequestURI());
        Assert.assertNotNull(mdr.getCreatedTimeStampUtc());
    }
}
