package com.twitter.userservice.util;

import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DaoHelper {
    private final JdbcTemplate jdbcTemplate;

    public void assignRoleToUser(UUID userId, Long roleId) {
        jdbcTemplate.update("INSERT INTO user_role (user_id, role_id) VALUES (?, ?)", 
                userId, roleId);
    }

    public boolean userHasRole(UUID userId, Long roleId) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM user_role WHERE user_id = ? AND role_id = ?", 
            Integer.class, userId, roleId);
        return count != null && count > 0;
    }
}
