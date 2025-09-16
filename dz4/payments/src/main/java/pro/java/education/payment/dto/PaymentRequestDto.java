package pro.java.education.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequestDto(@NotNull Long accountNumber, @NotNull Long destinationAccountNumber,
                                @NotNull Long userId, @Positive Long quantity) {
}
