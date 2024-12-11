package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername() {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password");
        mockUser.setEmail("testuser@example.com");
        mockUser.setRole("USER");

        when(userRepository.findByEmail("testuser@example.com")).thenReturn(java.util.Optional.of(mockUser));

        var userDetails = customUserDetailsService.loadUserByUsername("testuser@example.com");

        assertNotNull(userDetails);
        assertEquals("testuser@example.com", userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail("testuser@example.com");
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("notfound@example.com")
        );

        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }
}
