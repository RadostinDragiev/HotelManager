package com.hotelmanager.repository;

import com.hotelmanager.model.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, UUID> {

    Optional<RoomType> getByName(String name);

    boolean existsByName(String name);
}
