package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.model.dto.request.ReservationCreationDto;
import com.hotelmanager.model.dto.request.ReservationRoomDto;
import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.hotelmanager.exception.ExceptionMessages.NOT_ENOUGH_ROOMS_AVAILABLE;
import static com.hotelmanager.testutil.ErrorResultMatchers.exception;
import static com.hotelmanager.testutil.ErrorResultMatchers.validationError;
import static com.hotelmanager.validation.ValidationMessages.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/db/room_types.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@WithMockUser(username = "testUser", roles = {"MANAGER"})
@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest extends IntegrationBaseTest {

    private static final String FIRST_NAME_FIELD = "firstName";
    private static final String LAST_NAME_FIELD = "lastName";
    private static final String EMAIL_FIELD = "email";
    private static final String PHONE_FIELD = "phone";
    private static final String GUESTS_COUNT_FIELD = "guestsCount";
    private static final String START_DATE_FIELD = "startDate";
    private static final String END_DATE_FIELD = "endDate";
    private static final String ROOMS_FIELD = "rooms";
    private static final String ROOMS_COUNT_FIELD = "roomsCount";
    private static final String ROOMS_TYPE_NAME_FIELD = "roomTypeName";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        createRoom();
    }

    @Test
    @DisplayName("Should return 201 when reservation is created")
    void testCreateReservationSuccessfully() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Should return 400 when first name provided is null")
    void testCreateReservationWithNullFirstName() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setFirstName(null);

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(FIRST_NAME_FIELD, FIRST_NAME_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when first name provided is shorter than expected")
    void testCreateReservationWithShorterFirstName() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setFirstName("A");

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(FIRST_NAME_FIELD, FIRST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when first name provided is longer than expected")
    void testCreateReservationWithLongerFirstName() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setFirstName("A".repeat(51));

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(FIRST_NAME_FIELD, FIRST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when last name provided is null")
    void testCreateReservationWithNullLastName() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setLastName(null);

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(LAST_NAME_FIELD, LAST_NAME_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when last name provided is shorter than expected")
    void testCreateReservationWithShorterLastName() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setLastName("A");

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(LAST_NAME_FIELD, LAST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when last name provided is longer than expected")
    void testCreateReservationWithLongerLastName() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setLastName("A".repeat(51));

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(LAST_NAME_FIELD, LAST_NAME_SIZE));
    }

    @Test
    @DisplayName("Should return 400 when email provided is null")
    void testCreateReservationWithNullEmail() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setEmail(null);

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(EMAIL_FIELD, EMAIL_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when email provided is invalid")
    void testCreateReservationWithInvalidEmail() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setEmail("notEmail");

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(EMAIL_FIELD, VALID_EMAIL));
    }

    @Test
    @DisplayName("Should return 400 when phone provided is null")
    void testCreateReservationWithNullPhone() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setPhone(null);

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(PHONE_FIELD, PHONE_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when guests count provided is negative")
    void testCreateReservationWithNegativeGuestsCount() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setGuestsCount(-1);

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(GUESTS_COUNT_FIELD, GUESTS_COUNT_ONLY_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when start date provided is null")
    void testCreateReservationWithNullStartDate() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setStartDate(null);

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(START_DATE_FIELD, START_DATE_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when start date provided is in the past")
    void testCreateReservationWithPastStartDate() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setStartDate(LocalDate.now().minusDays(1));

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(START_DATE_FIELD, START_DATE_NOT_PAST));
    }

    @Test
    @DisplayName("Should return 400 when end date provided is null")
    void testCreateReservationWithNullEndDate() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setEndDate(null);

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(END_DATE_FIELD, END_DATE_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when end date provided is in the past")
    void testCreateReservationWithPastEndDate() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setEndDate(LocalDate.now().minusDays(1));

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(END_DATE_FIELD, END_DATE_NOT_PAST));
    }

    @Test
    @DisplayName("Should return 400 when end date provided is before start date")
    void testCreateReservationWithEndDateBeforeStartDate() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setStartDate(LocalDate.now().plusDays(2));
        creationDto.setEndDate(LocalDate.now());

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(START_DATE_FIELD, START_DATE_BEFORE_END_DATE));
    }

    @Test
    @DisplayName("Should return 400 when rooms provided are empty")
    void testCreateReservationWithEmptyRooms() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        creationDto.setRooms(new ArrayList<>());

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(ROOMS_FIELD, ROOMS_LIST_NOT_EMPTY));
    }

    @Test
    @DisplayName("Should return 400 when rooms count provided is negative")
    void testCreateReservationWithNegativeRoomsCount() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        ReservationRoomDto first = creationDto.getRooms().getFirst();
        first.setRoomsCount(-1);
        creationDto.setRooms(List.of(first));

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError("rooms[0]." + ROOMS_COUNT_FIELD, ROOMS_COUNT_ONLY_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when room type name provided is null")
    void testCreateReservationWithNullRoomTypeName() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        ReservationRoomDto first = creationDto.getRooms().getFirst();
        first.setRoomTypeName(null);
        creationDto.setRooms(List.of(first));

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError("rooms[0]." + ROOMS_TYPE_NAME_FIELD, ROOM_TYPE_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when too many rooms count provided")
    void testCreateReservationWithTooManyRoomsCount() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();
        ReservationRoomDto first = creationDto.getRooms().getFirst();
        first.setRoomsCount(10);
        creationDto.setRooms(List.of(first));

        this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpect(status().isBadRequest())
                .andExpectAll(exception("BAD_REQUEST", NOT_ENOUGH_ROOMS_AVAILABLE.formatted("STANDARD_DOUBLE_ROOM")));
    }

    private ReservationCreationDto buildReservationCreationDto() {
        ReservationRoomDto reservationRoomDto = ReservationRoomDto.builder()
                .roomsCount(1)
                .roomTypeName("STANDARD_DOUBLE_ROOM")
                .roomId(null)
                .build();

        return ReservationCreationDto.builder()
                .firstName("John")
                .lastName("Down")
                .email("valid@email.com")
                .phone("+1234567890")
                .guestsCount(2)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .rooms(List.of(reservationRoomDto))
                .build();
    }

    private void createRoom() throws Exception {
        RoomCreationDto standardDoubleRoom = RoomCreationDto.builder()
                .roomNumber("101")
                .roomType("STANDARD_DOUBLE_ROOM")
                .bedTypes(List.of(BedType.DOUBLE))
                .roomStatus(RoomStatus.AVAILABLE)
                .build();

        this.mockMvc.perform(post("/rooms")
                .with(user("testUser").roles("MANAGER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standardDoubleRoom)));
    }
}