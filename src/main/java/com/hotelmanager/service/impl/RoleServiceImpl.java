package com.hotelmanager.service.impl;

import com.hotelmanager.model.dto.response.RoleDto;
import com.hotelmanager.model.entity.Role;
import com.hotelmanager.repository.RoleRepository;
import com.hotelmanager.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public Set<Role> getRolesByIds(Collection<UUID> ids) {
        return this.roleRepository.findRolesByUuidIn(ids);
    }

    @Override
    public Set<RoleDto> getAllRoles() {
        return this.roleRepository.findAll().stream()
                .map(role -> this.modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toSet());
    }
}
