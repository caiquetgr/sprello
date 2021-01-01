package br.com.caiqueborges.sprello.base.web.service;

import br.com.caiqueborges.sprello.base.service.JwtService;
import br.com.caiqueborges.sprello.base.web.exception.RequestAuthorizationUserIdException;
import br.com.caiqueborges.sprello.base.web.exception.RequestWithoutAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@RequestScope
public class JwtRequestService {

    private final HttpServletRequest request;
    private final JwtService jwtService;

    public Long getRequestUserId() {

        final String authorization = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(StringUtils::isNotBlank)
                .orElseThrow(RequestWithoutAuthorizationException::new);

        return Optional.of(authorization)
                .map(jwtService::parseToken)
                .map(jwtService::getUserId)
                .map(Long::valueOf)
                .orElseThrow(RequestAuthorizationUserIdException::new);

    }

}
