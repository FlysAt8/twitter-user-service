package com.twitter.userservice.repository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository {
    void assignRole(UUID userId, Long roleId);
    boolean exists(UUID userId, Long roleId);
    List<Long> findRoleIdsByUserId(UUID userId);
    void deleteAllByUserId(UUID userId);
}
