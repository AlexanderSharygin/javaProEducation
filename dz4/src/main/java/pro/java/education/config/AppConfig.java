package pro.java.education.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

@Configuration
@PropertySource("classpath:database.properties")
public class AppConfig {
    // Базовая конфигурация
    @Value("${db.url}")
    private String jdbcUrl;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.driver}")
    private String driverClassName;

    @Value("${db.pool.maximumPoolSize}")
    private int maximumPoolSize;

    @Value("${db.pool.minimumIdle}")
    private int minimumIdle;

    @Value("${db.pool.connectionTimeout}")
    private long connectionTimeout;

    @Value("${db.pool.idleTimeout}")
    private long idleTimeout;

    @Value("${db.pool.maxLifetime}")
    private long maxLifetime;

    @Value("${db.pool.name}")
    private String poolName;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        // Basic connection settings
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        // Pool settings
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setPoolName(poolName);

        HikariDataSource dataSource = new HikariDataSource(config);
        executeSchemaScript(dataSource);
        return dataSource;
    }

    private void executeSchemaScript(DataSource dataSource) {
        try {
            ClassPathResource resource = new ClassPathResource("schema.sql");
            String sqlScript = Files.lines(Paths.get(resource.getURI()))
                    .collect(Collectors.joining("\n"));
            String[] sqlStatements = sqlScript.split(";");
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                for (String sql : sqlStatements) {
                    if (!sql.trim().isEmpty()) {
                        statement.execute(sql);
                    }
                }
                System.out.println("Таблица успешно инициализирована");
            }
        } catch (Exception e) {
            System.err.println("Что то пошло не так: " + e.getMessage());
        }
    }
}
