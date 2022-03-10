package io.dougj.metrics.filter;

import io.dougj.metrics.http.wrapper.MetricGatheringResponseWrapper;
import io.dougj.metrics.service.MetricService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class RequestResponseDataFilterTest {

    private RequestResponseDataFilter requestResponseDataFilter;
    private MetricService metricService;

    @Before
    public void setup() {
        metricService = mock(MetricService.class);
        requestResponseDataFilter = new RequestResponseDataFilter(metricService);
    }

    @Test
    public void testDoFilter() throws IOException, ServletException {
        // Given: data is passed
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        // Given: expectations are set
        doNothing().when(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        doNothing().when(metricService).collectRequestResponseData(any(HttpServletRequest.class), any(MetricGatheringResponseWrapper.class), any(String.class));

        // When: business logic is called
        requestResponseDataFilter.doFilter(request, response, filterChain);

        // Then: proper logic and methods are called
        verify(filterChain, times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        verify(metricService, times(1)).collectRequestResponseData(any(HttpServletRequest.class), any(MetricGatheringResponseWrapper.class), any(String.class));
    }

    @Test
    public void testInit() throws ServletException {
        FilterConfig filterConfig = mock(FilterConfig.class);
        requestResponseDataFilter.init(filterConfig);
    }

    @Test
    public void testDestroy() {
        requestResponseDataFilter.destroy();
    }
}
