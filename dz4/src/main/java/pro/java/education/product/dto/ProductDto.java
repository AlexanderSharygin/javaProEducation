package pro.java.education.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pro.java.education.user.dto.UserDto;

public record ProductDto(@NotNull Long id, @NotNull @Positive Long accountNumber, @NotNull Long balance,
                         ProductCategoryDto category, UserDto user) {
}
