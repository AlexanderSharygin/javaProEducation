package pro.java.education.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pro.java.education.exception.model.ConflictException;
import pro.java.education.exception.model.ErrorResponse;
import pro.java.education.exception.model.NotFoundException;


@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse entityIsAlreadyExist(ConflictException exception) {
        log.warn("Entity is already. Message: {}, StackTrace: {}", exception.getMessage(), exception.getStackTrace());

        return new ErrorResponse(exception.getMessage(), "Entity is already exist!",
                HttpStatus.CONFLICT.toString());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityIsNotExist(NotFoundException exception) {
        log.warn("Entity is not found. Message: {}, StackTrace: {}", exception.getMessage(), exception.getStackTrace());

        return new ErrorResponse(exception.getMessage(), "Entity is not found!",
                HttpStatus.NOT_FOUND.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(final Throwable e) {
        log.warn("Unknown error. Message: {}, StackTrace: {}", e.getMessage(), e.getStackTrace());

        return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Something went wrong");
    }
}