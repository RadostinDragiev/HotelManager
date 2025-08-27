package com.hotelmanager.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDetails {

    private HttpStatus status;
    private String message;
}
