package com.example.dt_tecnico_back.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.dt_tecnico_back.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/** Conceito
 *  Desenvolver um component, que fique responsavel por gerar e
 *  validar os tokens JWT.
 *
 *  Vai ter três métodos:
 *
 *  generatedToken() -> que vai ser responsavel por gerar o token e devolver ao usuário
 *  no momento do login e/ou cadastro.
 *
 *  generatedTokenTime() -> vai gerar um prazo de validade para o token
 *
 *  validateToken -> vai validar o token no momento do login e retorna o atributo ao qual
 *  o token foi "vinculado"
 * */
@Service
public class TokenService {

    @Value("${api.security.token.keySecurity}")
    private String keySecurity;

    private Instant generatedTokenTime() {
        return LocalDateTime.now()
                .plusHours(1)
                .toInstant(ZoneOffset.ofHours(-3));

    }

    public String generatedToken(User user) {
        // Captura algum possível erro do auth0
        try {
            Algorithm algorithm = Algorithm.HMAC256(keySecurity);
            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generatedTokenTime())//tempo que o token expira
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Tentativa de autenticação");
        }

    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(keySecurity);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}
