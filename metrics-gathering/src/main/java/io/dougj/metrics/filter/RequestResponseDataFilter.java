package io.dougj.metrics.filter;

import io.dougj.metrics.http.wrapper.MetricGatheringResponseWrapper;
import io.dougj.metrics.service.MetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


public class RequestResponseDataFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestResponseDataFilter.class);

    private final MetricService metricService;

    public RequestResponseDataFilter(MetricService metricService) {
        this.metricService = metricService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String uniqueRequestIdentifier = UUID.randomUUID().toString();
        LOG.info("starting doFilter() in RequestResponseDataFilter with uniqueRequestIdentifier: {}",
                uniqueRequestIdentifier);
        MetricGatheringResponseWrapper responseWrapper
                = new MetricGatheringResponseWrapper((HttpServletResponse)response, uniqueRequestIdentifier);
        try {
            chain.doFilter(request, responseWrapper);
        } finally {
            metricService.collectRequestResponseData((HttpServletRequest) request, responseWrapper, uniqueRequestIdentifier);
        }

        LOG.info("leaving doFilter() in RequestResponseDataFilter");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

   @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
