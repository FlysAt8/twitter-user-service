package com.twitter.userservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.twitter.userservice.entity.User;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
