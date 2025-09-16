package pro.java.education.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductDto(@NotNull Long id, @NotNull @Positive Long accountNumber, @NotNull Long balance,
                         String category) {
}
