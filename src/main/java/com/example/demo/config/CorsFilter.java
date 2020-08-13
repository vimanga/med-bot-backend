package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CorsFilter extends OncePerRequestFilter {
	
//	public static final String HEADER_NAME_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
//	public static final String HEADER_NAME_ALLOW_METHODS = "Access-Control-Allow-Methods";
//    public static final String HEADER_NAME_MAX_AGE = "Access-Control-Max-Age";
//    public static final String HEADER_NAME_ALLOW_HEADERS = "Access-Control-Allow-Headers";
//    public static final String VALUE_ALLOW_ORIGIN = "*";
//    public static final String VALUE_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
//    public static final String VALUE_MAX_AGE = "3600";
//    public static final String VALUE_ALLOW_HEADERS = "origin, content-type, accept, x-requested-with, clientid, traceFlag";
//    private CorsFilter() {
//    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, clientid, traceFlag");
        filterChain.doFilter(request, response);
    }
}




