package io.github.snz89.roshambo.security.jwt.serialization;

public interface TokenSerializer<T> {
    String serialize(T token);
}
