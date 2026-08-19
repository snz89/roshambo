package io.github.snz89.roshambo.jwt.deserialization;

import io.github.snz89.roshambo.exception.TokenDeserializationException;

import java.util.Optional;

public interface TokenDeserializer<T> {
    T deserialize(String tokenString);

    default Optional<T> tryDeserialize(String tokenString) {
        try {
            T token = deserialize(tokenString);
            return Optional.of(token);
        } catch (TokenDeserializationException _) {
            return Optional.empty();
        }
    }
}
