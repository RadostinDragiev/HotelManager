package com.hotelmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.model.dto.RoomCreationDto;
import com.hotelmanager.model.enums.BedType;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 400 if roomNumber is negative or invalid")
    void testInvalidRoomNumber() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setRoomNumber("-1");

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("roomNumber", "Room number must be positive"));
    }


    @Test
    @DisplayName("Should return 400 if capacity is greater than 9 or non-positive")
    void testInvalidCapacity() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setCapacity(0);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("capacity", "Capacity must be positive"));

        dto.setCapacity(10);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("capacity", "Capacity must be less than 10"));
    }

    @Test
    @DisplayName("Should return 400 if pricePerNight is non-positive")
    void testInvalidPricePerNight() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setPricePerNight(BigDecimal.ZERO);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("pricePerNight", "Price must be greater than 0"));
    }

    @Test
    @DisplayName("Should return 400 if description exceeds 3000 characters")
    void testTooLongDescription() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setDescription("A".repeat(3001));

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("description", "Description must not exceed 3000 characters"));
    }

    @Test
    @DisplayName("Should return 400 if bedTypes is empty")
    void testEmptyBedTypes() throws Exception {
        RoomCreationDto dto = buildValidRoomDto();
        dto.setBedTypes(List.of());

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpectAll(expectValidationError("bedTypes", "At least one bed type must be provided"));
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
}