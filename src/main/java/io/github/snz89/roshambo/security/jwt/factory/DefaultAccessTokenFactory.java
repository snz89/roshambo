package io.github.snz89.roshambo.security.jwt.factory;

import io.github.snz89.roshambo.security.jwt.AccessToken;
import io.github.snz89.roshambo.security.jwt.RefreshToken;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class DefaultAccessTokenFactory implements TokenFactory<AccessToken, RefreshToken> {

    private final Duration tokenTtl;

    public DefaultAccessTokenFactory(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }

    @Override
    public AccessToken createToken(RefreshToken refreshToken) {
        var createdAt = Instant.now();
        var expiresAt = createdAt.plus(tokenTtl);

        return new AccessToken(
                UUID.randomUUID(), refreshToken.subject(), refreshToken.accessTokenAuthorities(), createdAt, expiresAt);
    }
}
