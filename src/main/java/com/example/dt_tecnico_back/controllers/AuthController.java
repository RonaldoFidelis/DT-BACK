package com.example.dt_tecnico_back.controllers;

import com.example.dt_tecnico_back.auth.security.EmailService;
import com.example.dt_tecnico_back.auth.security.TokenService;
import com.example.dt_tecnico_back.domain.log.EmailLog;
import com.example.dt_tecnico_back.domain.user.User;
import com.example.dt_tecnico_back.dto.Email;
import com.example.dt_tecnico_back.dto.LoginRequestDTO;
import com.example.dt_tecnico_back.dto.RegisterDTO;
import com.example.dt_tecnico_back.dto.EmailLogDTO;
import com.example.dt_tecnico_back.dto.ResponseDTO;
import com.example.dt_tecnico_back.repositores.EmailLogRepository;
import com.example.dt_tecnico_back.repositores.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailLogRepository emailLogRepository;


    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(passwordEncoder.matches(body.password(), user.getPassword())){
            String token = this.tokenService.generatedToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getEmail(),user.getName(), token));
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
            return ResponseEntity.ok(new ResponseDTO(newUser.getEmail(),newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/recover")
    public ResponseEntity recoverPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<User> userOpt = repository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        User user = userOpt.get();

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusMinutes(30));
        repository.save(user);

        String resetLinkPassword = "Link de redefinição de senha=" + token;

        String messageEmail = String.format("Olá! %s, clique no link para redefinir a sua senha: %s", user.getName(), resetLinkPassword);
        try {
            emailService.sendEmail(new Email(
                    user.getEmail(),
                    "Redefinição de senha",
                    messageEmail
            ));
            return ResponseEntity.ok("E-mail enviado com instruções de recuperação.");
        } catch (Exception  e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar o e-mail: " + e.getMessage());
        }
    }

    @GetMapping("/log")
    public ResponseEntity<List<EmailLogDTO>> getEmailLogs() {
        List<EmailLog> logs = emailLogRepository.findAll();

        List<EmailLogDTO> dtos = logs.stream()
                .map(log -> new EmailLogDTO(
                        log.getRecipient(),
                        log.getStatus(),
                        log.getError(),
                        log.getDateSend()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }


}
