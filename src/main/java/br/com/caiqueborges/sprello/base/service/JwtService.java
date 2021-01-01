package br.com.caiqueborges.sprello.base.service;

import br.com.caiqueborges.sprello.base.web.exception.JwtAuthenticationException;
import br.com.caiqueborges.sprello.login.service.model.JwtInfo;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.FixedClock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Date;

import static br.com.caiqueborges.sprello.config.ClockAndTimeZoneConfig.DEFAULT_ZONE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private static final String BEARER = "Bearer ";

    private final Clock clock;

    @Value("${app.authorization-token.expiration-in-seconds:600}")
    private Long expirationInSeconds;

    @Value("${app.authorization-token.signature-key}")
    private String signatureKey;

    public JwtInfo createJwt(User user) {

        Date issuedAt = new Date();
        Date expirationDate = new Date(clock.millis() + (expirationInSeconds * 1000));

        String authorizationToken = Jwts.builder()
                .setId(user.getId().toString())
                .setSubject(user.getEmail())
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, signatureKey)
                .compact();

        return JwtInfo.builder()
                .authorizationToken(authorizationToken)
                .expirationDate(ZonedDateTime.ofInstant(expirationDate.toInstant(), DEFAULT_ZONE))
                .build();

    }

    public Jws<Claims> parseToken(String jwtToken) {

        try {

            final Date now = new Date(this.clock.millis());
            final FixedClock jwtClock = new FixedClock(now);

            return Jwts.parser()
                    .setClock(jwtClock)
                    .setSigningKey(signatureKey)
                    .parseClaimsJws(removeBearerFromJwtToken(jwtToken));

        } catch (JwtException e) {
            log.error(e.getLocalizedMessage());
            throw new JwtAuthenticationException();
        }

    }

    private String removeBearerFromJwtToken(String jwtToken) {

        if (isNotBlank(jwtToken) && jwtToken.startsWith(BEARER)) {
            return jwtToken.substring(7);
        }

        return jwtToken;

    }

    public String getUserEmail(Jws<Claims> tokenClaims) {
        return tokenClaims.getBody().getSubject();
    }

    public String getUserId(Jws<Claims> tokenClaims) {
        return tokenClaims.getBody().getId();
    }

    public boolean isTokenValid(Jws<Claims> tokenClaims) {

        Date tokenExpiration = tokenClaims.getBody().getExpiration();
        ZonedDateTime now = ZonedDateTime.ofInstant(clock.instant(), DEFAULT_ZONE);

        return now.isBefore(ZonedDateTime.ofInstant(tokenExpiration.toInstant(), DEFAULT_ZONE));

    }

}
