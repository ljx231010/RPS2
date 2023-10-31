package com.lu.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class HostFilter implements Filter {

    private static final String[] HOST_LIST = {"biolab.gxu.edu.cn:8080", "localhost:8080"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String host = request.getHeader("host");

        if (!checkBlankList(host)) {
            response.setStatus(403);
            return;
        }
        filterChain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }

    private boolean checkBlankList(String host) {
        boolean isAllow = false;
        if (host == null) {
            return true;
        }
        for (String blankHost : HOST_LIST) {
            if (blankHost.contains(host)) {
                isAllow = true;
            }
        }
        return isAllow;
    }
}
