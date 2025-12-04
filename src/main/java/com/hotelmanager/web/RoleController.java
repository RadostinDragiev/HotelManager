package com.hotelmanager.web;

import com.hotelmanager.model.dto.response.RoleDto;
import com.hotelmanager.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER')")
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Set<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(this.roleService.getAllRoles());
    }
}
