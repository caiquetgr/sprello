package br.com.caiqueborges.sprello.config.controlleradvice;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.MapUtils.emptyIfNull;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.replaceEachRepeatedly;

@RequiredArgsConstructor
@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

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

    private ApiError createApiError(ServletWebRequest webRequest, HttpStatus status, List<String> errors) {
        return ApiError.builder()
                .requestedUri(getRequestedUri(webRequest))
                .method(getHttpMethod(webRequest))
                .status(status)
                .time(ZonedDateTime.now(ZoneId.of("UTC")))
                .errors(errors)
                .build();
    }

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<Object> handleBaseException(BaseException ex,
                                                      WebRequest webRequest,
                                                      Locale locale) {

        final Class<? extends BaseException> exceptionClass = ex.getClass();

        final HttpStatus status = exceptionClass.isAnnotationPresent(ResponseStatus.class)
                ? exceptionClass.getAnnotation(ResponseStatus.class).value()
                : HttpStatus.INTERNAL_SERVER_ERROR;

        String message = getMessage(ex, locale);
        ApiError apiError = createApiError((ServletWebRequest) webRequest, status, Collections.singletonList(message));

        return new ResponseEntity<>(apiError, status);

    }

    private String getMessage(BaseException ex, Locale locale) {

        final String exceptionMessageKey = ex.getMessageKey();
        final String message = messageSource.getMessage(exceptionMessageKey, null, locale);

        if (isBlank(message)) {
            return exceptionMessageKey;
        }

        Map<String, Object> messageVariables = emptyIfNull(ex.getMessageVariables());

        if (isNotEmpty(messageVariables)) {

            return replaceEachRepeatedly(
                    message,
                    messageVariables.keySet().stream().map(this::addBrackets).toArray(String[]::new),
                    messageVariables.values().stream().map(Object::toString).toArray(String[]::new)
            );

        }

        return message;

    }

    private String addBrackets(String key) {
        return "{".concat(key).concat("}");
    }

}
