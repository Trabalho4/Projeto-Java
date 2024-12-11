package com.example.demo.config;

import com.example.demo.service.JwtUtil;
import com.example.demo.service.CustomUserDetailsService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil("TEST_SECRET_KEY_BASE64");
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return Mockito.mock(CustomUserDetailsService.class);
    }
}
