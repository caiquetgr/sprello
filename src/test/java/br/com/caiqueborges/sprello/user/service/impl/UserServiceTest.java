package br.com.caiqueborges.sprello.user.service.impl;

import br.com.caiqueborges.sprello.user.exception.UserExistsException;
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

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @Test
    void whenCreateUser_withValidUser_ThenReturnUserCreated() {

        final User userToCreate = Fixture.from(User.class).gimme(UserTemplateLoader.PRE_INSERT);

        doReturn(Boolean.FALSE)
                .when(userRepository)
                .existsByEmail(userToCreate.getEmail());

        doReturn(userToCreate.getPassword())
                .when(passwordEncoder)
                .encode(userToCreate.getPassword());

        doReturn(Fixture.from(User.class).gimme(UserTemplateLoader.AFTER_INSERT))
                .when(userRepository)
                .save(userToCreate);

        final User createdUser = userService.createUser(userToCreate);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getFirstName()).isEqualTo(userToCreate.getFirstName());
        assertThat(createdUser.getLastName()).isEqualTo(userToCreate.getLastName());
        assertThat(createdUser.getEmail()).isEqualTo(userToCreate.getEmail());
        assertThat(createdUser.getPassword()).isEqualTo(userToCreate.getPassword());
        assertThat(createdUser.getId()).isEqualTo(1L);
        assertThat(createdUser.getCreationDate()).isEqualTo(ZonedDateTime.of(2020, Month.MAY.getValue(),
                12, 12, 00, 00, 000000, ZoneId.of("UTC")));
        assertThat(createdUser.getActive()).isTrue();

        verify(userRepository).existsByEmail(userToCreate.getEmail());
        verify(userRepository).save(userToCreate);
        verify(passwordEncoder).encode(userToCreate.getPassword());

    }

    @Test
    void whenCreateUser_withUserAlreadyCreated_ThenShouldThrowUserExistsException() {

        final User userToCreate = Fixture.from(User.class).gimme(UserTemplateLoader.PRE_INSERT);

        doReturn(Boolean.TRUE)
                .when(userRepository)
                .existsByEmail(userToCreate.getEmail());

        assertThatThrownBy(() -> userService.createUser(userToCreate))
                .isInstanceOf(UserExistsException.class);

        verify(userRepository).existsByEmail(userToCreate.getEmail());
        verify(userRepository, never()).save(userToCreate);
        verify(passwordEncoder, never()).encode(userToCreate.getPassword());

    }

}
