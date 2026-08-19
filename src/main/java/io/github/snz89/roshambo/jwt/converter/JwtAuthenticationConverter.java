package io.github.snz89.roshambo.jwt.converter;

import io.github.snz89.roshambo.jwt.AccessToken;
import io.github.snz89.roshambo.jwt.RefreshToken;
import io.github.snz89.roshambo.jwt.deserialization.TokenDeserializer;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationConverter.class);

    private final TokenDeserializer<AccessToken> accessTokenDeserializer;
    private final TokenDeserializer<RefreshToken> refreshTokenDeserializer;

    public JwtAuthenticationConverter(
            TokenDeserializer<AccessToken> accessTokenDeserializer,
            TokenDeserializer<RefreshToken> refreshTokenDeserializer) {
        this.accessTokenDeserializer = accessTokenDeserializer;
        this.refreshTokenDeserializer = refreshTokenDeserializer;
    }

    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            LOGGER.trace("Authorization header is missing for request: {} {}", request.getMethod(), request.getRequestURI());
            return null;
        }

        if (!authorization.startsWith("Bearer ")) {
            LOGGER.debug("Authorization header does not start with 'Bearer ' (value: '{}') for URI: {}",
                    authorization.length() > 15 ? authorization.substring(0, 15) + "..." : authorization,
                    request.getRequestURI());
            return null;
        }

        var tokenString = authorization.substring(7);
        LOGGER.trace("Found Bearer token string. Attempting to deserialize as AccessToken");

        var accessToken = accessTokenDeserializer.tryDeserialize(tokenString);
        if (accessToken.isPresent()) {
            LOGGER.debug("Successfully deserialized Bearer token as AccessToken for subject: {}", accessToken.get().subject());
            return new PreAuthenticatedAuthenticationToken(accessToken.get(), tokenString);
        }

        LOGGER.trace("Token is not a valid AccessToken. Attempting to deserialize as RefreshToken");
        return refreshTokenDeserializer.tryDeserialize(tokenString)
                .map(token -> {
                    LOGGER.debug("Successfully deserialized Bearer token as RefreshToken for subject: {}", token.subject());
                    return new PreAuthenticatedAuthenticationToken(token, tokenString);
                })
                .orElseGet(() -> {
                    LOGGER.warn("Bearer token was provided but could not be deserialized as AccessToken or RefreshToken");
                    return null;
                });
    }
}
