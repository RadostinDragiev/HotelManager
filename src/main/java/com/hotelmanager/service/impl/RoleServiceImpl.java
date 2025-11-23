package com.hotelmanager.service.impl;

import com.hotelmanager.model.entity.Role;
import com.hotelmanager.repository.RoleRepository;
import com.hotelmanager.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Set<Role> getRolesByIds(Collection<UUID> ids) {
        return this.roleRepository.findRolesByUuidIn(ids);
    }
}
