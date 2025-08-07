package com.example.dt_tecnico_back.auth.security;

import com.example.dt_tecnico_back.domain.user.User;
import com.example.dt_tecnico_back.repositores.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
/**
 * Implementação customizada da interface, utilizada pelo Spring Security
 * para carregar detalhes do usuário durante o processo de autenticação.
 *
 * A classe busca o usuário no banco de dados a partir do e-mail fornecido e retorna
 * uma instância, que contém as credenciais e permissões.
 */

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.repository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("usuário não encontrado"));
        return new org
                .springframework
                .security
                .core
                .userdetails
                .User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
