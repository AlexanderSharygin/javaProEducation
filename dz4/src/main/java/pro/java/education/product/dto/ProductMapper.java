package pro.java.education.product.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pro.java.education.product.model.Product;
import pro.java.education.product.model.ProductCategory;
import pro.java.education.user.dto.UserDto;
import pro.java.education.user.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static ProductDto toProductDtoFromProduct(Product product) {
        ProductCategoryDto productCategoryDto = new ProductCategoryDto(product.getProductCategory().getId(),
                product.getProductCategory().getName());
        UserDto userDto = new UserDto(product.getUser().getId(), product.getUser().getName());
        return new ProductDto(product.getId(), product.getAccountNumber(), product.getBalance(),
                productCategoryDto, userDto);
    }

    public static Product toProductFromNewProductDto(NewProductDto productDto, User user, ProductCategory category) {
        Product product = new Product();
        product.setAccountNumber(productDto.getAccountNumber());
        product.setBalance(productDto.getBalance());
        product.setUser(user);
        product.setProductCategory(category);

        return product;
    }
}