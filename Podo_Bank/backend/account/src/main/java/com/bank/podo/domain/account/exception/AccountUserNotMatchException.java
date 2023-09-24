package src.main.java.com.bank.podo.domain.account.exception;

public class AccountUserNotMatchException extends RuntimeException {
    public AccountUserNotMatchException(String message) {
        super(message);
    }
}
