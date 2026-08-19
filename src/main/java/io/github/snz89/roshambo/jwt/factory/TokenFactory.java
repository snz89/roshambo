package io.github.snz89.roshambo.jwt.factory;

public interface TokenFactory<T, S> {
    T createToken(S source);
}
