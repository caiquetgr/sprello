package br.com.caiqueborges.sprello.config.controlleradvice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest webRequest) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = createApiError((ServletWebRequest) webRequest, HttpStatus.BAD_REQUEST, errors);

        return handleExceptionInternal(ex, apiError, headers, status, webRequest);

    }

    private String getRequestedUri(ServletWebRequest webRequest) {
        return webRequest.getRequest().getRequestURI();
    }

    private HttpMethod getHttpMethod(ServletWebRequest webRequest) {
        return webRequest.getHttpMethod();
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest webRequest) {

        HttpStatus status;

        if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
            status = ex.getClass().getAnnotation(ResponseStatus.class).value();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ApiError apiError = createApiError((ServletWebRequest) webRequest, status,
                Collections.singletonList(ex.getLocalizedMessage()));

        return new ResponseEntity<>(apiError, status);

    }

    private ApiError createApiError(ServletWebRequest webRequest, HttpStatus status, List<String> errors) {
        return ApiError.builder()
                .requestedUri(getRequestedUri(webRequest))
                .method(getHttpMethod(webRequest))
                .status(status)
                .time(ZonedDateTime.now(ZoneId.of("UTC")))
                .errors(errors)
                .build();
    }

}
