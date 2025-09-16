package pro.java.education.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequestDto(@NotNull Long accountNumber, @NotNull Long userId,
                                @NotNull Long destinationAccountNumber,
                                @Positive Long quantity) {
}
