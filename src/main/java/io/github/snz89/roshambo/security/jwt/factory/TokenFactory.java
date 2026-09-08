package io.github.snz89.roshambo.security.jwt.factory;

public interface TokenFactory<T, S> {
    T createToken(S source);
}
