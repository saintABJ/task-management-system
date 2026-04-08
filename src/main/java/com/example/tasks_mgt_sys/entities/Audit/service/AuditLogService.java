package com.example.tasks_mgt_sys.entities.Audit.service;

import com.example.tasks_mgt_sys.entities.Audit.AuditAction;

public interface AuditLogService {

    void logAction(AuditAction action, String entityName, Long entityId);

}
