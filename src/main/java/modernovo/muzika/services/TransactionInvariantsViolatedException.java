package modernovo.muzika.services;

public class TransactionInvariantsViolatedException extends RuntimeException {
    public TransactionInvariantsViolatedException(String message) {
        super(message);
    }
}
