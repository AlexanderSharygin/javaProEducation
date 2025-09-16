package pro.java.education.exception.model;

public class TransactionAmountException extends RuntimeException {
    public TransactionAmountException(String message) {
        super(message);
    }
}