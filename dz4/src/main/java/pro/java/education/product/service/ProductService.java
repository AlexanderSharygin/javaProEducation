package pro.java.education.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.java.education.exception.model.NotFoundException;
import pro.java.education.product.dto.NewProductDto;
import pro.java.education.product.dto.ProductDto;
import pro.java.education.product.dto.ProductMapper;
import pro.java.education.product.model.Product;
import pro.java.education.product.model.ProductCategory;
import pro.java.education.product.repository.ProductCategoryRepository;
import pro.java.education.product.repository.ProductRepository;
import pro.java.education.user.model.User;
import pro.java.education.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository categoryRepository;

    public void createProduct(NewProductDto newProductDto, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Не найден user c id = " + userId);
        }
        Optional<Product> existedProduct = productRepository.findByAccountNumber(newProductDto.getAccountNumber());
        if (existedProduct.isPresent()) {
            throw new NotFoundException("Product с указанным accountNumber уже существует!");
        }
        Optional<ProductCategory> category = categoryRepository.findByName(newProductDto.getCategory().toUpperCase());
        if (category.isEmpty()) {
            throw new NotFoundException("Указана неверная категория продукта");
        }

        productRepository.save(ProductMapper.toProductFromNewProductDto(newProductDto, user.get(), category.get()));
    }

    public List<ProductDto> getAllProductsByUserId(Long userId) {
        List<Product> products = productRepository.findAllByUser_Id(userId);
        return products.stream().map(ProductMapper::toProductDtoFromProduct).toList();
    }

    public ProductDto getProductsByAccountNumber(Long accountNumber) {
        Optional<Product> product = productRepository.findByAccountNumber(accountNumber);
        if (product.isEmpty()) {
            throw new NotFoundException("Product с указанным accountNumber не найден!");
        }
        return ProductMapper.toProductDtoFromProduct(product.get());
    }
}