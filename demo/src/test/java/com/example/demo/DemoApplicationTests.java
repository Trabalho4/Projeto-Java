package com.example.demo;

import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.config.JwtRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void contextLoads() {
        // Verifica se o contexto do Spring foi carregado sem erros
    }

    @Test
    void testMainMethod() {
        DemoApplication.main(new String[]{});
    }
}
