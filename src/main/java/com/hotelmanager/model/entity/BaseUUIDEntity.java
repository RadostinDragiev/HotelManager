package com.hotelmanager.model.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class BaseUUIDEntity {

    @Id
    @UuidGenerator
    private UUID uuid;
}
