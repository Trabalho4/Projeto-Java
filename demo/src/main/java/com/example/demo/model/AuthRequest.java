package com.example.demo.model;

public class AuthRequest {
    private String email; // Alterado de 'username' para 'email'
    private String password;

    // Getters e Setters
    public String getEmail() { // Corrigido
        return email;
    }

    public void setEmail(String email) { // Corrigido
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
