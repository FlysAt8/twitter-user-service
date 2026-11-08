package com.twitter.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.userservice.entity.Role;
import com.twitter.userservice.service.RoleService;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping ("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Iterable<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
    
    @PostMapping("/assign/{userId}/{roleId}")
    public ResponseEntity<String> assignRole(@PathVariable UUID userId,
                                            @PathVariable Long roleId){
        roleService.assignRole(userId, roleId);
        return ResponseEntity.ok("Роль успешно назначена");
    }
}
