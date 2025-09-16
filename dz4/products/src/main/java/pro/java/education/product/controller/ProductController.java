package pro.java.education.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.java.education.product.dto.NewProductDto;
import pro.java.education.product.dto.PaymentRequestDto;
import pro.java.education.product.dto.ProductDto;
import pro.java.education.product.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "V1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable("userId") Long userId,
                       @RequestBody @Valid NewProductDto productDto) {
        productService.createProduct(productDto, userId);
    }

    @GetMapping("/account/{number}")
    public ProductDto getProductByAccountNumber(@PathVariable("number") Long number) {
        return productService.getProductsByAccountNumber(number);
    }

    @GetMapping("/user/{id}")
    public List<ProductDto> getProductsByUserId(@PathVariable("id") Long id) {
        return productService.getAllProductsByUserId(id);
    }

    @PostMapping("/payment")
    public ResponseEntity<UUID> performPayment(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(productService.performPayment(paymentRequestDto));
    }
}