package pro.java.education.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import pro.java.education.exception.model.ErrorResponse;
import pro.java.education.exception.model.ProductServiceException;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class ProductServiceResponseErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
        throw new ProductServiceException("Product service exception: " + errorResponse.description());
    }
}
