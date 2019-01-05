package com.isep.psidi.gateway.filter;

import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.post.LocationRewriteFilter;
import org.springframework.http.HttpStatus;


public class CustomLocationRewriteFilter extends LocationRewriteFilter {
    /*
     * changes the response location to the gateway
     * */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        int statusCode = ctx.getResponseStatusCode();
        return HttpStatus.valueOf(statusCode).is3xxRedirection() || HttpStatus.valueOf(statusCode) == HttpStatus.CREATED;
    }
}
