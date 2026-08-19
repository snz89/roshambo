package io.github.snz89.roshambo.configuration.auth;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import io.github.snz89.roshambo.jwt.AccessToken;
import io.github.snz89.roshambo.jwt.RefreshToken;
import io.github.snz89.roshambo.jwt.deserialization.AccessTokenJwsDeserializer;
import io.github.snz89.roshambo.jwt.deserialization.RefreshTokenJweDeserializer;
import io.github.snz89.roshambo.jwt.deserialization.TokenDeserializer;
import io.github.snz89.roshambo.jwt.factory.DefaultAccessTokenFactory;
import io.github.snz89.roshambo.jwt.factory.DefaultRefreshTokenFactory;
import io.github.snz89.roshambo.jwt.factory.TokenFactory;
import io.github.snz89.roshambo.jwt.serialization.AccessTokenJwsSerializer;
import io.github.snz89.roshambo.jwt.serialization.RefreshTokenJweSerializer;
import io.github.snz89.roshambo.jwt.serialization.TokenSerializer;
import java.text.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

@Configuration()
public class JwtConfiguration {
    @Bean
    public TokenFactory<RefreshToken, Authentication> refreshTokenAuthenticationTokenFactory(
            JwtProperties jwtConfiguration) {
        return new DefaultRefreshTokenFactory(jwtConfiguration.getRefreshTokenTtl());
    }

    @Bean
    public TokenFactory<AccessToken, RefreshToken> accessTokenRefreshTokenTokenFactory(JwtProperties jwtConfiguration) {
        return new DefaultAccessTokenFactory(jwtConfiguration.getAccessTokenTtl());
    }

    @Bean
    public TokenSerializer<RefreshToken> refreshTokenSerializer(
            JWEEncrypter jweEncrypter, JWEAlgorithm jweAlgorithm, EncryptionMethod encryptionMethod) {
        return new RefreshTokenJweSerializer(jweEncrypter, jweAlgorithm, encryptionMethod);
    }

    @Bean
    public TokenSerializer<AccessToken> accessTokenSerializer(JWSSigner jwsSigner, JWSAlgorithm jwsAlgorithm) {
        return new AccessTokenJwsSerializer(jwsSigner, jwsAlgorithm);
    }

    @Bean
    public TokenDeserializer<RefreshToken> refreshTokenDeserializer(JWEDecrypter jweDecrypter) {
        return new RefreshTokenJweDeserializer(jweDecrypter);
    }

    @Bean
    public TokenDeserializer<AccessToken> accessTokenTokenDeserializer(JWSVerifier jwsVerifier) {
        return new AccessTokenJwsDeserializer(jwsVerifier);
    }

    @Bean
    public JWSAlgorithm jwsAlgorithm() {
        return JWSAlgorithm.HS256;
    }

    @Bean
    public JWSSigner jwsSigner(JwtProperties jwtProperties) throws ParseException, KeyLengthException {
        return new MACSigner(OctetSequenceKey.parse(jwtProperties.getAccessTokenKey()));
    }

    @Bean
    public JWSVerifier jwsVerifier(JwtProperties jwtProperties) throws ParseException, JOSEException {
        return new MACVerifier(OctetSequenceKey.parse(jwtProperties.getAccessTokenKey()));
    }

    @Bean
    public JWEAlgorithm jweAlgorithm() {
        return JWEAlgorithm.DIR;
    }

    @Bean
    public JWEEncrypter jweEncrypter(JwtProperties jwtProperties) throws ParseException, KeyLengthException {
        return new DirectEncrypter(OctetSequenceKey.parse(jwtProperties.getRefreshTokenKey()));
    }

    @Bean
    public JWEDecrypter jweDecrypter(JwtProperties jwtProperties) throws ParseException, KeyLengthException {
        return new DirectDecrypter(OctetSequenceKey.parse(jwtProperties.getRefreshTokenKey()));
    }

    @Bean
    public EncryptionMethod encryptionMethod() {
        return EncryptionMethod.A128GCM;
    }
}
