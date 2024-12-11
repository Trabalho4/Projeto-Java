package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(userService.getAllUsers().isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.getUserById(99L));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateUser_Success() {
        User user = new User();
        user.setName("New User");
        user.setEmail("newuser@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("New User", result.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_InvalidData() {
        User invalidUser = new User();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(invalidUser));
        assertEquals("Invalid user data", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(99L));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).existsById(99L);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteUser_NullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
        assertEquals("User ID cannot be null", exception.getMessage());
        verify(userRepository, never()).deleteById(anyLong());
    }





    @Test
    void testUpdateUser_NullId() {
        User user = new User();
        user.setName("Valid Name");
        user.setEmail("valid@example.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null, user));
        assertEquals("User ID cannot be null", exception.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void testUpdateUser_MissingEmail() {
        // Configura um usuário com dados inválidos (falta o email)
        User invalidUser = new User();
        invalidUser.setName("Valid Name"); // Nome presente, email ausente

        // Simula a existência do usuário no repositório
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        // Verifica que uma exceção é lançada com a mensagem esperada
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser(1L, invalidUser)
        );
        assertEquals("Email must not be empty", exception.getMessage());

        // Certifica que nenhum usuário foi salvo no repositório
        verify(userRepository, never()).save(any(User.class));
    }



    @Test
    void testUpdateUser_MissingName() {
        // Configura um usuário com dados inválidos (falta o nome)
        User invalidUser = new User();
        invalidUser.setEmail("valid@example.com"); // Email presente, nome ausente

        // Simula a existência do usuário no repositório
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        // Verifica que uma exceção é lançada com a mensagem esperada
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser(1L, invalidUser)
        );
        assertEquals("Name must not be empty", exception.getMessage());

        // Certifica que nenhum usuário foi salvo no repositório
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void testUpdateUser_Success() {
        // Arrange: cria objetos simulados para o teste
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        // Simula comportamento do repositório
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act: chama o método de serviço para atualizar o usuário
        User result = userService.updateUser(1L, updatedUser);

        // Assert: verifica o resultado esperado
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());

        // Verifica interações com o repositório
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }




}
