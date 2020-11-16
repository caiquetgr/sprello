package br.com.caiqueborges.sprello.security.service.impl;

import br.com.caiqueborges.sprello.security.exception.JwtAuthenticationException;
import br.com.caiqueborges.sprello.security.service.model.JwtInfo;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JwtService {

    public static final ZoneId ZONE_UTC = ZoneId.of("UTC");
    @Value("${app.authorization-token.expiration-in-seconds:600}")
    private Long expirationInSeconds;

    @Value("${app.authorization-token.signature-key}")
    private String signatureKey;

    public JwtInfo createJwt(User user) {

        Date issuedAt = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + (expirationInSeconds * 1000));

        String authorizationToken = Jwts.builder()
                .setId(user.getId().toString())
                .setSubject(user.getEmail())
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, signatureKey)
                .compact();

        return JwtInfo.builder()
                .authorizationToken(authorizationToken)
                .expirationDate(ZonedDateTime.ofInstant(expirationDate.toInstant(), ZONE_UTC))
                .build();

    }

    public Jws<Claims> parseToken(String jwtToken) {

        try {
            return Jwts.parser()
                    .setSigningKey(signatureKey)
                    .parseClaimsJws(jwtToken);
        } catch (JwtException e) {
            throw new JwtAuthenticationException(e.getLocalizedMessage());
        }

    }

    public String getUserEmail(Jws<Claims> tokenClaims) {
        return tokenClaims.getBody().getSubject();
    }

    public boolean isTokenValid(Jws<Claims> tokenClaims) {

        Date tokenExpiration = tokenClaims.getBody().getExpiration();
        ZonedDateTime now = ZonedDateTime.now(ZONE_UTC);

        return now.isBefore(ZonedDateTime.ofInstant(tokenExpiration.toInstant(), ZONE_UTC));

    }

}
