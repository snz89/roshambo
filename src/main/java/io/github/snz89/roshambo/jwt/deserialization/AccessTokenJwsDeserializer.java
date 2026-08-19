package io.github.snz89.roshambo.jwt.deserialization;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.github.snz89.roshambo.jwt.JwtClaimNames;
import io.github.snz89.roshambo.exception.TokenDeserializationException;
import io.github.snz89.roshambo.jwt.AccessToken;
import java.text.ParseException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessTokenJwsDeserializer implements TokenDeserializer<AccessToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenJwsDeserializer.class);

    private final JWSVerifier jwsVerifier;

    public AccessTokenJwsDeserializer(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    public AccessToken deserialize(String tokenString) {
        try {
            var signedJWT = SignedJWT.parse(tokenString);
            if (signedJWT.verify(jwsVerifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                return new AccessToken(
                        UUID.fromString(claimsSet.getJWTID()),
                        claimsSet.getSubject(),
                        claimsSet.getStringListClaim(JwtClaimNames.AUTHORITIES),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant());
            }
        } catch (ParseException | JOSEException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TokenDeserializationException(e);
        }

        throw new TokenDeserializationException("JWT verification attempt failed");
    }
}
