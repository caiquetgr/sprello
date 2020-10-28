package br.com.caiqueborges.sprello.config.controlleradvice;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
public class ApiError {

    private String requestedUri;
    private HttpMethod method;
    private HttpStatus status;
    private ZonedDateTime time;
    private List<String> errors;

}
