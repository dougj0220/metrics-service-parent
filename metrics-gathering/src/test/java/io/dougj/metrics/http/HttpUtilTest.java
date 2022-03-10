package io.dougj.metrics.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class HttpUtilTest {

    @Test
    public void testInstance() {
        HttpUtil httpUtil = new HttpUtil();
        assertNotNull(httpUtil);
    }
}
