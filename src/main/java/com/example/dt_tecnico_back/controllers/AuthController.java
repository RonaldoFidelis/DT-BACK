package com.example.dt_tecnico_back.controllers;

import com.example.dt_tecnico_back.auth.security.TokenService;
import com.example.dt_tecnico_back.domain.user.User;
import com.example.dt_tecnico_back.dto.LoginRequestDTO;
import com.example.dt_tecnico_back.dto.RegisterDTO;
import com.example.dt_tecnico_back.dto.ResponseDTO;
import com.example.dt_tecnico_back.repositores.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(passwordEncoder.matches(body.password(), user.getPassword())){
            String token = this.tokenService.generatedToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO body){
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()){
            User newUser = new User();
            newUser.setName(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            this.repository.save(newUser);
            String token = this.tokenService.generatedToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    //implementar o método de recover de senha
}
