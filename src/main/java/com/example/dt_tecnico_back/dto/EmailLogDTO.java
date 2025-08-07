package com.example.dt_tecnico_back.dto;

import java.time.LocalDateTime;

public record EmailLogDTO (String recipient, String status, String error, LocalDateTime dateSend) {
}
