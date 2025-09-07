package pro.java.education.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pro.java.education.product.dto.NewProductDto;
import pro.java.education.product.dto.ProductDto;
import pro.java.education.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
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
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductByAccountNumber(@PathVariable("number") Long number) {
        return productService.getProductsByAccountNumber(number);
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getUsers(@PathVariable("id") Long id) {
        return productService.getAllProductsByUserId(id);
    }
}