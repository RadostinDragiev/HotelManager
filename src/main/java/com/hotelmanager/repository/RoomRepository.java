package com.hotelmanager.repository;

import com.hotelmanager.model.entity.Room;
import com.hotelmanager.model.enums.RoomStatus;
import com.hotelmanager.model.enums.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    Page<Room> findByRoomTypeAndRoomStatus(RoomType roomType, RoomStatus status, Pageable pageable);

    Page<Room> findByRoomType(RoomType roomType, Pageable pageable);

    Page<Room> findByRoomStatus(RoomStatus status, Pageable pageable);
}
