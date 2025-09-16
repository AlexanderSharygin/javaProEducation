package pro.java.education.payment.sevice;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.java.education.payment.dto.ProductDto;
import pro.java.education.payment.dto.ProductServiceResponseDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate;

    public List<ProductDto> getAllProductsByUserId(Long userId) {
        String url = "/V1/products/user/" + userId;
        ResponseEntity<List<ProductServiceResponseDto>> response;
        response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        return Objects.requireNonNull(response.getBody()).stream().map(ProductServiceResponseDto::toDto).toList();
    }
}
