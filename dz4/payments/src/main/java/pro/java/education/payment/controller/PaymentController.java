package pro.java.education.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.java.education.payment.dto.PaymentRequestDto;
import pro.java.education.payment.dto.ProductDto;
import pro.java.education.payment.sevice.PaymentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "V1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/products/{userId}")
    public List<ProductDto> getProductsByUserId(@PathVariable("userId") Long userId) {
        return paymentService.getAllProductsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<UUID> executePayment(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(paymentService.executePayment(paymentRequestDto));
    }
}
