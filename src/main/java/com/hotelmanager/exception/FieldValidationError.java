package com.hotelmanager.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldValidationError {

    private String field;
    private String message;

    public FieldValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
