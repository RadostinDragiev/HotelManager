package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.model.dto.request.AuthRequestDto;
import com.hotelmanager.model.entity.Role;
import com.hotelmanager.model.entity.User;
import com.hotelmanager.repository.RoleRepository;
import com.hotelmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends IntegrationBaseTest {

    private static final String USERNAME = "john";
    private static final String PASSWORD = "password123";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL = "john@hotelmanager.com";
    private static final String POSITION = "Manager";
    private static final String MANAGER_ROLE = "MANAGER";
    private static final String USER_ROLE = "USER";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.userRepository.deleteAll();

        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(this.passwordEncoder.encode(PASSWORD));
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEnabled(true);
        user.setPosition(POSITION);
        user.setLastLoginDateTime(LocalDateTime.now());

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found")));
        roles.add(roleRepository.findByName("MANAGER")
                .orElseThrow(() -> new RuntimeException("MANAGER role not found")));
        user.setRoles(roles);

        this.userRepository.save(user);
    }

    @Test
    @DisplayName("Should return 200 when login with correct credentials and valid user")
    void testLoginWithValidUser() throws Exception {
        AuthRequestDto request = new AuthRequestDto();
        request.setUsername(USERNAME);
        request.setPassword(PASSWORD);

        this.mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.id").isNotEmpty())
                .andExpect(jsonPath("$.user.username").value(USERNAME))
                .andExpect(jsonPath("$.user.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.user.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.user.email").value(EMAIL))
                .andExpect(jsonPath("$.user.roles[*].name", hasItem(USER_ROLE)))
                .andExpect(jsonPath("$.user.roles[*].name", hasItem(MANAGER_ROLE)))
                .andExpect(jsonPath("$.user.createdDateTime").isNotEmpty());

        User updated = this.userRepository.findByUsername(USERNAME).orElseThrow();
        assertThat(updated.getLastLoginDateTime()).isNotNull();
    }

    @Test
    @DisplayName("Should return 401 when wrong password is used")
    void testLoginWithInvalidCredentialsShouldReturnErrorStatus() throws Exception {
        AuthRequestDto request = new AuthRequestDto();
        request.setUsername(USERNAME);
        request.setPassword("wrong-password");

        this.mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 400 when username is too short")
    void testLoginWithInvalidUsernameShouldReturnValidationError() throws Exception {
        AuthRequestDto request = new AuthRequestDto();
        request.setUsername("a");
        request.setPassword("password123");

        this.mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("username"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("Username should be between 4 and 20 characters!"));
    }

    @Test
    @DisplayName("Should return 400 when password is too short")
    void testLoginWithInvalidPasswordShouldReturnValidationError() throws Exception {
        AuthRequestDto request = new AuthRequestDto();
        request.setUsername("john");
        request.setPassword("a");

        this.mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("password"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("Password should be between 8 and 20 characters!"));
    }

    @Test
    @DisplayName("Should return 400 when username and password are too short")
    void testLoginWithInvalidUsernameAndPasswordShouldReturnValidationError() throws Exception {
        AuthRequestDto request = new AuthRequestDto();
        request.setUsername("a");
        request.setPassword("a");

        this.mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("username")))
                .andExpect(jsonPath("$.fieldErrors[*].message", hasItem("Username should be between 4 and 20 characters!")))
                .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("password")))
                .andExpect(jsonPath("$.fieldErrors[*].message", hasItem("Password should be between 8 and 20 characters!")));
    }

    @Test
    @DisplayName("Should return 401 when user is locked")
    void testLoginWithLockedUserShouldReturnLockedError() throws Exception {
        User user = this.userRepository.findByUsername(USERNAME).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        this.userRepository.save(user);

        AuthRequestDto request = new AuthRequestDto();
        request.setUsername(USERNAME);
        request.setPassword(PASSWORD);

        this.mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("User account is locked"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.timestamp").isString());
    }
}
