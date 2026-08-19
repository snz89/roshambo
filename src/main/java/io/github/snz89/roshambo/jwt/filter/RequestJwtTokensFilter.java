package io.github.snz89.roshambo.jwt.filter;

import io.github.snz89.roshambo.dto.response.LoginResponse;
import io.github.snz89.roshambo.jwt.AccessToken;
import io.github.snz89.roshambo.jwt.RefreshToken;
import io.github.snz89.roshambo.jwt.factory.TokenFactory;
import io.github.snz89.roshambo.jwt.serialization.TokenSerializer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

public class RequestJwtTokensFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher;

    private final SecurityContextRepository securityContextRepository;

    private final TokenFactory<RefreshToken, Authentication> refreshTokenFactory;
    private final TokenFactory<AccessToken, RefreshToken> accessTokenFactory;

    private final TokenSerializer<RefreshToken> refreshTokenSerializer;
    private final TokenSerializer<AccessToken> accessTokenSerializer;

    private final ObjectMapper objectMapper;

    public RequestJwtTokensFilter(
            RequestMatcher requestMatcher,
            SecurityContextRepository securityContextRepository,
            TokenFactory<RefreshToken, Authentication> refreshTokenFactory,
            TokenFactory<AccessToken, RefreshToken> accessTokenFactory,
            TokenSerializer<RefreshToken> refreshTokenSerializer,
            TokenSerializer<AccessToken> accessTokenSerializer,
            ObjectMapper objectMapper) {
        this.requestMatcher = requestMatcher;
        this.securityContextRepository = securityContextRepository;
        this.refreshTokenFactory = refreshTokenFactory;
        this.accessTokenFactory = accessTokenFactory;
        this.refreshTokenSerializer = refreshTokenSerializer;
        this.accessTokenSerializer = accessTokenSerializer;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (securityContextRepository.containsContext(request)) {
            var context = securityContextRepository.loadDeferredContext(request).get();
            var authentication = context.getAuthentication();

            if (!(authentication instanceof PreAuthenticatedAuthenticationToken)) {
                var refreshToken = refreshTokenFactory.createToken(authentication);
                var accessToken = accessTokenFactory.createToken(refreshToken);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                var tokens = new LoginResponse(
                        accessTokenSerializer.serialize(accessToken),
                        accessToken.expiresAt().toString(),
                        refreshTokenSerializer.serialize(refreshToken),
                        refreshToken.expiresAt().toString());

                objectMapper.writeValue(response.getWriter(), tokens);
                return;
            }
        }

        throw new AccessDeniedException("User must be authenticated");
    }
}
