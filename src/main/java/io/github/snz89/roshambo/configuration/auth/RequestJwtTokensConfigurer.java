package io.github.snz89.roshambo.configuration.auth;

import io.github.snz89.roshambo.jwt.AccessToken;
import io.github.snz89.roshambo.jwt.RefreshToken;
import io.github.snz89.roshambo.jwt.factory.TokenFactory;
import io.github.snz89.roshambo.jwt.filter.RequestJwtTokensFilter;
import io.github.snz89.roshambo.jwt.serialization.TokenSerializer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class RequestJwtTokensConfigurer extends AbstractHttpConfigurer<RequestJwtTokensConfigurer, HttpSecurity> {
    private final TokenFactory<RefreshToken, Authentication> refreshTokenFactory;
    private final TokenFactory<AccessToken, RefreshToken> accessTokenFactory;
    private final TokenSerializer<RefreshToken> refreshTokenSerializer;
    private final TokenSerializer<AccessToken> accessTokenSerializer;
    private final ObjectMapper objectMapper;

    public RequestJwtTokensConfigurer(TokenFactory<RefreshToken, Authentication> refreshTokenFactory, TokenFactory<AccessToken, RefreshToken> accessTokenFactory, TokenSerializer<RefreshToken> refreshTokenSerializer, TokenSerializer<AccessToken> accessTokenSerializer, ObjectMapper objectMapper) {
        this.refreshTokenFactory = refreshTokenFactory;
        this.accessTokenFactory = accessTokenFactory;
        this.refreshTokenSerializer = refreshTokenSerializer;
        this.accessTokenSerializer = accessTokenSerializer;
        this.objectMapper = objectMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(HttpSecurity builder) {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(SecurityConfiguration.LOGIN_REQUEST_MATCHER);
        }
    }

    @Override
    public void configure(HttpSecurity builder) {
        var securityContextRepository = builder.getSharedObject(SecurityContextRepository.class);
        var requestJwtTokensFilter = createRequestJwtTokensFilter(securityContextRepository);
        builder.addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class);
    }

    private RequestJwtTokensFilter createRequestJwtTokensFilter(SecurityContextRepository securityContextRepository) {
        return new RequestJwtTokensFilter(
                SecurityConfiguration.LOGIN_REQUEST_MATCHER,
                securityContextRepository,
                refreshTokenFactory,
                accessTokenFactory,
                refreshTokenSerializer,
                accessTokenSerializer,
                objectMapper);
    }
}
