package pro.java.education.payment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ProductServiceResponseDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("accountNumber")
    private Long accountNumber;
    @JsonProperty("balance")
    private Long balance;
    @JsonProperty("category")
    private Category category;

    @JsonCreator
    public ProductDto toDto() {
        return new ProductDto(
                this.id,
                this.accountNumber,
                this.balance,
                this.category != null ? this.category.name : null
        );
    }

    public static class Category {
        public String name;
    }
}
