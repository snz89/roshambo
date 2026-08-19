package io.github.snz89.roshambo.jwt.serialization;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.github.snz89.roshambo.jwt.JwtClaimNames;
import io.github.snz89.roshambo.exception.TokenSerializationException;
import io.github.snz89.roshambo.jwt.AccessToken;
import java.sql.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessTokenJwsSerializer implements TokenSerializer<AccessToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenJwsSerializer.class);

    private final JWSSigner jwsSigner;
    private final JWSAlgorithm jwsAlgorithm;

    public AccessTokenJwsSerializer(JWSSigner jwsSigner, JWSAlgorithm jwsAlgorithm) {
        this.jwsSigner = jwsSigner;
        this.jwsAlgorithm = jwsAlgorithm;
    }

    @Override
    public String serialize(AccessToken token) {
        var header =
                new JWSHeader.Builder(jwsAlgorithm).keyID(token.id().toString()).build();
        var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim(JwtClaimNames.AUTHORITIES, token.authorities())
                .build();
        var signedJwt = new SignedJWT(header, claimsSet);
        try {
            signedJwt.sign(jwsSigner);
            return signedJwt.serialize();
        } catch (JOSEException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TokenSerializationException(e);
        }
    }
}
