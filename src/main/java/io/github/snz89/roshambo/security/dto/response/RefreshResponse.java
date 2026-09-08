package io.github.snz89.roshambo.security.dto.response;

public record RefreshResponse(
        String accessToken,
        String accessTokenExpiry
) {
}
