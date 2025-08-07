package com.example.dt_tecnico_back.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Classe que representa o usuário dentro do DB
 * Atributos do usuario: ID, NAME, EMAIL E PASSWORD
 * Todos os atributos serão privados
 * */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String email;
    private String password;
    private String resetToken;
    private LocalDateTime tokenExpiration;

}
