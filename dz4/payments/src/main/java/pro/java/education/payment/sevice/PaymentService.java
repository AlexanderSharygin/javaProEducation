package pro.java.education.payment.sevice;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.java.education.exception.model.AccountNotFoundException;
import pro.java.education.exception.model.TransactionAmountException;
import pro.java.education.payment.dto.PaymentRequestDto;
import pro.java.education.payment.dto.ProductDto;
import pro.java.education.payment.dto.ProductMapper;
import pro.java.education.payment.dto.ProductServiceResponseDto;
import pro.java.education.payment.model.Product;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate;
    private final ProductMapper productMapper;

    private List<ProductDto> getProductsForUser(Long userId) {
        String url = "/V1/products/user/" + userId;
        ResponseEntity<List<ProductServiceResponseDto>> response;
        response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        return Objects.requireNonNull(response.getBody()).stream().map(ProductServiceResponseDto::toDto).toList();
    }

    public List<ProductDto> getAllProductsByUserId(Long userId) {
        return getProductsForUser(userId);
    }

    public UUID executePayment(PaymentRequestDto paymentRequestDto) {
        Product product = getProductsForUser(paymentRequestDto.userId()).stream()
                .map(productMapper::toProductFromProductDto)
                .filter(k -> k.accountNumber().equals(paymentRequestDto.accountNumber())).findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + paymentRequestDto.accountNumber() +
                        " is not found for user with id = " + paymentRequestDto.userId()));
        if (product.balance() < paymentRequestDto.quantity()) {
            throw new TransactionAmountException("Quantity less than or equal to account balance");
        }

        String url = "/V1/products/account/" + paymentRequestDto.destinationAccountNumber();
        ResponseEntity<ProductServiceResponseDto> response = restTemplate
                .exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {
                        });
        ProductServiceResponseDto productServiceResponseDto = response.getBody();
        if (productServiceResponseDto == null || productServiceResponseDto.toDto().id() == null) {
            throw new AccountNotFoundException("Destination account with number " +
                    paymentRequestDto.destinationAccountNumber() + " is not found");
        }

        String paymentUrl = "/V1/products/payment";

        return restTemplate.postForObject(paymentUrl, paymentRequestDto, UUID.class);
    }
}
