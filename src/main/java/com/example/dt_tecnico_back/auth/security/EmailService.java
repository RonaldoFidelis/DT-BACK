package com.example.dt_tecnico_back.auth.security;

import com.example.dt_tecnico_back.domain.log.EmailLog;
import com.example.dt_tecnico_back.dto.Email;
import com.example.dt_tecnico_back.repositores.EmailLogRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    public EmailService(JavaMailSender mailSender, EmailLogRepository emailLogRepository){
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository;
    }

    public void sendEmail(Email email) {
        EmailLog log = new EmailLog();
        log.setRecipient(email.to());
        log.setSubject(email.subject());
        log.setMessage(email.body());
        log.setDateSend(LocalDateTime.now());

        try {
            if (email.to().contains("error@test.com")) {
                throw new RuntimeException("Erro simulado no envio de e-mail:" +
                        "e-mail:error@test.com não é valido.");
            }

            var message = new SimpleMailMessage();
            message.setFrom("dtrecoverpassword@email.com");
            message.setTo(email.to());
            message.setSubject(email.subject());
            message.setText(email.body());

            mailSender.send(message);
            log.setStatus("SUCESS");
        } catch (Exception e) {
            log.setStatus("ERROR");
            log.setError(e.getMessage());
            throw e;
        } finally {
            emailLogRepository.save(log);
        }
    }
}
