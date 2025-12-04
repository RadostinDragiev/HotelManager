package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.model.dto.request.UserDto;
import com.hotelmanager.repository.RoleRepository;
import com.hotelmanager.repository.UserRepository;
import com.hotelmanager.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.hotelmanager.exception.ExceptionMessages.*;
import static com.hotelmanager.testutil.ErrorResultMatchers.exception;
import static com.hotelmanager.testutil.ErrorResultMatchers.validationError;
import static com.hotelmanager.validation.ValidationMessages.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @ParameterizedTest
    @MethodSource("authorizedManagementRoles")
    @DisplayName("Should return 200 when creating user with valid data")
    void testCreateUserWithValidData(String role) throws Exception {
        UserDto dto = buildValidUserDto();

        this.mockMvc.perform(post("/users")
                        .with(user("testUser").roles(role))
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
                .andExpectAll(exception("BAD_REQUEST", ROLE_NOT_FOUND));
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
                .andExpectAll(exception("NOT_FOUND", NO_USER_FOUND_BY_USERNAME));
    }

    @Test
    @DisplayName("Should return 400 when username provided already exists")
    void testCreateUserWithAlreadyExistingUsername() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setUsername("testUser");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(USERNAME_FIELD, UNIQUE_USERNAME));
    }

    @Test
    @DisplayName("Should return 400 when username provided is null")
    void testCreateUserWithNullUsername() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setUsername(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(USERNAME_FIELD, USERNAME_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when username provided is shorter than expected")
    void testCreateUserWithShorterUsername() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setUsername("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(USERNAME_FIELD, USERNAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when username provided is longer than expected")
    void testCreateUserWithLongerUsername() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setUsername("a".repeat(21));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(USERNAME_FIELD, USERNAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when password provided is null")
    void testCreateUserWithNullPassword() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPassword(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(PASSWORD_FIELD, PASSWORD_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when password provided is shorter than expected")
    void testCreateUserWithShorterPassword() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPassword("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(PASSWORD_FIELD, PASSWORD_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when password provided is longer than expected")
    void testCreateUserWithLongerPassword() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPassword("a".repeat(21));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(PASSWORD_FIELD, PASSWORD_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when email provided is null")
    void testCreateUserWithNullEmail() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setEmail(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(EMAIL_FIELD, EMAIL_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when email provided is invalid")
    void testCreateUserWithInvalidEmail() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setEmail("invalid.com");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(EMAIL_FIELD, VALID_EMAIL));
    }

    @Test
    @DisplayName("Should return 400 when first name provided is null")
    void testCreateUserWithNullFirstName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setFirstName(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(FIRST_NAME_FIELD, FIRST_NAME_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when username first name is shorter than expected")
    void testCreateUserWithShorterFirstName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setFirstName("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(FIRST_NAME_FIELD, FIRST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when first name provided is longer than expected")
    void testCreateUserWithLongerFirstName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setFirstName("a".repeat(51));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(FIRST_NAME_FIELD, FIRST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when last name provided is null")
    void testCreateUserWithNullLastName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setLastName(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(LAST_NAME_FIELD, LAST_NAME_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when last name provided is shorter than expected")
    void testCreateUserWithShorterLastName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setLastName("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(LAST_NAME_FIELD, LAST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when last name provided is longer than expected")
    void testCreateUserWithLongerLastName() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setLastName("a".repeat(51));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(LAST_NAME_FIELD, LAST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when position provided is null")
    void testCreateUserWithNullPosition() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPosition(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(POSITION_FIELD, POSITION_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when position provided is shorter than expected")
    void testCreateUserWithShorterPosition() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPosition("a");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(POSITION_FIELD, POSITION_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when position provided is longer than expected")
    void testCreateUserWithLongerPosition() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setPosition("a".repeat(51));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(POSITION_FIELD, POSITION_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when is enabled provided is null")
    void testCreateUserWithNullIsEnabled() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setIsEnabled(null);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(IS_ENABLED_FIELD, IS_ENABLED_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when roles provided are empty")
    void testCreateUserWithNoRoles() throws Exception {
        UserDto dto = buildValidUserDto();
        dto.setRoles(Set.of());

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROLES_FIELD, ROLES_NOT_EMPTY));
    }

    @ParameterizedTest
    @MethodSource("unauthorizedManagementRoles")
    @DisplayName("Should return 401 when deactivating user with unauthorized user")
    void testCreateUserWithUnauthorizedUser(String role) throws Exception {
        UserDto dto = buildValidUserDto();

        this.mockMvc.perform(post("/users")
                        .with(user("unauthorizedUser").roles(role))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpectAll(exception("UNAUTHORIZED", USER_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Should return 200 when get all users with existing users")
    void testGetAllUsersReturnRecords() throws Exception {
        this.mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isNotEmpty())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.records[0].uuid").isNotEmpty())
                .andExpect(jsonPath("$.records[0].firstName").value("Test"))
                .andExpect(jsonPath("$.records[0].lastName").value("User"))
                .andExpect(jsonPath("$.records[0].position").value("TEST_USER"))
                .andExpect(jsonPath("$.records[0].createdDateTime").isNotEmpty())
                .andExpect(jsonPath("$.records[0].enabled").value(true));
    }

    @Test
    @DisplayName("Should return 200 when get all users without existing users")
    void testGetAllUsersWithEmptyTable() throws Exception {
        this.userRepository.deleteAll();

        this.mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records").isEmpty())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    @DisplayName("Should return 200 when get user by id find a user")
    void testGetUserByIdSuccessfully() throws Exception {
        UUID userId = fetchUserId();

        this.mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("testUser@example.com"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles").isNotEmpty())
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.createdDateTime").isNotEmpty())
                .andExpect(jsonPath("$.lastLoginDateTime").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 400 when get user by invalid id")
    void testGetUserByIdWithInvalidId() throws Exception {
        UUID userId = UUID.randomUUID();

        this.mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpectAll(exception("BAD_REQUEST", NO_USER_FOUND_BY_ID));
    }

    @ParameterizedTest
    @MethodSource("authorizedManagementRoles")
    @DisplayName("Should return 200 when activating user with authorized user")
    void testActivateUserWithAuthorizedUser(String role) throws Exception {
        UserDto userDto = buildValidUserDto();
        userDto.setIsEnabled(false);
        UUID userId = this.userService.createUser(userDto);

        this.mockMvc.perform(post("/users/{id}/activate", userId)
                        .with(user("authorizedUser").roles(role))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("unauthorizedManagementRoles")
    @DisplayName("Should return 401 when activating user with unauthorized user")
    void testActivateUserWithUnauthorizedUser(String role) throws Exception {
        UserDto userDto = buildValidUserDto();
        userDto.setIsEnabled(false);
        UUID userId = this.userService.createUser(userDto);

        this.mockMvc.perform(post("/users/{id}/activate", userId)
                        .with(user("unauthorizedUser").roles(role))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpectAll(exception("UNAUTHORIZED", USER_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Should return 400 when no user found by id")
    void testActivateUserWithInvalidId() throws Exception {
        this.mockMvc.perform(post("/users/{id}/activate", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpectAll(exception("BAD_REQUEST", NO_USER_FOUND_BY_ID));
    }

    @ParameterizedTest
    @MethodSource("authorizedManagementRoles")
    @DisplayName("Should return 200 when deactivating user")
    void testDeactivateUserSucceed(String role) throws Exception {
        this.mockMvc.perform(delete("/users/{id}", fetchUserId())
                        .with(user("authorizedUser").roles(role))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 400 when no user found by id")
    void testDeactivateUserWithInvalidId() throws Exception {
        this.mockMvc.perform(delete("/users/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpectAll(exception("BAD_REQUEST", NO_USER_FOUND_BY_ID));
    }

    @ParameterizedTest
    @MethodSource("unauthorizedManagementRoles")
    @DisplayName("Should return 401 when deactivating user with unauthorized user")
    void testDeactivateUserWithUnauthorizedUser(String role) throws Exception {
        this.mockMvc.perform(delete("/users/{id}", fetchUserId())
                        .with(user("unauthorizedUser").roles(role))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpectAll(exception("UNAUTHORIZED", USER_UNAUTHORIZED));
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

    private UUID fetchRole() {
        return this.roleRepository.findByName("USER")
                .orElseThrow()
                .getUuid();
    }

    private UUID fetchUserId() {
        return this.userRepository.findByUsername("testUser")
                .orElseThrow()
                .getUuid();
    }

    private static List<String> authorizedManagementRoles() {
        return List.of("MANAGER", "ADMINISTRATOR");
    }

    private static List<String> unauthorizedManagementRoles() {
        return List.of("USER", "HOUSEKEEPING", "RECEPTIONIST");
    }
}