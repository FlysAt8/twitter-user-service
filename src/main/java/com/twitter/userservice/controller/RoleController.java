package com.twitter.userservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.userservice.dto.ApiResponse;
import com.twitter.userservice.dto.RoleResponse;
import com.twitter.userservice.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping ("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles().stream()
                .map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(roles));
    }

    @PostMapping("/assign/{userId}/{roleId}")
    public ResponseEntity<ApiResponse<Void>> assignRole(@PathVariable UUID userId,
                                                        @PathVariable Long roleId){
        roleService.assignRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Роль успешно назначена"));
    }

    @DeleteMapping("/remove/{userId}/{roleId}")
    public ResponseEntity<ApiResponse<Void>> removeRole(
            @PathVariable UUID userId,
            @PathVariable Long roleId) {

        roleService.removeRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Роль успешно удалена"));
    }
}