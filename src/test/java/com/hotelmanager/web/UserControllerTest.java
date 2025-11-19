package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.model.dto.request.UserDto;
import com.hotelmanager.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Set;
import java.util.UUID;

import static com.hotelmanager.validation.ValidationMessages.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(scripts = "/db/users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@WithMockUser(username = "testUser", roles = {"MANAGER"})
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends IntegrationBaseTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "username123";
    private static final String EMAIL = "username@hotelmanager.com";
    private static final String FIRST_NAME = "User";
    private static final String LAST_NAME = "Name";
    private static final String POSITION = "Position";
    private static final boolean IS_ENABLED = true;
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String EMAIL_FIELD = "email";
    private static final String FIRST_NAME_FIELD = "firstName";
    private static final String LAST_NAME_FIELD = "lastName";
    private static final String POSITION_FIELD = "position";
    private static final String IS_ENABLED_FIELD = "isEnabled";
    private static final String ROLES_FIELD = "roles";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Should return 200 when creating user with valid data")
    void testCreateUserWithValidData() throws Exception {
        UserDto dto = buildValidUserDto();

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("/users")));
    }

    @Test
    @DisplayName("Should return 400 when creating user with invalid role ids")
    void testCreateUserWithInvalidRoleIds() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setRoles(Set.of(UUID.randomUUID()));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Invalid role ids provided!"));
    }

    @WithMockUser(username = "invalid", roles = {"MANAGER"})
    @Test
    @DisplayName("Should return 404 when username provided not found")
    void testCreateUserWithByInvalidUser() throws Exception {
        UserDto dto = buildValidUserDto();

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("No user with provided username found!"));
    }

    @Test
    @DisplayName("Should return 400 when username provided already exists")
    void testCreateUserWithAlreadyExistingUsername() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setUsername("testUser");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(USERNAME_FIELD, UNIQUE_USERNAME));
    }

    @Test
    @DisplayName("Should return 400 when username provided is null")
    void testCreateUserWithNullUsername() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setUsername(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(USERNAME_FIELD, USERNAME_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when username provided is shorter than expected")
    void testCreateUserWithShorterUsername() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setUsername("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(USERNAME_FIELD, USERNAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when username provided is longer than expected")
    void testCreateUserWithLongerUsername() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setUsername("a".repeat(21));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(USERNAME_FIELD, USERNAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when password provided is null")
    void testCreateUserWithNullPassword() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPassword(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(PASSWORD_FIELD, PASSWORD_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when password provided is shorter than expected")
    void testCreateUserWithShorterPassword() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPassword("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(PASSWORD_FIELD, PASSWORD_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when password provided is longer than expected")
    void testCreateUserWithLongerPassword() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPassword("a".repeat(21));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(PASSWORD_FIELD, PASSWORD_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when email provided is null")
    void testCreateUserWithNullEmail() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setEmail(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(EMAIL_FIELD, EMAIL_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when email provided is invalid")
    void testCreateUserWithInvalidEmail() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setEmail("invalid.com");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(EMAIL_FIELD, VALID_EMAIL));
    }

    @Test
    @DisplayName("Should return 400 when first name provided is null")
    void testCreateUserWithNullFirstName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setFirstName(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(FIRST_NAME_FIELD, FIRST_NAME_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when username first name is shorter than expected")
    void testCreateUserWithShorterFirstName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setFirstName("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(FIRST_NAME_FIELD, FIRST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when first name provided is longer than expected")
    void testCreateUserWithLongerFirstName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setFirstName("a".repeat(51));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(FIRST_NAME_FIELD, FIRST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when last name provided is null")
    void testCreateUserWithNullLastName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setLastName(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(LAST_NAME_FIELD, LAST_NAME_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when last name provided is shorter than expected")
    void testCreateUserWithShorterLastName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setLastName("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(LAST_NAME_FIELD, LAST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when last name provided is longer than expected")
    void testCreateUserWithLongerLastName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setLastName("a".repeat(51));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(LAST_NAME_FIELD, LAST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when position provided is null")
    void testCreateUserWithNullPosition() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPosition(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(POSITION_FIELD, POSITION_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when position provided is shorter than expected")
    void testCreateUserWithShorterPosition() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPosition("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(POSITION_FIELD, POSITION_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when position provided is longer than expected")
    void testCreateUserWithLongerPosition() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPosition("a".repeat(51));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(POSITION_FIELD, POSITION_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when is enabled provided is null")
    void testCreateUserWithNullIsEnabled() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setIsEnabled(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(IS_ENABLED_FIELD, IS_ENABLED_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when roles provided are empty")
    void testCreateUserWithNoRoles() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setRoles(Set.of());

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROLES_FIELD, ROLES_NOT_EMPTY));
    }

    private UserDto buildValidUserDto() {
        return UserDto.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .position(POSITION)
                .isEnabled(IS_ENABLED)
                .roles(Set.of(fetchRole()))
                .build();
    }

    private static ResultMatcher[] expectValidationError(String field, String expectedMessage) {
        return new ResultMatcher[]{
                status().isBadRequest(),
                jsonPath("$.status").value("BAD_REQUEST"),
                jsonPath("$.message").value("Validation error"),
                jsonPath("$.fieldErrors[0].field").value(field),
                jsonPath("$.fieldErrors[0].message").value(expectedMessage)
        };
    }

    private UUID fetchRole() {
        return this.roleRepository.findByName("USER")
                .orElseThrow()
                .getUuid();
    }
}