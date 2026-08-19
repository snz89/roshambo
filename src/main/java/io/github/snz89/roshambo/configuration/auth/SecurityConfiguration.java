package io.github.snz89.roshambo.configuration.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.web.util.matcher.RequestMatchers.not;

@Configuration
public class SecurityConfiguration {
    public static final RequestMatcher LOGIN_REQUEST_MATCHER =
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/auth/login");

    public static final PathPatternRequestMatcher REFRESH_TOKEN_REQUEST_MATCHER
            = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/auth/refresh");

    @Bean
    @Order(1)
    public SecurityFilterChain loginFilterChain(HttpSecurity http, RequestJwtTokensConfigurer requestJwtTokensConfigurer) {
        http.apply(requestJwtTokensConfigurer);

        return http
                .securityMatcher(LOGIN_REQUEST_MATCHER)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, JwtAuthenticationConfigurer jwtAuthenticationConfigurer) {
        http.apply(jwtAuthenticationConfigurer);

        return http
                .securityMatcher(not(LOGIN_REQUEST_MATCHER))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
    }
}
