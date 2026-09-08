package io.github.snz89.roshambo.security.exception;

public class TokenDeserializationException extends RuntimeException {
    public TokenDeserializationException(String message) {
        super(message);
    }

    public TokenDeserializationException(Throwable cause) {
        super(cause);
    }
}
