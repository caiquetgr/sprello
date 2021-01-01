package br.com.caiqueborges.sprello.base.web.filter;

import br.com.caiqueborges.sprello.config.controlleradvice.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {

        final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final ApiError apiError = createApiError(httpServletRequest, httpStatus,
                Collections.singletonList(e.getLocalizedMessage()));

        httpServletResponse.getOutputStream().println(objectMapper.writeValueAsString(apiError));

    }

    private ApiError createApiError(HttpServletRequest httpServletRequest, HttpStatus status, List<String> errors) {
        return ApiError.builder()
                .requestedUri(httpServletRequest.getRequestURI())
                .method(HttpMethod.resolve(httpServletRequest.getMethod()))
                .status(status)
                .time(ZonedDateTime.now(ZoneId.of("UTC")))
                .errors(errors)
                .build();
    }

}
