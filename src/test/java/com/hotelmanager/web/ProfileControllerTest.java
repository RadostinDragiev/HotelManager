package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.model.dto.request.ProfilePasswordDto;
import com.hotelmanager.validation.ValidationMessages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.hotelmanager.exception.ExceptionMessages.NEW_PASSWORDS_DOES_NOT_MATCH;
import static com.hotelmanager.exception.ExceptionMessages.OLD_PASSWORD_DOES_NOT_MATCH;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest extends IntegrationBaseTest {

    private static final String USERNAME = "testUser";
    private static final String EMAIL = "testUser@example.com";
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "User";
    private static final String POSITION = "TEST_USER";
    private static final String OLD_PASSWORD = "testUser123";
    private static final String NEW_PASSWORD = "321resUtset";
    private static final int LOW_EDGE_PASSWORD_SIZE = 7;
    private static final int HIGH_EDGE_PASSWORD_SIZE = 51;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 200 when fetch own profile information")
    void testGetProfile() throws Exception {
        this.mockMvc.perform(get("/profile")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.position").value(POSITION));
    }

    @Test
    @DisplayName("Should return 200 when update password is successful")
    void testUpdatePasswordSuccessful() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();

        this.mockMvc.perform(post("/profile/password")
                        .with(user("testUser"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 400 when old password is different than existing one")
    void testUpdatePasswordWithWrongOldPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setOldPassword(OLD_PASSWORD + "1");

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectExceptionError(OLD_PASSWORD_DOES_NOT_MATCH));
    }

    @Test
    @DisplayName("Should return 400 when new password is different than confirm new password")
    void testUpdatePasswordWithWrongNewPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setNewPassword(NEW_PASSWORD + "1");

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectExceptionError(NEW_PASSWORDS_DOES_NOT_MATCH));
    }

    @Test
    @DisplayName("Should return 400 when confirm new password is different than new password")
    void testUpdatePasswordWithWrongConfirmNewPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setConfirmNewPassword(NEW_PASSWORD + "1");

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectExceptionError(NEW_PASSWORDS_DOES_NOT_MATCH));
    }

    @Test
    @DisplayName("Should return 400 when old password is null")
    void testUpdatePasswordWithNullOldPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setOldPassword(null);

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("oldPassword"));
    }

    @Test
    @DisplayName("Should return 400 when old password provided is shorter than expected")
    void testUpdatePasswordWithShorterOldPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setOldPassword("a".repeat(LOW_EDGE_PASSWORD_SIZE));

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("oldPassword"));
    }

    @Test
    @DisplayName("Should return 400 when old password provided is longer than expected")
    void testUpdatePasswordWithLongerOldPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setOldPassword("a".repeat(HIGH_EDGE_PASSWORD_SIZE));

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("oldPassword"));
    }

    @Test
    @DisplayName("Should return 400 when new password is null")
    void testUpdatePasswordWithNullNewPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setNewPassword(null);

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("newPassword"));
    }

    @Test
    @DisplayName("Should return 400 when new password provided is shorter than expected")
    void testUpdatePasswordWithShorterNewPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setNewPassword("a".repeat(LOW_EDGE_PASSWORD_SIZE));

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("newPassword"));
    }

    @Test
    @DisplayName("Should return 400 when new password provided is longer than expected")
    void testUpdatePasswordWithLongerNewPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setNewPassword("a".repeat(HIGH_EDGE_PASSWORD_SIZE));

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("newPassword"));
    }

    @Test
    @DisplayName("Should return 400 when confirm new password is null")
    void testUpdatePasswordWithNullConfirmNewPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setConfirmNewPassword(null);

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("confirmNewPassword"));
    }

    @Test
    @DisplayName("Should return 400 when confirm new password provided is shorter than expected")
    void testUpdatePasswordWithShorterConfirmNewPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setConfirmNewPassword("a".repeat(LOW_EDGE_PASSWORD_SIZE));

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("confirmNewPassword"));
    }

    @Test
    @DisplayName("Should return 400 when confirm new password provided is longer than expected")
    void testUpdatePasswordWithLongerConfirmNewPassword() throws Exception {
        ProfilePasswordDto dto = buildProfilePasswordDto();
        dto.setConfirmNewPassword("a".repeat(HIGH_EDGE_PASSWORD_SIZE));

        this.mockMvc.perform(post("/profile/password")
                        .with(user(USERNAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("confirmNewPassword"));
    }

    private static ResultMatcher[] expectExceptionError(String expectedMessage) {
        return new ResultMatcher[]{
                status().isBadRequest(),
                jsonPath("$.status").value("BAD_REQUEST"),
                jsonPath("$.timestamp").isNotEmpty(),
                jsonPath("$.message").value(expectedMessage)
        };
    }

    private static ResultMatcher[] expectValidationError(String field) {
        return new ResultMatcher[]{
                status().isBadRequest(),
                jsonPath("$.status").value("BAD_REQUEST"),
                jsonPath("$.message").value("Validation error"),
                jsonPath("$.fieldErrors[0].field").value(field),
                jsonPath("$.fieldErrors[0].message").value(ValidationMessages.PASSWORD_SIZE)
        };
    }

    private ProfilePasswordDto buildProfilePasswordDto() {
        return ProfilePasswordDto.builder()
                .oldPassword(OLD_PASSWORD)
                .newPassword(NEW_PASSWORD)
                .confirmNewPassword(NEW_PASSWORD)
                .build();
    }
}