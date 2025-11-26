package com.hotelmanager.specifications;

import com.hotelmanager.model.entity.Room;
import com.hotelmanager.model.enums.RoomStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class RoomSpecifications {

    public static Specification<Room> byRoomType(UUID type) {
        return (root, query, cb) -> cb.equal(root.get("roomType").get("uuid"), type);
    }

    public static Specification<Room> byRoomStatus(RoomStatus status) {
        return (root, query, cb) -> cb.equal(root.get("roomStatus"), status);
    }
}
