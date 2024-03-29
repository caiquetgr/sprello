package br.com.caiqueborges.sprello.login.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationInfo {

    private String authenticationToken;
    private Long userId;
    private ZonedDateTime validUntil;

}
