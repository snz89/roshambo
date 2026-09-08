package io.github.snz89.roshambo.security.dto.response;

public record LoginResponse(
        String accessToken,
        String accessTokenExpiry,
        String refreshToken,
        String refreshTokenExpiry
) {}
