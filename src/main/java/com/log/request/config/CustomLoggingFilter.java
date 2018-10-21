package com.log.request.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoggingFilter extends CommonsRequestLoggingFilter {

    public CustomLoggingFilter() {
        setMaxPayloadLength(10000);
        setIncludePayload(true);
        setIncludeQueryString(true);
        setIncludeHeaders(true);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !this.isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        if (this.isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request);
        }
        boolean shouldLog = this.shouldLog(requestToUse);
        if (shouldLog && isFirstRequest) {
            this.beforeRequest(requestToUse, this.getBeforeMessage(requestToUse));
        }
        filterChain.doFilter(requestToUse, response);
        int status = response.getStatus();
        if (HttpStatus.valueOf(status).is2xxSuccessful()) {//We can customize as per our response status
            if (shouldLog && !this.isAsyncStarted(requestToUse)) {
                this.afterRequest(requestToUse, this.getAfterMessage(requestToUse));
            }
        }
    }

    private String getAfterMessage(HttpServletRequest request) {
        String afterMessagePrefix = "After request [";
        String afterMessageSuffix = "]";
        return this.createMessage(request, afterMessagePrefix, afterMessageSuffix);
    }

    private String getBeforeMessage(HttpServletRequest request) {
        String beforeMessagePrefix = "Before request [";
        String beforeMessageSuffix = "]";
        return this.createMessage(request, beforeMessagePrefix, beforeMessageSuffix);
    }
}
