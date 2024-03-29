package br.com.caiqueborges.sprello.login.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String authenticationToken;
    private Long userId;
    private ZonedDateTime validUntil;

}
