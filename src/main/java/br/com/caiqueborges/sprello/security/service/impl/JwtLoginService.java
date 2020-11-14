package br.com.caiqueborges.sprello.security.service.impl;

import br.com.caiqueborges.sprello.security.exception.LoginFailedException;
import br.com.caiqueborges.sprello.security.exception.LoginUserNotActiveException;
import br.com.caiqueborges.sprello.security.service.LoginService;
import br.com.caiqueborges.sprello.security.service.model.AuthenticationInfo;
import br.com.caiqueborges.sprello.security.service.model.JwtInfo;
import br.com.caiqueborges.sprello.user.repository.UserRepository;
import br.com.caiqueborges.sprello.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.apache.commons.lang3.BooleanUtils.negate;

@RequiredArgsConstructor
@Service
class JwtLoginService implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationInfo login(String email, String password) {

        final User user = getUserAndValidate(email, password);

        JwtInfo jwtInfo = jwtService.createJwt(user);

        return AuthenticationInfo.builder()
                .userId(user.getId())
                .authenticationToken(jwtInfo.getAuthorizationToken())
                .validUntil(jwtInfo.getExpirationDate())
                .build();

    }

    private User getUserAndValidate(String email, String password) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new LoginFailedException();
        }

        final User user = userOpt.get();

        if (negate(passwordEncoder.matches(password, user.getPassword()))) {
            throw new LoginFailedException();
        }

        if (negate(user.getActive())) {
            throw new LoginUserNotActiveException();
        }

        return user;

    }

}
