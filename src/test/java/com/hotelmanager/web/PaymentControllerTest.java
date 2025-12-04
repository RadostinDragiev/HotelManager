package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.IntegrationBaseTest;
import com.hotelmanager.model.dto.request.PaymentCreationDto;
import com.hotelmanager.model.dto.request.ReservationCreationDto;
import com.hotelmanager.model.dto.request.ReservationRoomDto;
import com.hotelmanager.model.dto.request.RoomCreationDto;
import com.hotelmanager.model.enums.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.hotelmanager.exception.ExceptionMessages.RESERVATION_NOT_FOUND;
import static com.hotelmanager.exception.ExceptionMessages.ROOM_NOT_FOUND_ID;
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
class PaymentControllerTest extends IntegrationBaseTest {

    private static final String AMOUNT_FIELD = "amount";
    private static final String PAYMENT_TYPE_FIELD = "paymentType";
    private static final String REASON_FIELD = "reason";
    private static final String PAYMENT_STATUS_FIELD = "paymentStatus";
    private static final String RESERVATION_ID_FIELD = "reservationId";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 201 when payment is created")
    void testCreatePaymentSuccessfully() throws Exception {
        createRoom();

        String reservation = createReservation();
        PaymentCreationDto creationDto = buildPaymentCreationDto();
        creationDto.setReservationId(reservation);

        this.mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Should return 400 when amount provided is null")
    void testCreatePaymentWithNullAmount() throws Exception {
        PaymentCreationDto creationDto = buildPaymentCreationDto();
        creationDto.setAmount(null);

        this.mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(AMOUNT_FIELD, AMOUNT_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when amount provided is negative")
    void testCreatePaymentWithNegativeAmount() throws Exception {
        PaymentCreationDto creationDto = buildPaymentCreationDto();
        creationDto.setAmount(BigDecimal.valueOf(-1));

        this.mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(AMOUNT_FIELD, AMOUNT_ONLY_POSITIVE));
    }

    @Test
    @DisplayName("Should return 400 when payment type provided is null")
    void testCreatePaymentWithNullPaymentType() throws Exception {
        PaymentCreationDto creationDto = buildPaymentCreationDto();
        creationDto.setPaymentType(null);

        this.mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(PAYMENT_TYPE_FIELD, PAYMENT_TYPE_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when reason provided is null")
    void testCreatePaymentWithNullReason() throws Exception {
        PaymentCreationDto creationDto = buildPaymentCreationDto();
        creationDto.setReason(null);

        this.mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(REASON_FIELD, REASON_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when payment status provided is null")
    void testCreatePaymentWithNullPaymentStatus() throws Exception {
        PaymentCreationDto creationDto = buildPaymentCreationDto();
        creationDto.setPaymentStatus(null);

        this.mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(PAYMENT_STATUS_FIELD, PAYMENT_STATUS_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when reservation id provided is null")
    void testCreatePaymentWithNullReservationId() throws Exception {
        PaymentCreationDto creationDto = buildPaymentCreationDto();
        creationDto.setReservationId(null);

        this.mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(validationError(RESERVATION_ID_FIELD, RESERVATION_ID_NOT_NULL));
    }

    @Test
    @DisplayName("Should return 400 when reservation id is not found")
    void testCreatePaymentWithInvalidReservationId() throws Exception {
        PaymentCreationDto creationDto = buildPaymentCreationDto();
        creationDto.setReservationId("ad2d9c3c-c4d4-11f0-b961-51382206b67f");

        this.mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(exception("BAD_REQUEST", RESERVATION_NOT_FOUND));
    }

    @Test
    @DisplayName("Should return 400 when room id is not found")
    void testCreatePaymentWithInvalidRoomId() throws Exception {
        createRoom();

        PaymentCreationDto creationDto = buildPaymentCreationDto();
        creationDto.setReservationId(createReservation());
        creationDto.setRoomId("ad2d9c3c-c4d4-11f0-b961-51382206b67f");

        this.mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpectAll(exception("NOT_FOUND", ROOM_NOT_FOUND_ID));
    }

    private PaymentCreationDto buildPaymentCreationDto() {
        return PaymentCreationDto.builder()
                .amount(BigDecimal.valueOf(120))
                .paymentType(PaymentType.CARD_PAYMENT)
                .reason(PaymentReason.DEPOSIT)
                .paymentStatus(PaymentStatus.ACCEPTED)
                .reservationId("")
                .build();
    }

    private String createReservation() throws Exception {
        ReservationCreationDto creationDto = buildReservationCreationDto();

        MvcResult result = this.mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        int lastIndex = Objects.requireNonNull(location).lastIndexOf("/") + 1;
        return location.substring(lastIndex);
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
                .reservationPaymentType(ReservationPaymentType.FULL_PREPAY)
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