package pro.java.education.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record NewProductDto(@NotNull @Positive Long accountNumber, @NotNull Long balance,
                            @NotBlank @Size(min = 1, max = 10) String category) {
}
