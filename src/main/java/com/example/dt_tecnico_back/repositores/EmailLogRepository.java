package com.example.dt_tecnico_back.repositores;

import com.example.dt_tecnico_back.domain.log.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}
