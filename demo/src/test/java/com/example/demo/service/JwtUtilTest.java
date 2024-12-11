package com.example.demo.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecretKey = "MZMmwzDDeG9Nw5cEk4Wp9Z/QMDCM+XQeeYE0g3uMMn4="; // Substitua pela chave gerada

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(testSecretKey); // Inicializa com a chave de teste
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("testuser");
        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("testuser");
        String username = jwtUtil.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void testValidateTokenSuccess() {
        String token = jwtUtil.generateToken("testuser");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testValidateTokenWithInvalidUser() {
        String token = jwtUtil.generateToken("testuser");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("otheruser")
                .password("password")
                .roles("USER")
                .build();

        assertFalse(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testIsTokenExpired() {
        String expiredToken = Jwts.builder()
                .setSubject("testuser")
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Token expirado
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(testSecretKey)), SignatureAlgorithm.HS256)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.extractUsername(expiredToken));
    }

    @Test
    void testExtractUsernameWithInvalidToken() {
        assertThrows(Exception.class, () -> jwtUtil.extractUsername("invalid-token"));
    }
}
