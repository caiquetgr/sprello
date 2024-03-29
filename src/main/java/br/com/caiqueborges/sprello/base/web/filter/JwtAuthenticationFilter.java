package br.com.caiqueborges.sprello.base.web.filter;

import br.com.caiqueborges.sprello.base.service.JwtService;
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

        SecurityContextHolder.clearContext();

    }

    private Authentication createAuthentication(Jws<Claims> tokenClaims) {
        return new UsernamePasswordAuthenticationToken(jwtService.getUserId(tokenClaims),
                null, Collections.emptyList());
    }

    private String getJwtToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

}
