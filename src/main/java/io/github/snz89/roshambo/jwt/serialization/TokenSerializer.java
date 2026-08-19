package io.github.snz89.roshambo.jwt.serialization;

public interface TokenSerializer<T> {
    String serialize(T token);
}
