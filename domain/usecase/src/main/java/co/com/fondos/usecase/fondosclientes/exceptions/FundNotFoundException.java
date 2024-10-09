package co.com.fondos.usecase.fondosclientes.exceptions;

public class FundNotFoundException extends RuntimeException {
    public FundNotFoundException(String message) {
        super(message);
    }
}
