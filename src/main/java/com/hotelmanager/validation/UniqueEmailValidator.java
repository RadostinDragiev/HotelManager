package com.hotelmanager.validation;

import com.hotelmanager.repository.UserRepository;
import com.hotelmanager.validation.annotation.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String emails, ConstraintValidatorContext constraintValidatorContext) {
        return !this.userRepository.existsByEmail(emails);
    }
}
