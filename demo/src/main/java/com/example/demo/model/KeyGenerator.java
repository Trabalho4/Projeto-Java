package com.example.demo.model;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args) {
        // Gera uma chave segura de 256 bits para o algoritmo HS256
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Converte a chave em Base64 para que você possa usá-la no código
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        // Imprime a chave no console
        System.out.println("Chave gerada (Base64): " + base64Key);
    }
}
