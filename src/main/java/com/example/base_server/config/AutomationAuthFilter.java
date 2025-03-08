package com.example.base_server.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

//This is the head class that gives the automation system credentials to manipulate the database.
@Component
public class AutomationAuthFilter implements Filter {

    @Value("${automation.token}") //Extract token from application.properties
    private String automationToken;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI(); // URL requisition

        // If it's not an auto requisition, ignores it
        if (!path.startsWith("/automation")) {
            chain.doFilter(request, response);
            return;
        }

        // If is an auto req, apply the token
        String token = httpRequest.getHeader("Automation-Token");

        if (token != null && token.equals(automationToken)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            "automation-user",
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_AUTOMATION"))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            throw new ServletException("Automation authentication denied!");
        }
    }
}
