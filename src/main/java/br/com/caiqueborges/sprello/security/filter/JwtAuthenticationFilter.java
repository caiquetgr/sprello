package br.com.caiqueborges.sprello.security.filter;

import br.com.caiqueborges.sprello.security.service.impl.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = getJwtToken(request);

        if (isNotBlank(jwtToken)) {

            Jws<Claims> tokenClaims = jwtService.parseToken(jwtToken);

            if (jwtService.isTokenValid(tokenClaims)) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(createAuthentication(tokenClaims));
            }

        }

        filterChain.doFilter(request, response);

    }

    private Authentication createAuthentication(Jws<Claims> tokenClaims) {
        return new UsernamePasswordAuthenticationToken(jwtService.getUserEmail(tokenClaims),
                null, Collections.emptyList());
    }

    private String getJwtToken(HttpServletRequest request) {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isNotBlank(authorization) && authorization.startsWith(BEARER)) {
            return authorization.substring(7);
        }

        return authorization;

    }

}
