package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.PaymentCreationDto;
import com.hotelmanager.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Void> createPayment(@Valid @RequestBody PaymentCreationDto paymentCreationDto,
                                              UriComponentsBuilder uriComponentsBuilder) {
        UUID paymentId = this.paymentService.createPayment(paymentCreationDto);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/payments/{id}")
                        .buildAndExpand(paymentId)
                        .toUri())
                .build();
    }
}
