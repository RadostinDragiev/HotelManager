package com.hotelmanager.validation;

import com.hotelmanager.validation.annotation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startFieldName;
    private String endFieldName;
    private String message;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startFieldName = constraintAnnotation.start();
        this.endFieldName = constraintAnnotation.end();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            LocalDate start = getFieldValue(value, startFieldName);
            LocalDate end = getFieldValue(value, endFieldName);

            if (start == null || end == null) {
                return true;
            }

            boolean valid = start.isBefore(end);

            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(startFieldName)
                        .addConstraintViolation();
            }

            return valid;

        } catch (Exception e) {
            throw new RuntimeException("Invalid @ValidDateRange configuration", e);
        }
    }

    private LocalDate getFieldValue(Object obj, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {

        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (LocalDate) field.get(obj);
    }
}
