package pro.java.education.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewProductDto {
    @NotNull
    @Positive
    private Long accountNumber;
    @NotNull
    private Long balance;
    @NotBlank
    @Size(min = 1, max = 10)
    private String category;
}
