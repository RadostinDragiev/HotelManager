package com.hotelmanager.web;

import com.hotelmanager.model.dto.RoomTypeAvailability;
import com.hotelmanager.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final RoomTypeService roomTypeService;

    @GetMapping
    public ResponseEntity<Map<String, RoomTypeAvailability>> getAvailability() {
        LocalDate now = LocalDate.now();
        Map<String, RoomTypeAvailability> stringRoomTypeAvailabilityMap = this.roomTypeService.roomTypeAvailabilitiesMap(now, now.plusMonths(1));

        return ResponseEntity.ok(stringRoomTypeAvailabilityMap);
    }
}
