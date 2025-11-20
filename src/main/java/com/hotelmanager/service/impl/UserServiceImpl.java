package com.hotelmanager.service.impl;

import com.hotelmanager.exception.exceptions.PasswordsDoesNotMatchException;
import com.hotelmanager.exception.exceptions.RolesNotFoundException;
import com.hotelmanager.exception.exceptions.UserNotFoundException;
import com.hotelmanager.model.dto.request.ProfilePasswordDto;
import com.hotelmanager.model.dto.request.UserDto;
import com.hotelmanager.model.dto.response.ProfileDto;
import com.hotelmanager.model.entity.Role;
import com.hotelmanager.model.entity.User;
import com.hotelmanager.repository.UserRepository;
import com.hotelmanager.service.RoleService;
import com.hotelmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static com.hotelmanager.exception.ExceptionMessages.NEW_PASSWORDS_DOES_NOT_MATCH;
import static com.hotelmanager.exception.ExceptionMessages.OLD_PASSWORD_DOES_NOT_MATCH;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateLastLogin(String username) {
        this.userRepository.findByUsername(username)
                .ifPresent(user -> {
                    user.setLastLoginDateTime(LocalDateTime.now());
                    this.userRepository.save(user);
                });
    }

    @Override
    public UUID createUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User creationUser = this.userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No user with provided username found!"));
        user.setCreatedBy(creationUser);
        user.setRoles(fetchRolesByIds(userDto.getRoles()));

        return this.userRepository.save(user).getUuid();
    }

    @Override
    public ProfileDto getUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User creationUser = this.userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No user with provided username found!"));

        return this.modelMapper.map(creationUser, ProfileDto.class);
    }

    @Override
    public void updateProfilePassword(ProfilePasswordDto passwordDto) {
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmNewPassword())) {
            throw new PasswordsDoesNotMatchException(NEW_PASSWORDS_DOES_NOT_MATCH);
        }

        User user = getAuthenticationUser();
        if (!this.passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new PasswordsDoesNotMatchException(OLD_PASSWORD_DOES_NOT_MATCH);
        }

        user.setPassword(this.passwordEncoder.encode(passwordDto.getNewPassword()));
        this.userRepository.save(user);
    }

    @Override
    public void activateUser(String id) {
        User user = this.userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UserNotFoundException("No user found by the provided id!"));
        user.setEnabled(true);

        this.userRepository.save(user);
    }

    @Override
    public void deactivateUser(String id) {
        User user = this.userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UserNotFoundException("No user found by the provided id!"));
        user.setEnabled(false);

        this.userRepository.save(user);
    }

    private Set<Role> fetchRolesByIds(Set<UUID> roleIds) {
        Set<Role> roles = this.roleService.getRolesByIds(roleIds);
        if (roles == null || roles.isEmpty() || (roles.size() != roleIds.size())) {
            throw new RolesNotFoundException("Invalid role ids provided!");
        }

        return roles;
    }

    private User getAuthenticationUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return this.userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No user with provided username found!"));
    }
}
