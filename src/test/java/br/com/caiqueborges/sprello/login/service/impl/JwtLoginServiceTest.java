package br.com.caiqueborges.sprello.login.service.impl;

import br.com.caiqueborges.sprello.base.service.JwtService;
import br.com.caiqueborges.sprello.login.exception.LoginFailedException;
import br.com.caiqueborges.sprello.login.exception.LoginUserNotActiveException;
import br.com.caiqueborges.sprello.login.service.model.AuthenticationInfo;
import br.com.caiqueborges.sprello.login.service.model.JwtInfo;
import br.com.caiqueborges.sprello.user.fixture.UserTemplateLoader;
import br.com.caiqueborges.sprello.user.repository.UserRepository;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtLoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtLoginService jwtLoginService;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @Test
    void whenFindsUser_andPasswordMatches_thenCreateJwtToken_AndReturnAuthenticationInfo() {

        final User user = Fixture.from(User.class).gimme(UserTemplateLoader.AFTER_INSERT);
        final String email = "caiquetgr@gmail.com";
        final String password = "password";

        final String authenticationToken = "token";
        final ZonedDateTime validUntil = ZonedDateTime.now().plusMinutes(10);

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        given(passwordEncoder.matches(password, user.getPassword()))
                .willReturn(Boolean.TRUE);

        given(jwtService.createJwt(user))
                .willReturn(new JwtInfo(authenticationToken, validUntil));

        AuthenticationInfo authenticationInfo = jwtLoginService.login(email, password);

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, user.getPassword());

        assertThat(authenticationInfo.getUserId()).isEqualTo(user.getId());
        assertThat(authenticationInfo.getAuthenticationToken()).isEqualTo(authenticationToken);
        assertThat(authenticationInfo.getValidUntil()).isEqualTo(validUntil);

    }

    @Test
    void whenDoesntFindsUser_thenThrowLoginFailedException() {

        final String email = "caiquetgr@gmail.com";
        final String password = "password";

        given(userRepository.findByEmail(email))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> jwtLoginService.login(email, password))
                .isInstanceOf(LoginFailedException.class);

        verify(userRepository).findByEmail(email);

    }

    @Test
    void whenFindsUser_andPasswordDoesntMatches_thenThrowLoginFailedException() {

        final User user = Fixture.from(User.class).gimme(UserTemplateLoader.AFTER_INSERT);
        final String email = "caiquetgr@gmail.com";
        final String password = "password";

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        given(passwordEncoder.matches(password, user.getPassword()))
                .willReturn(Boolean.FALSE);

        assertThatThrownBy(() -> jwtLoginService.login(email, password))
                .isInstanceOf(LoginFailedException.class);

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, user.getPassword());

    }

    @Test
    void whenFindsUser_andPasswordMatches_andUserNotActive_thenThrowLoginUserNotActiveException() {

        final User user = Fixture.from(User.class).gimme(UserTemplateLoader.AFTER_INSERT);
        user.setActive(Boolean.FALSE);

        final String email = "caiquetgr@gmail.com";
        final String password = "password";

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        given(passwordEncoder.matches(password, user.getPassword()))
                .willReturn(Boolean.TRUE);

        assertThatThrownBy(() -> jwtLoginService.login(email, password))
                .isInstanceOf(LoginUserNotActiveException.class);

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, user.getPassword());


    }

}
