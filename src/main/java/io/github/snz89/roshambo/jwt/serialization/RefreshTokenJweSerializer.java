package io.github.snz89.roshambo.jwt.serialization;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import io.github.snz89.roshambo.jwt.JwtClaimNames;
import io.github.snz89.roshambo.exception.TokenSerializationException;
import io.github.snz89.roshambo.jwt.RefreshToken;
import java.sql.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshTokenJweSerializer implements TokenSerializer<RefreshToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenJweSerializer.class);

    private final JWEEncrypter jweEncrypter;
    private final JWEAlgorithm jweAlgorithm;
    private final EncryptionMethod encryptionMethod;

    public RefreshTokenJweSerializer(
            JWEEncrypter jweEncrypter, JWEAlgorithm jweAlgorithm, EncryptionMethod encryptionMethod) {
        this.jweEncrypter = jweEncrypter;
        this.jweAlgorithm = jweAlgorithm;
        this.encryptionMethod = encryptionMethod;
    }

    @Override
    public String serialize(RefreshToken token) {
        var header = new JWEHeader.Builder(jweAlgorithm, encryptionMethod)
                .keyID(token.id().toString())
                .build();
        var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim(JwtClaimNames.AUTHORITIES, token.authorities())
                .claim(JwtClaimNames.ACCESS_TOKEN_AUTHORITIES, token.accessTokenAuthorities())
                .build();
        var encryptedJWT = new EncryptedJWT(header, claimsSet);
        try {
            encryptedJWT.encrypt(jweEncrypter);
            return encryptedJWT.serialize();
        } catch (JOSEException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TokenSerializationException(e);
        }
    }
}
