package pro.java.education.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pro.java.education.user.dto.UserDto;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductDto {
    @NotNull
    private Long id;
    @NotNull
    @Positive
    private Long accountNumber;
    @NotNull
    private Long balance;
    private ProductCategoryDto category;
    private UserDto user;
}
