package br.com.caiqueborges.sprello.user.service.impl;

import br.com.caiqueborges.sprello.user.exception.UserExistsException;
import br.com.caiqueborges.sprello.user.repository.UserRepository;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import br.com.caiqueborges.sprello.user.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Validated
@Service
class UserService implements CreateUserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public User createUser(@Valid User user) {

        validateUserNotExists(user);
        completeUserInformation(user);

        return saveUser(user);

    }

    private void validateUserNotExists(User user) {

        final String email = user.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new UserExistsException(email);
        }

    }

    private void completeUserInformation(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(Boolean.TRUE);
        user.setCreationDate(ZonedDateTime.now(ZoneId.of("UTC")));
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

}
