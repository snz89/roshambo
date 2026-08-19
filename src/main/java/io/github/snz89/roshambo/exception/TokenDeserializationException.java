package io.github.snz89.roshambo.exception;

public class TokenDeserializationException extends RuntimeException {
    public TokenDeserializationException(String message) {
        super(message);
    }

    public TokenDeserializationException(Throwable cause) {
        super(cause);
    }
}
