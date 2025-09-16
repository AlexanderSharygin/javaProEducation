package pro.java.education.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pro.java.education.exception.handler.ProductServiceResponseErrorHandler;

@Configuration
@EnableConfigurationProperties(RestTemplateConfigurationProperties.class)
@RequiredArgsConstructor
public class AppConfig {

    private final RestTemplateConfigurationProperties restTemplateProperties;
    private final ProductServiceResponseErrorHandler productServiceResponseErrorHandler;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .rootUri(restTemplateProperties.getProductsClient().getUrl())
                .readTimeout(restTemplateProperties.getProductsClient().getReadTimeout())
                .connectTimeout(restTemplateProperties.getProductsClient().getConnectionTimeout())
                .errorHandler(productServiceResponseErrorHandler)
                .build();
    }
}
