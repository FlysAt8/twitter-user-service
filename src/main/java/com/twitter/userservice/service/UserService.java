package com.twitter.userservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.twitter.userservice.dto.CreateUserRequest;
import com.twitter.userservice.dto.UpdateUserRequest;
import com.twitter.userservice.dto.UserResponse;
import com.twitter.userservice.entity.Role;
import com.twitter.userservice.entity.User;
import com.twitter.userservice.exceptions.ConflictException;
import com.twitter.userservice.exceptions.NotFoundException;
import com.twitter.userservice.repository.RoleRepository;
import com.twitter.userservice.repository.UserRepository;
import com.twitter.userservice.util.DaoHelper;
import com.twitter.userservice.util.UserMapper;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final DaoHelper daoHelper;
    private final PasswordEncoder passwordEncoder;

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

        daoHelper.assignRoleToUser(savedUser.getId(), guestRole.getId());

        UserResponse response = userMapper.toResponse(savedUser);
        response.setRoles(List.of(guestRole));
        return response;
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Long> roleIds = jdbcTemplate.queryForList(
                "SELECT role_id FROM user_role WHERE user_id = ?", Long.class, id);
        
        List<Role> roles = new ArrayList<>();
        roleRepository.findAllById(roleIds).forEach(roles::add);

        UserResponse response = userMapper.toResponse(user);
        response.setRoles(roles);
        return response;
    }

    public List<UserResponse> getUsersByIds(List<UUID> ids) {
        List<UserResponse> responses = new ArrayList<>();
        userRepository.findAllById(ids).forEach(user -> responses.add(userMapper.toResponse(user)));
        return responses;
    }

    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new ConflictException("Телефон уже занят другим пользователем");
            }
            user.setPhone(request.getPhone());
        }

        if (request.getFirstname() != null)
            user.setFirstname(request.getFirstname());
        if (request.getLastname() != null)
            user.setLastname(request.getLastname());

        User updatedUser = userRepository.save(user);

        List<Long> roleIds = jdbcTemplate.queryForList(
                "SELECT role_id FROM user_role WHERE user_id = ?", Long.class, id);
        List<Role> roles = new ArrayList<>();
        roleRepository.findAllById(roleIds).forEach(roles::add);

        UserResponse response = userMapper.toResponse(updatedUser);
        response.setRoles(roles);
        return response;
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
            
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", id);
        
        userRepository.delete(user);
    }
}
