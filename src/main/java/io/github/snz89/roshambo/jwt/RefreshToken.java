package io.github.snz89.roshambo.jwt;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RefreshToken(
        UUID id,
        String subject,
        List<String> authorities,
        List<String> accessTokenAuthorities,
        Instant createdAt,
        Instant expiresAt)
        implements Token {}
