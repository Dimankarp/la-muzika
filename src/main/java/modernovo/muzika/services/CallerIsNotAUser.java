package modernovo.muzika.services;

public class CallerIsNotAUser extends Exception {
    public CallerIsNotAUser(String message) {
        super(message);
    }
}
