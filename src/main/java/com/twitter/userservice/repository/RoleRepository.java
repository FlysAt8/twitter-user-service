package com.twitter.userservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.twitter.userservice.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query("""
            SELECT r.id, r.name
            FROM roles r
            JOIN user_role ur ON ur.role_id = r.id
            WHERE ur.user_id = :userId
            """)
    List<Role> findAllByUserId(UUID userId);
}