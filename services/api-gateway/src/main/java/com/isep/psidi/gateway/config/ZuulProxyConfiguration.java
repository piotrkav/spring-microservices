package com.isep.psidi.gateway.config;

import com.isep.psidi.gateway.filter.CustomLocationRewriteFilter;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableZuulProxy
public class ZuulProxyConfiguration {

    @Bean
    public CustomLocationRewriteFilter locationRewriteFilter() {
        return new CustomLocationRewriteFilter();
    }
}
