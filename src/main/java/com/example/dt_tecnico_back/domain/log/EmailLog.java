package com.example.dt_tecnico_back.domain.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String recipient;
    private String subject;

    @Lob
    private String message;
    private String status;

    @Lob
    private String error;
    private LocalDateTime dateSend;
}
