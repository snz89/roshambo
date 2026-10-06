package io.github.snz89.roshambo.domain.exception;

public class MissingContextDataException extends RuntimeException {
    public MissingContextDataException(String message) {
        super(message);
    }
}
