package src.main.java.com.bank.podo.domain.account.exception;

public class AccountBalanceNotZeroException extends RuntimeException {
    public AccountBalanceNotZeroException(String message) {
        super(message);
    }
}
