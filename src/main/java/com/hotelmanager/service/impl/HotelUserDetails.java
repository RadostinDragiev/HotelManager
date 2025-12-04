package com.hotelmanager.service.impl;

import com.hotelmanager.model.dto.response.RoleDto;
import com.hotelmanager.model.dto.response.UserInfoDto;
import com.hotelmanager.model.entity.Role;
import com.hotelmanager.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record HotelUserDetails(User user) implements org.springframework.security.core.userdetails.UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    public UserInfoDto getAsUserInfoDto() {
        return UserInfoDto.builder()
                .id(this.user.getUuid().toString())
                .username(this.user.getUsername())
                .firstName(this.user.getFirstName())
                .lastName(this.user.getLastName())
                .email(this.user.getEmail())
                .roles(mapToRoleDto(this.user.getRoles()))
                .createdDateTime(this.user.getCreatedDateTime())
                .lastLoginDateTime(this.user.getLastLoginDateTime())
                .build();
    }

    private Set<RoleDto> mapToRoleDto(Set<Role> roles) {
        return roles.stream()
                .map(role -> new RoleDto(role.getUuid().toString(), role.getName()))
                .collect(Collectors.toSet());
    }
}
