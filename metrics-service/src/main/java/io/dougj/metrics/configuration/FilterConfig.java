package io.dougj.metrics.configuration;

import io.dougj.metrics.filter.RequestResponseDataFilter;
import io.dougj.metrics.service.MetricService;
import io.dougj.metrics.util.HttpUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class FilterConfig {

    /**
     * Create MetricService Bean as Singleton to be used for metrics endpoint calls
     * @return MetricService
     */
    @Bean
    @Order(1)
    public MetricService metricServiceInstance() {
        return MetricService.getInstance();
    }

    /**
     * Register the metrics gathering library RequestResponseDataFilter
     * as priority 1 filter to track request / response data.
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<RequestResponseDataFilter> registerRequestResponseDataFilter() {
        FilterRegistrationBean<RequestResponseDataFilter> registrationBean  = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestResponseDataFilter(metricServiceInstance()));
        registrationBean.addUrlPatterns(HttpUtil.API_ROOT + "/*"); // capture all endpoint data
        registrationBean.setOrder(1);   // want this filter to fire first in order of precedence

        return registrationBean;
    }
}
