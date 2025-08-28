package com.hotelmanager.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationConstraintErrorResponse {

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private List<FieldValidationError> fieldErrors = new ArrayList<>();

    public ValidationConstraintErrorResponse(HttpStatus status) {
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public void addFieldError(String field, String errorMessage) {
        this.fieldErrors.add(new FieldValidationError(field, errorMessage));
    }
}
