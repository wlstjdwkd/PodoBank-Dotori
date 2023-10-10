package auth.src.main.java.com.bank.podo.domain.user.exception;

public class AlreadyUsedUsernameException extends RuntimeException {
    public AlreadyUsedUsernameException(String message) {
        super(message);
    }
}
