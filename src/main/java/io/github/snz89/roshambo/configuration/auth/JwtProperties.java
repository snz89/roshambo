package io.github.snz89.roshambo.configuration.auth;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "jwt")
@Configuration
public class JwtProperties {
    private String accessTokenKey;
    private String refreshTokenKey;
    private Duration accessTokenTtl;
    private Duration refreshTokenTtl;

    public String getAccessTokenKey() {
        return accessTokenKey;
    }

    public String getRefreshTokenKey() {
        return refreshTokenKey;
    }

    public Duration getAccessTokenTtl() {
        return accessTokenTtl;
    }

    public Duration getRefreshTokenTtl() {
        return refreshTokenTtl;
    }

    public void setAccessTokenKey(String accessTokenKey) {
        this.accessTokenKey = accessTokenKey;
    }

    public void setRefreshTokenKey(String refreshTokenKey) {
        this.refreshTokenKey = refreshTokenKey;
    }

    public void setAccessTokenTtl(Duration accessTokenTtl) {
        this.accessTokenTtl = accessTokenTtl;
    }

    public void setRefreshTokenTtl(Duration refreshTokenTtl) {
        this.refreshTokenTtl = refreshTokenTtl;
    }
}
