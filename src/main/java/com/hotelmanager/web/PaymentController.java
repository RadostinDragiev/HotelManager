package com.hotelmanager.web;

import com.hotelmanager.model.dto.request.PaymentCreationDto;
import com.hotelmanager.model.dto.response.PaymentMenus;
import com.hotelmanager.model.dto.response.PaymentResponseDto;
import com.hotelmanager.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/menus")
    public ResponseEntity<PaymentMenus> getPaymentMenus() {
        PaymentMenus paymentMenus = this.paymentService.getPaymentMenus();

        return ResponseEntity.ok(paymentMenus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable String id) {
        PaymentResponseDto payment = this.paymentService.getPaymentById(id);

        return ResponseEntity.ok(payment);
    }
}
