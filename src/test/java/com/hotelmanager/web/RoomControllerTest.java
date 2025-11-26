package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.dto.request.RoomUpdateDto;
import com.hotelmanager.model.dto.response.RoomResponseDto;
import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.service.RoomService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.UUID;

import static com.hotelmanager.exception.ExceptionMessages.*;
import static com.hotelmanager.testutil.ErrorResultMatchers.exception;
import static com.hotelmanager.testutil.ErrorResultMatchers.validationError;
import static com.hotelmanager.validation.ValidationMessages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(scripts = "/db/room_types.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@WithMockUser(username = "testUser", roles = {"MANAGER"})
@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest extends IntegrationBaseTest {

    private static final UUID RANDOM_ID = UUID.randomUUID();
    private static final String ROOM_NUMBER_FIELD = "roomNumber";
    private static final String ROOM_TYPE_FIELD = "roomType";
    private static final String BED_TYPES_FIELD = "bedTypes";
    private static final String ROOM_STATUS_FIELD = "roomStatus";
    private static final String PAGINATION_RESULT_PREFIX = ".records[0]";
    private static final String STANDARD_SINGLE_ROOM_UUID = "fcf4ed3a-00f0-4187-841c-8960dc3b07f5";
    private static final String STANDARD_DOUBLE_ROOM_UUID = "e5a9aead-0b4e-4bbd-9fb1-92b424f55b6c";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomService roomService;

    @Test
    @DisplayName("Should return 200 when room is created")
    void testCreateRoomSuccessfully() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Should return 400 when roomNumber is negative or invalid")
    void testCreateRoomWithInvalidRoomNumber() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomNumber(null);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when roomNumber is negative or invalid")
    void testCreateRoomWithNegativeRoomNumber() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomNumber("-1");

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when roomNumber exceeds 10 digits")
    void testCreateRoomWithTooLongRoomNumber() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomNumber("12345678901");

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_MAX));
    }

    @Test
    @DisplayName("Should return 400 when roomNumber already exists")
    void testCreateRoomWithExistingRoomNumber() throws Exception {
        createMultipleRooms(1);
        RoomCreationDto dto = buildValidRoomDto();

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpectAll(exception("BAD_REQUEST", ROOM_NUMBER_EXISTS.formatted(dto.getRoomNumber())));
    }

    @Test
    @DisplayName("Should return 400 when roomType is null")
    void testCreateRoomWithNullRoomType() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomType(null);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROOM_TYPE_FIELD, ROOM_TYPE_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when bedTypes is null")
    void testCreateRoomWithNullBedType() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setBedTypes(null);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(BED_TYPES_FIELD, BED_TYPES_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when bedTypes is empty")
    void testCreateRoomWithEmptyBedTypes() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setBedTypes(List.of());

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(BED_TYPES_FIELD, BED_TYPES_MORE_THAN_ONE));
    }

    @Test
    @DisplayName("Should return 400 when roomStatus is null")
    void testCreateRoomWithNullRoomStatus() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomStatus(null);

        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROOM_STATUS_FIELD, ROOM_STATUS_REQUIRED));
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
                .andExpectAll(validationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when room number is negative number")
    void testUpdateInvalidRoomNumber() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomNumber("-1");

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when room number is longer than 10 symbols")
    void testUpdateTooLongRoomNumber() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomNumber("12345678901");

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROOM_NUMBER_FIELD, ROOM_NUMBER_MAX));
    }

    @Test
    @DisplayName("Should return 400 when room number already exists")
    void testUpdateRoomNumberWithExisting() throws Exception {
        UUID roomId = this.roomService.createRoom(buildValidRoomDto()).getUuid();

        RoomCreationDto secondRoom = buildValidRoomDto();
        secondRoom.setRoomNumber("102");
        this.roomService.createRoom(secondRoom);

        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomNumber("102");

        this.mockMvc.perform(put("/rooms/{id}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpectAll(exception("BAD_REQUEST", ROOM_NUMBER_EXISTS.formatted(dto.getRoomNumber())));
    }

    @Test
    @DisplayName("Should return 400 when room type is null")
    void testUpdateRoomWithRoomTypeNull() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomType(null);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROOM_TYPE_FIELD, ROOM_TYPE_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when room type is invalid")
    void testUpdateRoomWithInvalidRoomType() throws Exception {
        UUID roomId = this.roomService.createRoom(buildValidRoomDto()).getUuid();
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomType("SINGLE");

        this.mockMvc.perform(put("/rooms/{id}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpectAll(exception("BAD_REQUEST", ROOM_TYPE_NOT_FOUND));
    }

    @Test
    @DisplayName("Should return 400 when bed types is null")
    void testUpdateRoomWithBedTypeNull() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setBedTypes(null);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(BED_TYPES_FIELD, BED_TYPES_REQUIRED));
    }

    @Test
    @DisplayName("Should return 400 when bed types is empty")
    void testUpdateRoomWithEmptyBedType() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setBedTypes(List.of());

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(BED_TYPES_FIELD, BED_TYPES_MORE_THAN_ONE));
    }

    @Test
    @DisplayName("Should return 400 when room status is null")
    void testUpdateRoomWithRoomStatusNull() throws Exception {
        RoomUpdateDto dto = buildValidRoomUpdateDto();
        dto.setRoomStatus(null);

        this.mockMvc.perform(put("/rooms/{id}", RANDOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(validationError(ROOM_STATUS_FIELD, ROOM_STATUS_REQUIRED));
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
                .andExpect(jsonPath("$.roomType").value(dto.getRoomType()))
                .andExpect(jsonPath("$.capacity").isNotEmpty())
                .andExpect(jsonPath("$.bedTypes").isArray())
                .andExpect(jsonPath("$.bedTypes[0]").value(dto.getBedTypes().getFirst().toString()))
                .andExpect(jsonPath("$.pricePerNight").isNotEmpty())
                .andExpect(jsonPath("$.description").isNotEmpty())
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
                        .param(ROOM_TYPE_FIELD, UUID.randomUUID().toString())
                        .param(ROOM_STATUS_FIELD, RoomStatus.CLEANING.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records").isEmpty());
    }

    @Test
    @DisplayName("Should return 200 and single room with roomType and roomStatus")
    void testGetAllRoomsWithExistingRoomTypeAndRoomStatus() throws Exception {
        RoomResponseDto room = this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, STANDARD_DOUBLE_ROOM_UUID)
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
                        .param(ROOM_TYPE_FIELD, STANDARD_DOUBLE_ROOM_UUID)
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
                        .param(ROOM_TYPE_FIELD, STANDARD_DOUBLE_ROOM_UUID)
                        .param(ROOM_STATUS_FIELD, RoomStatus.AVAILABLE.toString())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size));
    }

    @Test
    @DisplayName("Should return 200 and empty result when no rooms with roomType")
    void testGetAllRoomsWithMissingRoomType() throws Exception {
        this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, STANDARD_SINGLE_ROOM_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records").isEmpty());
    }

    @Test
    @DisplayName("Should return 400 with roomType but invalid page")
    void testGetAllRoomsWithExistingRoomTypeButInvalidPage() throws Exception {
        int size = 5;
        createMultipleRooms(10);

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, STANDARD_DOUBLE_ROOM_UUID)
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
                        .param(ROOM_TYPE_FIELD, STANDARD_DOUBLE_ROOM_UUID)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size));
    }

    @Test
    @DisplayName("Should return 200 and single room with roomType")
    void testGetAllRoomsWithExistingRoomType() throws Exception {
        RoomResponseDto room = this.roomService.createRoom(buildValidRoomDto());

        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(ROOM_TYPE_FIELD, STANDARD_DOUBLE_ROOM_UUID))
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
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records").isEmpty());
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
                .andExpect(jsonPath("$.page").value(page))
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
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size));
    }

    @Test
    @DisplayName("Should return 200 and pagination empty result")
    void testGetAllRoomsWithoutParamsResultEmpty() throws Exception {
        this.mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records").isEmpty());
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

    private static ResultMatcher[] expectGetAllRooms(String prefix, RoomResponseDto room) {
        return new ResultMatcher[]{
                status().isOk(),
                jsonPath("$" + prefix + ".uuid").value(room.getUuid().toString()),
                jsonPath("$" + prefix + ".roomNumber").value(room.getRoomNumber()),
                jsonPath("$" + prefix + ".roomType").value(room.getRoomType()),
                jsonPath("$" + prefix + ".capacity").isNotEmpty(),
                jsonPath("$" + prefix + ".bedTypes").isArray(),
                jsonPath("$" + prefix + ".bedTypes[0]").value(room.getBedTypes().getFirst()),
                jsonPath("$" + prefix + ".pricePerNight").isNotEmpty(),
                jsonPath("$" + prefix + ".description").isNotEmpty(),
                jsonPath("$" + prefix + ".roomStatus").value(room.getRoomStatus().toString())
        };
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
                .roomType("STANDARD_DOUBLE_ROOM")
                .bedTypes(List.of(BedType.SINGLE))
                .roomStatus(RoomStatus.AVAILABLE)
                .build();
    }

    private RoomUpdateDto buildValidRoomUpdateDto() {
        return RoomUpdateDto.builder()
                .roomNumber("105")
                .roomType("DELUXE_DOUBLE_APARTMENT")
                .bedTypes(List.of(BedType.DOUBLE, BedType.DOUBLE))
                .roomStatus(RoomStatus.CLEANING)
                .build();
    }
}