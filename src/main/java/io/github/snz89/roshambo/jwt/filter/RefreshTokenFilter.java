package io.github.snz89.roshambo.jwt.filter;

import io.github.snz89.roshambo.dto.response.RefreshResponse;
import io.github.snz89.roshambo.jwt.AccessToken;
import io.github.snz89.roshambo.jwt.RefreshToken;
import io.github.snz89.roshambo.jwt.TokenUser;
import io.github.snz89.roshambo.jwt.factory.TokenFactory;
import io.github.snz89.roshambo.jwt.serialization.TokenSerializer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class RefreshTokenFilter extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher;
    private final TokenFactory<AccessToken, RefreshToken> accessTokenFactory;
    private final TokenSerializer<AccessToken> accessTokenSerializer;
    private final SecurityContextRepository securityContextRepository;
    private final ObjectMapper objectMapper;

    public RefreshTokenFilter(RequestMatcher requestMatcher, TokenFactory<AccessToken, RefreshToken> accessTokenFactory,
                              TokenSerializer<AccessToken> accessTokenSerializer, SecurityContextRepository securityContextRepository, ObjectMapper objectMapper) {
        this.requestMatcher = requestMatcher;
        this.accessTokenFactory = accessTokenFactory;
        this.accessTokenSerializer = accessTokenSerializer;
        this.securityContextRepository = securityContextRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (securityContextRepository.containsContext(request)) {
            var context = securityContextRepository.loadDeferredContext(request).get();
            var authentication = context.getAuthentication();
            if (authentication instanceof PreAuthenticatedAuthenticationToken
                    && authentication.getPrincipal() instanceof TokenUser user) {
                if (!(user.getToken() instanceof RefreshToken)) {
                    throw new AccessDeniedException("Expected to receive a refresh token");
                }

                var accessToken = accessTokenFactory.createToken((RefreshToken) user.getToken());

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                var responseBody = new RefreshResponse(
                        accessTokenSerializer.serialize(accessToken),
                        accessToken.expiresAt().toString()
                );
                objectMapper.writeValue(response.getWriter(), responseBody);
            }
        }

        throw new AccessDeniedException("User must be authenticated");
    }
}
