package com.hotelmanager.web;

import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.repository.RoomTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.hotelmanager.exception.ExceptionMessages.ROOM_TYPE_EXISTS;
import static com.hotelmanager.testutil.ErrorResultMatchers.exception;
import static com.hotelmanager.testutil.ErrorResultMatchers.validationError;
import static com.hotelmanager.validation.ValidationMessages.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(scripts = "/db/room_types.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@WithMockUser(username = "testUser", roles = {"MANAGER"})
@SpringBootTest
@AutoConfigureMockMvc
class RoomTypeControllerTest extends IntegrationBaseTest {

    private static final String NAME_FIELD = "name";
    private static final String CAPACITY_FIELD = "capacity";
    private static final String PRICE_PER_NIGHT_FIELD = "basePricePerNight";
    private static final String DESCRIPTION_FIELD = "description";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Test
    @DisplayName("Should return 200 when room type is created")
    void testCreateRoomTypeSuccessfully() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "APARTMENT_DELUXE_DOUBLE")
                        .param("basePricePerNight", "100")
                        .param("capacity", "3")
                        .param("description", "Short description"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Should return 400 when room type name already exists")
    void testCreateRoomTypeWithExistingName() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "DELUXE_DOUBLE_APARTMENT")
                        .param("basePricePerNight", "100")
                        .param("capacity", "3")
                        .param("description", "Short description"))
                .andExpect(status().isBadRequest())
                .andExpectAll(exception("BAD_REQUEST", ROOM_TYPE_EXISTS));
    }

    @Test
    @DisplayName("Should return 400 when name is null")
    void testCreateRoomTypeWithNullName() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("basePricePerNight", "100")
                        .param("capacity", "3")
                        .param("description", "Short description"))
                .andExpectAll(validationError(NAME_FIELD, ROOM_TYPE_NAME_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when name is too short")
    void testCreateRoomTypeWithShorterName() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "DELU")
                        .param("basePricePerNight", "100")
                        .param("capacity", "3")
                        .param("description", "Short description"))
                .andExpectAll(validationError(NAME_FIELD, ROOM_TYPE_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when name is too long")
    void testCreateRoomTypeWithLongerName() throws Exception {
        MockMultipartFile image = mockImage();
        String longName = "A".repeat(51);

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", longName)
                        .param("basePricePerNight", "100")
                        .param("capacity", "3")
                        .param("description", "Short description"))
                .andExpectAll(validationError(NAME_FIELD, ROOM_TYPE_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when pricePerNight is null")
    void testCreateRoomTypeWithNullPricePerNight() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "DELUXE_DOUBLE_APARTMENT")
                        .param("capacity", "3")
                        .param("description", "Short description"))
                .andExpectAll(validationError(PRICE_PER_NIGHT_FIELD, PRICE_PER_NIGHT_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when pricePerNight is non-positive")
    void testCreateRoomTypeWithZeroPricePerNight() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "DELUXE_DOUBLE_APARTMENT")
                        .param("basePricePerNight", "-1")
                        .param("capacity", "3")
                        .param("description", "Short description"))
                .andExpectAll(validationError(PRICE_PER_NIGHT_FIELD, PRICE_PER_NIGHT_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when pricePerNight is too long")
    void testCreateRoomTypeWithTooLongPricePerNight() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "DELUXE_DOUBLE_APARTMENT")
                        .param("basePricePerNight", "12345678912.123")
                        .param("capacity", "3")
                        .param("description", "Short description"))
                .andExpectAll(validationError(PRICE_PER_NIGHT_FIELD, PRICE_PER_NIGHT_MONETARY));
    }

    @Test
    @DisplayName("Should return 400 when capacity is null")
    void testUpdateRoomTypeWithNullCapacity() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "DELUXE_DOUBLE_APARTMENT")
                        .param("basePricePerNight", "120")
                        .param("description", "Short description"))
                .andExpectAll(validationError(CAPACITY_FIELD, CAPACITY_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when capacity is negative")
    void testUpdateRoomTypeWithNegativeCapacity() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "DELUXE_DOUBLE_APARTMENT")
                        .param("basePricePerNight", "120")
                        .param("capacity", "-1")
                        .param("description", "Short description"))
                .andExpectAll(validationError(CAPACITY_FIELD, CAPACITY_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when capacity is more than 10")
    void testUpdateRoomTypeWithTooBigCapacity() throws Exception {
        MockMultipartFile image = mockImage();

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "DELUXE_DOUBLE_APARTMENT")
                        .param("basePricePerNight", "120")
                        .param("capacity", "10")
                        .param("description", "Short description"))
                .andExpectAll(validationError(CAPACITY_FIELD, CAPACITY_MAX));
    }

    @Test
    @DisplayName("Should return 400 when description exceeds 3000 characters")
    void testCreateRoomTypeWithTooLongDescription() throws Exception {
        MockMultipartFile image = mockImage();
        String longDescription = "A".repeat(3001);

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/room-type")
                        .file(image)
                        .param("name", "DELUXE_DOUBLE_APARTMENT")
                        .param("basePricePerNight", "120")
                        .param("capacity", "3")
                        .param("description", longDescription))
                .andExpectAll(validationError(DESCRIPTION_FIELD, DESCRIPTION_MAX_LENGTH));
    }

    @Test
    @DisplayName("Should return 200 when get all room types")
    void testGetAllRoomTypes() throws Exception {
        this.mockMvc.perform(get("/room-type")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }

    @Test
    @DisplayName("Should return 200 when get all room types return empty list")
    void testGetAllEmptyResponse() throws Exception {
        this.roomTypeRepository.deleteAll();

        this.mockMvc.perform(get("/room-type")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private MockMultipartFile mockImage() {
        return new MockMultipartFile(
                "images",
                "test.jpg",
                "image/jpeg",
                "dummy".getBytes()
        );
    }
}