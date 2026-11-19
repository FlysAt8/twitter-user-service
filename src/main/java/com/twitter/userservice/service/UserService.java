package com.twitter.userservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.twitter.userservice.dto.UserShortResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twitter.userservice.dto.CreateUserRequest;
import com.twitter.userservice.dto.UpdateUserRequest;
import com.twitter.userservice.dto.UserResponse;
import com.twitter.userservice.entity.Role;
import com.twitter.userservice.entity.User;
import com.twitter.userservice.exceptions.ConflictException;
import com.twitter.userservice.exceptions.NotFoundException;
import com.twitter.userservice.repository.RoleRepository;
import com.twitter.userservice.repository.UserRepository;
import com.twitter.userservice.repository.UserRoleRepository;
import com.twitter.userservice.util.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new ConflictException("Email уже занят");
        if (userRepository.existsByPhone(request.getPhone()))
            throw new ConflictException("Телефон уже занят");

        Role guestRole = roleRepository.findByName("guest")
                .orElseThrow(() -> new NotFoundException("Роль guest не найдена в БД"));

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        userRoleRepository.assignRole(savedUser.getId(), guestRole.getId());

        return userMapper.toResponse(savedUser, List.of(guestRole));
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Role> roles = getUserRoles(id);
        return userMapper.toResponse(user, roles);
    }

    public List<UserShortResponse> getUsersByIds(List<UUID> ids) {
        return ((List<User>) userRepository.findAllById(ids)).stream()
                .map(userMapper::toShortResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new ConflictException("Телефон уже занят другим пользователем");
            }
            user.setPhone(request.getPhone());
        }

        if (request.getFirstname() != null) user.setFirstname(request.getFirstname());
        if (request.getLastname() != null) user.setLastname(request.getLastname());

        User updatedUser = userRepository.save(user);
        List<Role> roles = getUserRoles(id);

        return userMapper.toResponse(updatedUser, roles);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        userRoleRepository.deleteAllByUserId(id);
        userRepository.delete(user);
    }

    private List<Role> getUserRoles(UUID userId) {
        List<Long> roleIds = userRoleRepository.findRoleIdsByUserId(userId);
        return (List<Role>) roleRepository.findAllById(roleIds);
    }
}