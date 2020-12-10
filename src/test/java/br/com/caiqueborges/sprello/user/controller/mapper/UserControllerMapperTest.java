package br.com.caiqueborges.sprello.user.controller.mapper;

import br.com.caiqueborges.sprello.user.controller.model.CreateUserRequest;
import br.com.caiqueborges.sprello.user.controller.model.CreateUserResponse;
import br.com.caiqueborges.sprello.user.fixture.CreateUserRequestTemplateLoader;
import br.com.caiqueborges.sprello.user.fixture.UserTemplateLoader;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerMapperTest {

    private UserControllerMapper userControllerMapper = new UserControllerMapperImpl();

    @BeforeAll
    static void setup() {
        FixtureFactoryLoader.loadTemplates("br.com.caiqueborges.sprello.user.fixture");
    }

    @Test
    void whenCreateUserRequestToUser_thenReturnUser() {

        final CreateUserRequest createUserRequest = Fixture.from(CreateUserRequest.class)
                .gimme(CreateUserRequestTemplateLoader.VALID_CREATE_USER_REQUEST);

        final User user = userControllerMapper.createUserRequestToUser(createUserRequest);

        assertThat(createUserRequest.getEmail()).isEqualTo(user.getEmail());
        assertThat(createUserRequest.getPassword()).isEqualTo(user.getPassword());
        assertThat(createUserRequest.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(createUserRequest.getLastName()).isEqualTo(user.getLastName());

    }

    @Test
    void whenuserToCreateUserResponse_thenReturnCreateUserResponse() {

        final User user = Fixture.from(User.class).gimme(UserTemplateLoader.AFTER_INSERT);

        final CreateUserResponse createUserResponse = userControllerMapper.userToCreateUserResponse(user);

        assertThat(user.getId()).isEqualTo(createUserResponse.getId());
        assertThat(user.getFirstName()).isEqualTo(createUserResponse.getFirstName());
        assertThat(user.getLastName()).isEqualTo(createUserResponse.getLastName());
        assertThat(user.getEmail()).isEqualTo(createUserResponse.getEmail());

    }

}
