package com.example.tasks_mgt_sys.entities.Audit.service;

import com.example.tasks_mgt_sys.entities.Audit.AuditAction;
import com.example.tasks_mgt_sys.entities.Audit.AuditLog;
import com.example.tasks_mgt_sys.entities.Audit.AuditLogRepository;
import com.example.tasks_mgt_sys.entities.user.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final CurrentUserUtil currentUserUtil;

    @Override
    public void logAction(AuditAction action, String entityName, Long entityId) {

        String performedBy;

        try {
            performedBy = currentUserUtil.getLoggedInUser().getEmail();
        } catch (Exception ex) {
            // fallback for system/CLI actions
            performedBy = "SYSTEM";
        }

        AuditLog auditLog = AuditLog.builder()
                .action(String.valueOf(action))
                .entity(entityName)
                .entityId(entityId)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(auditLog);
    }
}
