package io.github.snz89.roshambo.jwt.factory;

import io.github.snz89.roshambo.jwt.RefreshToken;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class DefaultRefreshTokenFactory implements TokenFactory<RefreshToken, Authentication> {

    private static final List<String> REFRESH_TOKEN_AUTHORITIES = List.of("JWT_REFRESH", "JWT_LOGOUT");

    private final Duration tokenTtl;

    public DefaultRefreshTokenFactory(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }

    @Override
    public RefreshToken createToken(Authentication authentication) {
        var accessTokenAuthorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var createdAt = Instant.now();
        var expiresAt = createdAt.plus(tokenTtl);

        return new RefreshToken(
                UUID.randomUUID(),
                authentication.getName(),
                REFRESH_TOKEN_AUTHORITIES,
                accessTokenAuthorities,
                createdAt,
                expiresAt);
    }
}
