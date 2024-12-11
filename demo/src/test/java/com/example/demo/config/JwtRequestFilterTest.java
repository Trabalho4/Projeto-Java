package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import static org.mockito.Mockito.*;

class JwtRequestFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        UserDetails userDetails = mock(User.class);

        when(jwtUtil.extractUsername("valid-token")).thenReturn("testuser");
        when(jwtUtil.validateToken("valid-token", userDetails)).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(jwtUtil, times(1)).extractUsername("valid-token");
        verify(jwtUtil, times(1)).validateToken("valid-token", userDetails);
        verify(customUserDetailsService, times(1)).loadUserByUsername("testuser");
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtil.extractUsername("invalid-token")).thenThrow(new RuntimeException("Invalid token"));

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(jwtUtil, times(1)).extractUsername("invalid-token");
        verifyNoInteractions(customUserDetailsService);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithoutToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verifyNoInteractions(jwtUtil, customUserDetailsService);
        verify(chain, times(1)).doFilter(request, response);
    }
}
