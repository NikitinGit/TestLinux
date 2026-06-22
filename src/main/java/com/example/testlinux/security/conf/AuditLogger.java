package com.example.testlinux.security.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogger {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    /**
     * @param action         create | update | delete | custom | impersonate | add-user | remove-user
     * @param userId         who performed the action
     * @param auditableType  "users" | "user_groups"
     * @param auditableId    PK of the changed entity
     * @param oldValues      nullable — fields before change
     * @param newValues      nullable — fields after change
     */
    public void log(String action,
                    Integer userId,
                    String  auditableType,
                    Long    auditableId,
                    Map<String, Object> oldValues,
                    Map<String, Object> newValues) {
        try {
            String ip  = resolveIp();
            String old = oldValues != null ? objectMapper.writeValueAsString(oldValues) : null;
            String nw  = newValues != null ? objectMapper.writeValueAsString(newValues) : null;

            jdbcTemplate.update("""
                INSERT INTO audit_logs
                    (action, user_id, auditable_type, auditable_id, old_values, new_values, ip, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                """, action, userId, auditableType, auditableId, old, nw, ip);
        } catch (Exception e) {
            log.warn("AuditLogger: failed to write audit entry — action={} userId={} type={} id={}",
                    action, userId, auditableType, auditableId, e);
        }
    }

    private String resolveIp() {
        try {
            var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return null;
            var req = attrs.getRequest();
            String forwarded = req.getHeader("X-Forwarded-For");
            if (forwarded != null && !forwarded.isBlank()) {
                return forwarded.split(",")[0].trim();
            }
            return req.getRemoteAddr();
        } catch (Exception e) {
            return null;
        }
    }
}