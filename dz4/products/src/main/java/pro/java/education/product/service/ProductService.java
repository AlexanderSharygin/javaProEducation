package pro.java.education.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.java.education.exception.model.ConflictException;
import pro.java.education.exception.model.InvalidPaymentDataException;
import pro.java.education.exception.model.NotFoundException;
import pro.java.education.product.dto.NewProductDto;
import pro.java.education.product.dto.PaymentRequestDto;
import pro.java.education.product.dto.ProductDto;
import pro.java.education.product.dto.ProductMapper;
import pro.java.education.product.model.Product;
import pro.java.education.product.model.ProductCategory;
import pro.java.education.product.model.Transaction;
import pro.java.education.product.repository.ProductCategoryRepository;
import pro.java.education.product.repository.ProductRepository;
import pro.java.education.product.repository.TransactionRepository;
import pro.java.education.user.model.User;
import pro.java.education.user.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final ProductMapper productMapper;

    public void createProduct(NewProductDto newProductDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден user c id = " + userId));
        productRepository.findByAccountNumber(newProductDto.accountNumber())
                .ifPresent(__ -> {
                    throw new ConflictException("Product с указанным accountNumber уже существует!");
                });
        ProductCategory category = categoryRepository.findByName(newProductDto.category().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Указана неверная категория продукта"));

        productRepository.save(productMapper.toProductFromNewProductDto(newProductDto, user, category));
    }

    public List<ProductDto> getAllProductsByUserId(Long userId) {
        List<Product> products = productRepository.findAllByUser_Id(userId);
        return products.stream().map(productMapper::toProductDtoFromProduct).toList();
    }

    public ProductDto getProductsByAccountNumber(Long accountNumber) {
        Product product = productRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Product с указанным accountNumber не найден!"));
        return productMapper.toProductDtoFromProduct(product);
    }

    public UUID performPayment(PaymentRequestDto paymentRequestDto) {
        Product product = productRepository.findByAccountNumber(paymentRequestDto.accountNumber())
                .orElseThrow(() -> new InvalidPaymentDataException("Wrong account number!"));
        Product destinationProduct = productRepository.findByAccountNumber(paymentRequestDto.destinationAccountNumber())
                .orElseThrow(() -> new InvalidPaymentDataException("Wrong destination account number!"));

        product.setBalance(product.getBalance() - paymentRequestDto.quantity());
        destinationProduct.setBalance(product.getBalance() + paymentRequestDto.quantity());
        productRepository.saveAll(List.of(destinationProduct, product));

        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction();
        transaction.setAccount(paymentRequestDto.accountNumber());
        transaction.setAmount(paymentRequestDto.quantity());
        transaction.setDestinationAccount(paymentRequestDto.destinationAccountNumber());
        transaction.setTransactionId(transactionId);
        transaction.setUser(product.getUser());
        transactionRepository.save(transaction);

        return transactionId;
    }
}