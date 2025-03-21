package com.example.base_server.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration //This is necessary for the Bean declaration system
public class SecurityConfig {

    private final AutomationAuthFilter automationAuthFilter;

    public SecurityConfig(AutomationAuthFilter automationAuthFilter) {
        this.automationAuthFilter = automationAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //This bean randle the authentication by session system
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)// Exclude the CSRF requirement
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register", "/users/login", "/api/hello", "/users/verify", "/users/request-reset", "/users/reset").permitAll() // Let this routes open with no authentication
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/requisition/new").authenticated()
                        .requestMatchers(HttpMethod.POST, "/automation/process").hasRole("AUTOMATION")
                        .requestMatchers(HttpMethod.PUT, "/users/update").authenticated()
                        .requestMatchers("/books/**", "/reviews/**").authenticated()
                        .anyRequest().authenticated() // For any other routes, authentication is required.
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)  // Session configuration
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
                        .permitAll()
                )
                .addFilterBefore(automationAuthFilter, UsernamePasswordAuthenticationFilter.class); //This will include the Automation Authentication system.

        return http.build();
    }

    @Bean //For Spring Security authentication handle.
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(List.of(authProvider));
    }
}
