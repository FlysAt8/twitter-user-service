package com.twitter.userservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcUserRoleRepository implements UserRoleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void assignRole(UUID userId, Long roleId) {
        jdbcTemplate.update("INSERT INTO user_role (user_id, role_id) VALUES (?, ?)", userId, roleId);
    }

    @Override
    public boolean exists(UUID userId, Long roleId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_role WHERE user_id = ? AND role_id = ?",
                Integer.class, userId, roleId);
        return count != null && count > 0;
    }

    @Override
    public List<Long> findRoleIdsByUserId(UUID userId) {
        return jdbcTemplate.queryForList(
                "SELECT role_id FROM user_role WHERE user_id = ?", Long.class, userId);
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", userId);
    }

    @Override
    public void removeRole(UUID userId, Long roleId) {
        jdbcTemplate.update(
                "DELETE FROM user_role WHERE user_id = ? AND role_id = ?",
                userId,
                roleId
        );
    }
}