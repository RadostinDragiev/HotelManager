package com.hotelmanager.testutil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorResultMatchers {

    public static ResultMatcher[] validationError(String field, String expectedMessage) {
        return new ResultMatcher[]{
                status().isBadRequest(),
                jsonPath("$.status").value("BAD_REQUEST"),
                jsonPath("$.message").value("Validation error"),
                jsonPath("$.fieldErrors[0].field").value(field),
                jsonPath("$.fieldErrors[0].message").value(expectedMessage)
        };
    }

    public static ResultMatcher[] exception(String status, String expectedMessage) {
        return new ResultMatcher[]{
                jsonPath("$.status").value(status),
                jsonPath("$.timestamp").isNotEmpty(),
                jsonPath("$.message").value(expectedMessage)
        };
    }
}
