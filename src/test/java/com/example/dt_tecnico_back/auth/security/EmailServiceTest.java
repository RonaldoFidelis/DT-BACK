package com.example.dt_tecnico_back.auth.security;

import com.example.dt_tecnico_back.dto.Email;
import com.example.dt_tecnico_back.repositores.EmailLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe de EmailService.
 */
class EmailServiceTest {

    // Mock das dependências
    JavaMailSender mailSender = mock(JavaMailSender.class);
    EmailLogRepository logRepository = mock(EmailLogRepository.class);

    // Instância do service, com dependências mockadas
    EmailService service = new EmailService(mailSender, logRepository);

    @Test
    void enviarEmailComSucessoESalvarLog() {
        // Arrange
        Email email = new Email("usuario@teste.com", "Assunto", "Mensagem");

        // Act
        service.sendEmail(email);

        // Assert
        // Verifica se o mailSender.send foi chamado uma vez com qualquer mensagem
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

        // Verifica se o logRepository.save foi chamado uma vez
        verify(logRepository, times(1)).save(any());
    }

    @Test
    void lancarErroParaEmailSimulado() {
        // Arrange
        Email email = new Email("error@test.com", "Assunto", "Mensagem");

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.sendEmail(email));
        assertTrue(ex.getMessage().contains("Erro simulado"));
    }
}
