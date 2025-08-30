package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
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

import static com.hotelmanager.exception.ExceptionMessages.ROOM_NOT_FOUND_ID;
import static com.hotelmanager.validation.ValidationMessages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private static final String PAGINATION_RESULT_PREFIX = ".content[0]";

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

    private static ResultMatcher[] expectGetAllRooms(String prefix, RoomResponseDto room) {
        return new ResultMatcher[]{
                status().isOk(),
                jsonPath("$" + prefix + ".uuid").value(room.getUuid().toString()),
                jsonPath("$" + prefix + ".roomNumber").value(room.getRoomNumber()),
                jsonPath("$" + prefix + ".roomType").value(room.getRoomType().toString()),
                jsonPath("$" + prefix + ".capacity").value(room.getCapacity()),
                jsonPath("$" + prefix + ".bedTypes").isArray(),
                jsonPath("$" + prefix + ".bedTypes[0]").value(room.getBedTypes().getFirst().toString()),
                jsonPath("$" + prefix + ".pricePerNight").value(room.getPricePerNight()),
                jsonPath("$" + prefix + ".description").value(room.getDescription()),
                jsonPath("$" + prefix + ".roomStatus").value(room.getRoomStatus().toString())
        };
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
    @DisplayName("Should return 400 if bedTypes is empty")
    void testCreateRoomWithEmptyBedTypes() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setBedTypes(List.of());

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(BED_TYPES_FIELD, BED_TYPES_MORE_THAN_ONE));
    }

    @Test
    @DisplayName("Should return 400 if description exceeds 3000 characters")
    void testCreateRoomWithTooLongDescription() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setDescription("A".repeat(3001));

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(DESCRIPTION_FIELD, DESCRIPTION_MAX_LENGTH));
    }

    @Test
    @DisplayName("Should return 400 if roomStatus is null")
    void testCreateRoomWithNullRoomStatus() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomStatus(null);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError(ROOM_STATUS_FIELD, ROOM_STATUS_REQUIRED));
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
                .andExpect(jsonPath("$.message").value(ROOM_NOT_FOUND_ID + RANDOM_ID));
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

    @Test
    @DisplayName("Should return 201 and Location header for a valid room")
    void testCreateRoomSuccessful() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/rooms/")));
    }

    @Test
    @DisplayName("Should return 404 when room id not found")
    void testGetRoomByIdWithIdNotFound() throws Exception {
        this.mockMvc.perform(get("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(ROOM_NOT_FOUND_ID + RANDOM_ID));
    }

    @Test
    @DisplayName("Should return 200 and single room")
    void testGetRoomById() throws Exception {
        RoomResponseDto room = this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms/{id}", room.getUuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(expectGetAllRooms("", room));
    }

    @Test
    @DisplayName("Should return 200 and empty result when no rooms with roomType and roomStatus")
    void testGetAllRoomsWithMissingRoomTypeAndRoomStatus() throws Exception {
        this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, RoomType.DOUBLE.toString())
                        .param(ROOM_STATUS_FIELD, RoomStatus.CLEANING.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Should return 200 and single room with roomType and roomStatus")
    void testGetAllRoomsWithExistingRoomTypeAndRoomStatus() throws Exception {
        RoomResponseDto room = this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, RoomType.SINGLE.toString())
                        .param(ROOM_STATUS_FIELD, RoomStatus.AVAILABLE.toString()))
                .andExpectAll(expectGetAllRooms(PAGINATION_RESULT_PREFIX, room));
    }

    @Test
    @DisplayName("Should return 400 with roomType and roomStatus but invalid page")
    void testGetAllRoomsWithExistingRoomTypeAndRoomStatusButInvalidPage() throws Exception {
        int size = 5;
        createMultipleRooms(10);

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, RoomType.SINGLE.toString())
                        .param(ROOM_STATUS_FIELD, RoomStatus.AVAILABLE.toString())
                        .param("page", "5")
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Requested page exceeds total pages. Max page index: 1"));
    }

    @Test
    @DisplayName("Should return 200 and multiple rooms with roomType and roomStatus with multiple pages")
    void testGetAllRoomsWithExistingRoomTypeAndRoomStatusMultiplePages() throws Exception {
        int size = 5;
        int page = 1;
        createMultipleRooms(10);

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, RoomType.SINGLE.toString())
                        .param(ROOM_STATUS_FIELD, RoomStatus.AVAILABLE.toString())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(page))
                .andExpect(jsonPath("$.size").value(size));
    }

    @Test
    @DisplayName("Should return 200 and empty result when no rooms with roomType")
    void testGetAllRoomsWithMissingRoomType() throws Exception {
        this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, RoomType.DOUBLE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Should return 400 with roomType but invalid page")
    void testGetAllRoomsWithExistingRoomTypeButInvalidPage() throws Exception {
        int size = 5;
        createMultipleRooms(10);

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, RoomType.SINGLE.toString())
                        .param("page", "5")
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Requested page exceeds total pages. Max page index: 1"));
    }

    @Test
    @DisplayName("Should return 200 with roomType and multiple pages")
    void testGetAllRoomsWithExistingRoomTypeMultiplePages() throws Exception {
        int size = 5;
        int page = 1;
        createMultipleRooms(10);

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, RoomType.SINGLE.toString())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(page))
                .andExpect(jsonPath("$.size").value(size));
    }

    @Test
    @DisplayName("Should return 200 and single room with roomType")
    void testGetAllRoomsWithExistingRoomType() throws Exception {
        RoomResponseDto room = this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, RoomType.SINGLE.toString()))
                .andExpectAll(expectGetAllRooms(PAGINATION_RESULT_PREFIX, room));
    }

    @Test
    @DisplayName("Should return 200 and empty result when no rooms with roomStatus")
    void testGetAllRoomsWithMissingRoomStatus() throws Exception {
        this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_STATUS_FIELD, RoomStatus.CLEANING.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Should return 400 with roomStatus but invalid page")
    void testGetAllRoomsWithExistingStatusTypeButInvalidPage() throws Exception {
        int size = 5;
        createMultipleRooms(10);

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_STATUS_FIELD, RoomStatus.AVAILABLE.toString())
                        .param("page", "5")
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Requested page exceeds total pages. Max page index: 1"));
    }

    @Test
    @DisplayName("Should return 200 with roomStatus and multiple pages")
    void testGetAllRoomsWithExistingStatusTypeMultiplePages() throws Exception {
        int size = 5;
        int page = 1;
        createMultipleRooms(10);

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_STATUS_FIELD, RoomStatus.AVAILABLE.toString())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(page))
                .andExpect(jsonPath("$.size").value(size));
    }

    @Test
    @DisplayName("Should return 200 and single room with roomStatus")
    void testGetAllRoomsWithExistingRoomStatus() throws Exception {
        RoomResponseDto room = this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_STATUS_FIELD, RoomStatus.AVAILABLE.toString()))
                .andExpectAll(expectGetAllRooms(PAGINATION_RESULT_PREFIX, room));
    }

    @Test
    @DisplayName("Should return 400 without params but invalid page")
    void testGetAllRoomsWithoutParamsButInvalidPage() throws Exception {
        int size = 5;
        createMultipleRooms(10);

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "5")
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Requested page exceeds total pages. Max page index: 1"));
    }

    @Test
    @DisplayName("Should return 200 without params and multiple pages")
    void testGetAllRoomsWithoutParamsMultiplePages() throws Exception {
        int size = 5;
        int page = 1;
        createMultipleRooms(10);

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(page))
                .andExpect(jsonPath("$.size").value(size));
    }

    @Test
    @DisplayName("Should return 200 and pagination empty result")
    void testGetAllRoomsWithoutParamsResultEmpty() throws Exception {
        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
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

    @Test
    @DisplayName("Should return 200 and pagination result with one record")
    void testGetAllRoomsWithoutParams() throws Exception {
        RoomResponseDto room = this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(expectGetAllRooms(PAGINATION_RESULT_PREFIX, room));
    }

    @Test
    @DisplayName("Should return 404 when delete room by missing id")
    void testDeleteRoomByIdWithInvalidId() throws Exception {
        this.mockMvc.perform(delete("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value(ROOM_NOT_FOUND_ID + RANDOM_ID));
    }

    @Test
    @DisplayName("Should return 200 when delete room by valid id")
    void testDeleteRoomById() throws Exception {
        RoomResponseDto room = this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(delete("/rooms/{id}", room.getUuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void createMultipleRooms(int count) {
        int baseRoomNumber = Integer.parseInt(buildValidRoomDto().getRoomNumber());

        for (int i = 0; i < count; i++) {
            RoomCreationDto room = buildValidRoomDto();
            room.setRoomNumber(String.valueOf(baseRoomNumber + i));
            roomService.createRoom(room);
        }
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