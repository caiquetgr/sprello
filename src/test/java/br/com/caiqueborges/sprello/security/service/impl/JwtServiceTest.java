package br.com.caiqueborges.sprello.security.service.impl;

import br.com.caiqueborges.sprello.security.service.model.JwtInfo;
import br.com.caiqueborges.sprello.user.fixture.UserTemplateLoader;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeAll
    static void setupAll() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @BeforeEach
    void setup() {

        jwtService = new JwtService(Clock.fixed(Instant.parse("2020-05-12T10:00:00.00Z"), ZoneId.of("UTC")));

        ReflectionTestUtils.setField(jwtService, "expirationInSeconds", Long.valueOf(60L));
        ReflectionTestUtils.setField(jwtService, "signatureKey", "sigKey!@#");

    }

    @Test
    void whenCreateJwt_thenReturnValidTokenWithUserInformation() {

        final User user = Fixture.from(User.class).gimme(UserTemplateLoader.AFTER_INSERT);

        final JwtInfo jwtInfo = jwtService.createJwt(user);

        final Jws<Claims> claimsJws = jwtService.parseToken(jwtInfo.getAuthorizationToken());

        final String userEmail = jwtService.getUserEmail(claimsJws);
        final String userId = jwtService.getUserId(claimsJws);

        assertThat(jwtInfo.getExpirationDate()).isEqualTo(ZonedDateTime.of(
                LocalDate.of(2020, Month.MAY, 12),
                LocalTime.of(10, 1, 0, 0),
                ZoneId.of("UTC")
        ));

        assertThat(userEmail).isEqualTo(user.getEmail());
        assertThat(Long.valueOf(userId)).isEqualTo(user.getId());

    }

    @Test
    void whenCallTokenIsValid_withNotExpiredToken_thenReturnTrue() {

        final User user = Fixture.from(User.class).gimme(UserTemplateLoader.AFTER_INSERT);

        final JwtInfo jwtInfo = jwtService.createJwt(user);

        final Jws<Claims> claimsJws = jwtService.parseToken(jwtInfo.getAuthorizationToken());

        assertThat(jwtService.isTokenValid(claimsJws)).isTrue();

    }

    @Test
    void whenCallTokenIsValid_withExpiredToken_thenReturnFalse() {

        final User user = Fixture.from(User.class).gimme(UserTemplateLoader.AFTER_INSERT);

        final JwtInfo jwtInfo = jwtService.createJwt(user);

        final Jws<Claims> claimsJws = jwtService.parseToken(jwtInfo.getAuthorizationToken());

        ReflectionTestUtils.setField(jwtService, "clock",
                Clock.fixed(Instant.parse("2020-05-12T11:00:00.00Z"), ZoneId.of("UTC")));

        assertThat(jwtService.isTokenValid(claimsJws)).isFalse();

    }

}
