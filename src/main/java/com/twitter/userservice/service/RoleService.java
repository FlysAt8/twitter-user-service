package com.twitter.userservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twitter.userservice.entity.Role;
import com.twitter.userservice.exceptions.ConflictException;
import com.twitter.userservice.exceptions.NotFoundException;
import com.twitter.userservice.repository.RoleRepository;
import com.twitter.userservice.repository.UserRepository;
import com.twitter.userservice.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public List<Role> getAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    @Transactional
    public void assignRole(UUID userId, Long roleId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Роль не найдена"));

        if (userRoleRepository.exists(userId, roleId)) {
            throw new ConflictException("У пользователя уже есть эта роль");
        }
        userRoleRepository.assignRole(userId, roleId);
    }

    @Transactional
    public void removeRole(UUID userId, Long roleId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Роль не найдена"));

        if (!userRoleRepository.exists(userId, roleId)) {
            throw new NotFoundException("У пользователя нет этой роли");
        }

        userRoleRepository.removeRole(userId, roleId);
    }
}