package io.github.snz89.roshambo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("jwt")
@Configuration
public class JwtConfiguration {
    private final String accessTokenKey;
    private final String refreshTokenKey;

    public JwtConfiguration(String accessTokenKey, String refreshTokenKey) {
        this.accessTokenKey = accessTokenKey;
        this.refreshTokenKey = refreshTokenKey;
    }

    public String getAccessTokenKey() {
        return accessTokenKey;
    }

    public String getRefreshTokenKey() {
        return refreshTokenKey;
    }
}
