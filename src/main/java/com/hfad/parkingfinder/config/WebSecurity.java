package com.hfad.parkingfinder.config;


import com.hfad.parkingfinder.auth.service.AuthorizationService;
import com.hfad.parkingfinder.exceptions.UnauthorizedExcpetion;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class WebSecurity implements Filter {

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        val httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest.getServletPath().contains("/secured")) {
            val httpServletResponse = (HttpServletResponse) response;
            val bearerToken = httpServletRequest.getHeader("Authorization");
            if (bearerToken == null || bearerToken.isEmpty()) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
            try {
                authorizationService.validateUser(bearerToken);
            } catch (UnauthorizedExcpetion unauthorizedExcpetion) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}