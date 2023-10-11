package com.lu.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class Filter1 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");


        response.addHeader("X-Frame-Options","SAMEORIGIN");
        response.addHeader("Referer-Policy","origin");
        response.addHeader("Content-Security-Policy","object-src 'self'");
        response.addHeader("X-Permitted-Cross-Domain-Policies","master-only");
        response.addHeader("X-Content-Type-Options","nosniff");
        response.addHeader("X-XSS-Protection","1; mode=block");
        response.addHeader("X-Download-Options","noopen");
        response.addHeader("X-Frame-Options","SAMEORIGIN");
        response.addHeader("Strict-Transport-Security","max-age=63072000; includeSubdomains; preload");
        filterChain.doFilter(servletRequest,servletResponse);
        }


    @Override
    public void destroy() {

    }
}
