package io.github.snz89.roshambo.dto.response;

public record LoginResponse(
        String accessToken,
        String accessTokenExpiry,
        String refreshToken,
        String refreshTokenExpiry
) {}
