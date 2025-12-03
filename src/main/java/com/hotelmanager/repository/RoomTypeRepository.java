package com.hotelmanager.repository;

import com.hotelmanager.model.dto.RoomTypeAvailability;
import com.hotelmanager.model.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, UUID> {

    @Query(value = """
            SELECT
                rt.name as room_type_names,
                COALESCE(br.total_rooms, 0) AS total_rooms,
                rt.total_reservations_by_room_type as booked_rooms,
                COALESCE(total_rooms - rt.total_reservations_by_room_type, 0) as available_rooms
            FROM
                (
                    SELECT
                        rt.uuid,
                        rt.name,
                        COUNT(r.uuid) AS total_reservations_by_room_type
                    FROM room_types rt
                             LEFT JOIN reservations_room_types rrt
                                       ON rt.uuid = rrt.room_type_id
                             LEFT JOIN reservations r
                                       ON rrt.reservation_id = r.uuid
                                           AND r.is_deleted = FALSE
                                           AND r.reservation_status NOT IN ('CANCELED', 'REJECTED')
                                           AND r.start_date < :endDate
                                           AND r.end_date > :startDate
                    GROUP BY rt.uuid, rt.name
                ) rt
                    LEFT JOIN
                (
                    SELECT
                        r.room_type_id,
                        COUNT(*) AS total_rooms
                    FROM rooms r
                    where r.room_status NOT IN ('UNDER_CONSTRUCTION')
                    GROUP BY r.room_type_id
                ) br
                ON rt.uuid = br.room_type_id;
            """,nativeQuery = true)
    List<RoomTypeAvailability> availableRoomsByType(LocalDate startDate, LocalDate endDate);

    Optional<RoomType> getByName(String name);

    boolean existsByName(String name);
}
