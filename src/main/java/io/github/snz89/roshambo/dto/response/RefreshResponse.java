package io.github.snz89.roshambo.dto.response;

public record RefreshResponse(
        String accessToken,
        String accessTokenExpiry
) {
}
