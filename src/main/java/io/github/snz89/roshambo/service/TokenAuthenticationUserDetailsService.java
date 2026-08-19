package io.github.snz89.roshambo.service;

import io.github.snz89.roshambo.jwt.Token;
import io.github.snz89.roshambo.jwt.TokenUser;
import java.time.Instant;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            var authorities = token.authorities().stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return new TokenUser(
                    token.subject(),
                    "missing",
                    true,
                    true,
                    token.expiresAt().isAfter(Instant.now()),
                    true,
                    authorities,
                    token);
        }

        throw new UsernameNotFoundException("Principal must be of type " + Token.class);
    }
}
