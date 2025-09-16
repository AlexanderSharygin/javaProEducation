package pro.java.education.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("integrations.clients")
@RequiredArgsConstructor
@Getter
public class RestTemplateConfigurationProperties {
    private final RestTemplateProperties productsClient;
}
