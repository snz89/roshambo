package io.github.snz89.roshambo.jwt.deserialization;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import io.github.snz89.roshambo.jwt.JwtClaimNames;
import io.github.snz89.roshambo.exception.TokenDeserializationException;
import io.github.snz89.roshambo.jwt.RefreshToken;
import java.text.ParseException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshTokenJweDeserializer implements TokenDeserializer<RefreshToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenJweDeserializer.class);

    private final JWEDecrypter jweDecrypter;

    public RefreshTokenJweDeserializer(JWEDecrypter jweDecrypter) {
        this.jweDecrypter = jweDecrypter;
    }

    @Override
    public RefreshToken deserialize(String tokenString) {
        try {
            var encryptedJwt = EncryptedJWT.parse(tokenString);
            encryptedJwt.decrypt(jweDecrypter);
            JWTClaimsSet claimsSet = encryptedJwt.getJWTClaimsSet();
            return new RefreshToken(
                    UUID.fromString(claimsSet.getJWTID()),
                    claimsSet.getSubject(),
                    claimsSet.getStringListClaim(JwtClaimNames.AUTHORITIES),
                    claimsSet.getStringListClaim(JwtClaimNames.ACCESS_TOKEN_AUTHORITIES),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant());
        } catch (ParseException | JOSEException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TokenDeserializationException(e);
        }
    }
}
