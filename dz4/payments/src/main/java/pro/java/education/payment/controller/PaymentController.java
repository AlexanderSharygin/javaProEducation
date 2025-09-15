package pro.java.education.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.java.education.payment.dto.ProductDto;
import pro.java.education.payment.sevice.PaymentService;

import java.util.List;

@RestController
@RequestMapping(path = "V1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/products/{userId}")
    public List<ProductDto> getProductsByUserId(@PathVariable("userId") Long userId) {
        return paymentService.getAllProductsByUserId(userId);
    }


}
