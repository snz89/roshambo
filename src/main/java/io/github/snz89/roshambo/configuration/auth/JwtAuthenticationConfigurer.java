package io.github.snz89.roshambo.configuration.auth;

import io.github.snz89.roshambo.jwt.filter.RefreshTokenFilter;
import io.github.snz89.roshambo.jwt.AccessToken;
import io.github.snz89.roshambo.jwt.converter.JwtAuthenticationConverter;
import io.github.snz89.roshambo.jwt.RefreshToken;
import io.github.snz89.roshambo.jwt.factory.TokenFactory;
import io.github.snz89.roshambo.jwt.serialization.TokenSerializer;
import io.github.snz89.roshambo.service.TokenAuthenticationUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {
    private final TokenFactory<AccessToken, RefreshToken> accessTokenFactory;
    private final TokenSerializer<AccessToken> accessTokenSerializer;
    private final ObjectMapper objectMapper;

    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;

    public JwtAuthenticationConfigurer(
            TokenFactory<AccessToken, RefreshToken> accessTokenFactory,
            TokenSerializer<AccessToken> accessTokenSerializer,
            ObjectMapper objectMapper,
            JwtAuthenticationConverter jwtAuthenticationConverter,
            TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService) {
        this.accessTokenFactory = accessTokenFactory;
        this.accessTokenSerializer = accessTokenSerializer;
        this.objectMapper = objectMapper;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        this.tokenAuthenticationUserDetailsService = tokenAuthenticationUserDetailsService;
    }

    @Override
    public void configure(HttpSecurity builder) {
        var securityContextRepository = builder.getSharedObject(SecurityContextRepository.class);
        var authenticationManager = builder.getSharedObject(AuthenticationManager.class);

        var refreshTokenFilter = createRefreshTokenFilter(securityContextRepository);
        var jwtAuthenticationFilter = createAuthenticationFilter(authenticationManager);

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(tokenAuthenticationUserDetailsService);

        builder.addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(authenticationProvider);
    }

    private AuthenticationFilter createAuthenticationFilter(AuthenticationManager authenticationManager) {
        var jwtAuthenticationFilter = new AuthenticationFilter(authenticationManager, jwtAuthenticationConverter);
        jwtAuthenticationFilter.setSuccessHandler((request, _, _) ->
                CsrfFilter.skipRequest(request));
        jwtAuthenticationFilter.setFailureHandler(
                (_, response, _) ->
                        response.sendError(HttpServletResponse.SC_FORBIDDEN));
        return jwtAuthenticationFilter;
    }

    private RefreshTokenFilter createRefreshTokenFilter(SecurityContextRepository securityContextRepository) {
        return new RefreshTokenFilter(
                SecurityConfiguration.REFRESH_TOKEN_REQUEST_MATCHER,
                accessTokenFactory,
                accessTokenSerializer,
                securityContextRepository,
                objectMapper
        );
    }
}
