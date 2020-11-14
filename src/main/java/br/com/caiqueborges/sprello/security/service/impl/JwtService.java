package br.com.caiqueborges.sprello.security.service.impl;

import br.com.caiqueborges.sprello.security.service.model.JwtInfo;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JwtService {

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
                .expirationDate(ZonedDateTime.ofInstant(expirationDate.toInstant(), ZoneId.of("UTC")))
                .build();

    }

}
