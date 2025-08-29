package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import com.hotelmanager.service.RoomService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.hotelmanager.validation.ValidationMessages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest extends IntegrationBaseTest {

    private static final UUID RANDOM_ID = UUID.randomUUID();
    private static final String ROOM_NUMBER_FIELD = "roomNumber";
    private static final String ROOM_TYPE_FIELD = "roomType";
    private static final String CAPACITY_FIELD = "capacity";
    private static final String BED_TYPES_FIELD = "bedTypes";
    private static final String PRICE_PER_NIGHT_FIELD = "pricePerNight";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String ROOM_STATUS_FIELD = "roomStatus";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomService roomService;

    @Test
    @DisplayName("Should return 400 if roomNumber is negative or invalid")
    void testCreateRoomWithInvalidRoomNumber() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomNumber(null);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 if roomNumber is negative or invalid")
    void testCreateRoomWithNegativeRoomNumber() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomNumber("-1");

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 if roomNumber exceeds 10 digits")
    void testCreateRoomWithTooLongRoomNumber() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomNumber("12345678901");

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_MAX));
    }

    @Test
    @DisplayName("Should return 400 if roomType is null")
    void testCreateRoomWithNullRoomType() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomType(null);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_TYPE_FIELD, ROOM_TYPE_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 if capacity is greater than 9 or non-positive")
    void testCreateRoomWithInvalidCapacity() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setCapacity(0);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(CAPACITY_FIELD, CAPACITY_POSITIVE));

        dto.setCapacity(10);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(CAPACITY_FIELD, CAPACITY_MAX));
    }

    @Test
    @DisplayName("Should return 400 if bedTypes is null")
    void testCreateRoomWithNullBedType() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setBedTypes(null);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(BED_TYPES_FIELD, BED_TYPES_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 if bedTypes is empty")
    void testCreateRoomWithEmptyBedTypes() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setBedTypes(List.of());

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(BED_TYPES_FIELD, BED_TYPES_MORE_THAN_ONE));
    }

    @Test
    @DisplayName("Should return 400 if pricePerNight is null")
    void testCreateRoomWithNullPricePerNight() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setPricePerNight(null);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(PRICE_PER_NIGHT_FIELD, PRICE_PER_NIGHT_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 if pricePerNight is non-positive")
    void testCreateRoomWithZeroPricePerNight() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setPricePerNight(BigDecimal.ZERO);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(PRICE_PER_NIGHT_FIELD, PRICE_PER_NIGHT_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 if pricePerNight is too long")
    void testCreateRoomWithTooLongPricePerNight() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setPricePerNight(BigDecimal.valueOf(12345678912.123));

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(PRICE_PER_NIGHT_FIELD, PRICE_PER_NIGHT_MONETARY));
    }

    @Test
    @DisplayName("Should return 400 if description exceeds 3000 characters")
    void testCreateRoomWithTooLongDescription() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setDescription("A".repeat(3001));

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(DESCRIPTION_FIELD, DESCRIPTION_MAX_LENGTH));
    }

    @Test
    @DisplayName("Should return 400 if roomStatus is null")
    void testCreateRoomWithNullRoomStatus() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomStatus(null);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_STATUS_FIELD, ROOM_STATUS_REQUIRED));
    }

    @Test
    @DisplayName("Should return 201 and Location header for a valid room")
    void testValidRoom() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/rooms/")));
    }

    @Test
    @DisplayName("Should return 404 when invalid room id passed")
    void testUpdateRoomThrowsException() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Invalid room id " + RANDOM_ID));
    }

    @Test
    @DisplayName("Should return 400 when room number is null")
    void testUpdateNullRoomNumber() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomNumber(null);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when room number is negative number")
    void testUpdateInvalidRoomNumber() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomNumber("-1");

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when room number is longer than 10 symbols")
    void testUpdateTooLongRoomNumber() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomNumber("12345678901");

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_MAX));
    }

    @Test
    @DisplayName("Should return 400 when room type is null")
    void testUpdateRoomWithRoomTypeTypeNull() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomType(null);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_TYPE_FIELD, ROOM_TYPE_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when capacity is negative")
    void testUpdateRoomWithNegativeCapacity() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setCapacity(-1);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(CAPACITY_FIELD, CAPACITY_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when capacity is more than 10")
    void testUpdateRoomWithTooBigCapacity() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setCapacity(10);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(CAPACITY_FIELD, CAPACITY_MAX));
    }

    @Test
    @DisplayName("Should return 400 when bed types is null")
    void testUpdateRoomWithBedTypeNull() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setBedTypes(null);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(BED_TYPES_FIELD, BED_TYPES_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when bed types is empty")
    void testUpdateRoomWithEmptyBedType() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setBedTypes(List.of());

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(BED_TYPES_FIELD, BED_TYPES_MORE_THAN_ONE));
    }

    @Test
    @DisplayName("Should return 400 when price per night is null")
    void testUpdateRoomWithPricePerNightNull() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setPricePerNight(null);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(PRICE_PER_NIGHT_FIELD, PRICE_PER_NIGHT_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when price per night is negative")
    void testUpdateRoomWithNegativePricePerNight() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setPricePerNight(BigDecimal.valueOf(-1.2));

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(PRICE_PER_NIGHT_FIELD, PRICE_PER_NIGHT_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when price per night is too long")
    void testUpdateRoomWithTooLongPricePerNight() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setPricePerNight(BigDecimal.valueOf(12345678912.123));

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(PRICE_PER_NIGHT_FIELD, PRICE_PER_NIGHT_MONETARY));
    }

    @Test
    @DisplayName("Should return 400 when description is too long")
    void testUpdateRoomWithTooLongDescription() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setDescription("a".repeat(3001));

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(DESCRIPTION_FIELD, DESCRIPTION_MAX_LENGTH));
    }

    @Test
    @DisplayName("Should return 400 when room status is null")
    void testUpdateRoomWithRoomStatusNull() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomStatus(null);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_STATUS_FIELD, ROOM_STATUS_REQUIRED));
    }

    @Test
    @DisplayName("Should return 200 and updated room for valid updates")
    void testUpdateValidRoom() throws Exception {
        UUID roomId = this.roomService.createRoom(buildValidRoomDto()).getUuid();
        RoomUpdateDto dto = buildValidRoomUpdateDto();

        this.mockMvc.perform(put("/rooms/{id}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(roomId.toString()))
                .andExpect(jsonPath("$.roomNumber").value(dto.getRoomNumber()))
                .andExpect(jsonPath("$.roomType").value(dto.getRoomType().toString()))
                .andExpect(jsonPath("$.capacity").value(dto.getCapacity()))
                .andExpect(jsonPath("$.bedTypes").isArray())
                .andExpect(jsonPath("$.bedTypes[0]").value(dto.getBedTypes().getFirst().toString()))
                .andExpect(jsonPath("$.pricePerNight").value(dto.getPricePerNight()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()))
                .andExpect(jsonPath("$.roomStatus").value(dto.getRoomStatus().toString()));
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

    private RoomCreationDto buildValidRoomDto() {
        return RoomCreationDto.builder()
                .roomNumber("101")
                .roomType(RoomType.SINGLE)
                .capacity(2)
                .bedTypes(List.of(BedType.SINGLE))
                .pricePerNight(BigDecimal.valueOf(120.00))
                .description("Valid room description")
                .roomStatus(RoomStatus.AVAILABLE)
                .build();
    }

    private RoomUpdateDto buildValidRoomUpdateDto() {
        return RoomUpdateDto.builder()
                .roomNumber("105")
                .roomType(RoomType.DOUBLE)
                .capacity(5)
                .bedTypes(List.of(BedType.DOUBLE))
                .pricePerNight(BigDecimal.valueOf(100.00))
                .description("Valid update room description")
                .roomStatus(RoomStatus.CLEANING)
                .build();
    }
}