package pro.java.education.payment.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pro.java.education.payment.model.Product;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class ProductMapper {

    public Product toProductFromProductDto(ProductDto productDto) {
        return new Product(productDto.id(), productDto.accountNumber(), productDto.balance());
    }
}