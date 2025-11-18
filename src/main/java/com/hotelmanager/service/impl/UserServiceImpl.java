package com.hotelmanager.service.impl;

import com.hotelmanager.repository.UserRepository;
import com.hotelmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void updateLastLogin(String username) {
        this.userRepository.findByUsername(username)
                .ifPresent(user -> {
                    user.setLastLoginDateTime(LocalDateTime.now());
                    this.userRepository.save(user);
                });
    }
}
