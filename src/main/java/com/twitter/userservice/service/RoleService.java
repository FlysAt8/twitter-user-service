package com.twitter.userservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.twitter.userservice.entity.Role;
import com.twitter.userservice.exceptions.ConflictException;
import com.twitter.userservice.exceptions.NotFoundException;
import com.twitter.userservice.repository.RoleRepository;
import com.twitter.userservice.repository.UserRepository;
import com.twitter.userservice.util.DaoHelper;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DaoHelper daoHelper;

    public Iterable<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void assignRole(UUID userId, Long roleId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        roleRepository.findById(roleId)
            .orElseThrow(() -> new NotFoundException("Роль не найдена"));

        if (daoHelper.userHasRole(userId, roleId)) {
            throw new ConflictException("У пользователя уже есть эта роль");
        }
        daoHelper.assignRoleToUser(userId, roleId);
    }
}