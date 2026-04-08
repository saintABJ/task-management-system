package com.example.tasks_mgt_sys.entities.Audit;

import com.example.tasks_mgt_sys.entities.user.CurrentUserUtil;
import com.example.tasks_mgt_sys.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final CurrentUserUtil currentUserUtil;

    @AfterReturning(pointcut = "@annotation(audit)", returning = "result")
    public void logAudit(Audit audit, Object result) {

        User user = currentUserUtil.getLoggedInUser();

        Long entityId = extractEntityId(result);

        AuditLog log = AuditLog.builder()
                .action(audit.action())
                .entity(audit.entity())
                .entityId(entityId)
                .performedBy(user.getEmail())
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    private Long extractEntityId(Object result) {
        try {
            return (Long) result.getClass()
                    .getMethod("getId")
                    .invoke(result);
        } catch (Exception e) {
            return null;
        }
    }
}
