package com.hotelmanager.model.dto;

public record RoomTypeAvailability(
        String roomType,
        long totalRooms,
        long bookedRooms,
        long availableRooms
) {
}
