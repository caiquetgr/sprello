package br.com.caiqueborges.sprello.login.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtInfo {
    private String authorizationToken;
    private ZonedDateTime expirationDate;
}
