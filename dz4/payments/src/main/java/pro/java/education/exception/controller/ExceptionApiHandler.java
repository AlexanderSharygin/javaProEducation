package pro.java.education.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pro.java.education.exception.model.AccountNotFoundException;
import pro.java.education.exception.model.ErrorResponse;
import pro.java.education.exception.model.ProductServiceException;
import pro.java.education.exception.model.TransactionAmountException;

import java.nio.channels.ClosedChannelException;

@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {

    @ExceptionHandler(ProductServiceException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse productServiceError(ProductServiceException exception) {
        log.warn("Something went wrong with product service. Message: {}, StackTrace: {}", exception.getMessage(),
                exception.getStackTrace());
        return new ErrorResponse(exception.getMessage(), HttpStatus.BAD_GATEWAY.toString(),
                "Something went wrong with products service");
    }

    @ExceptionHandler(ClosedChannelException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse connectionError(ClosedChannelException exception) {
        log.warn("Can't connect to products service. Message: {}, StackTrace: {}", exception.getMessage(),
                exception.getStackTrace());
        return new ErrorResponse("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE.toString(),
                "Can't connect to products service");
    }

    @ExceptionHandler(TransactionAmountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse entityIsAlreadyExist(TransactionAmountException exception) {
        log.warn("Entity is already. Message: {}, StackTrace: {}", exception.getMessage(), exception.getStackTrace());

        return new ErrorResponse(exception.getMessage(), HttpStatus.CONFLICT.toString(),
                "Wrong amount value!");
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityIsNotExist(AccountNotFoundException exception) {
        log.warn("Entity is not found. Message: {}, StackTrace: {}", exception.getMessage(), exception.getStackTrace());

        return new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.toString(),
                "Account is not found!");
    }
}