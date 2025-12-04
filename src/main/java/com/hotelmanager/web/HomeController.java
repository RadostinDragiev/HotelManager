package com.hotelmanager.web;

import com.hotelmanager.model.dto.response.RoomTypesPreview;
import com.hotelmanager.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final RoomTypeService roomTypeService;

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomTypesPreview>> getRoomTypesPreview() {
        return ResponseEntity.ok(this.roomTypeService.getTypesPreview());
    }
}
