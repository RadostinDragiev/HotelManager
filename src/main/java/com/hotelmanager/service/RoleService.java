package com.hotelmanager.service;

import com.hotelmanager.model.entity.Role;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface RoleService {

    Set<Role> getRolesByIds(Collection<UUID> ids);
}
