package com.josiel.starwars.config.security;

import com.josiel.starwars.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_STRING = "Authorization";

    @Autowired
    private SecurityService securityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(HEADER_STRING);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                securityService.authenticate(token);
            } catch (Exception e) {
                System.out.println("Failed when authenticating token '{" + token + "}'. Error: '{" + e.getMessage() + "}'");
            }
        }

        filterChain.doFilter(request, response);
    }
}
