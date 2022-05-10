package com.lucky.smartadplatform.application.rest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    private String username;

    private String password;

    private Boolean rememberMe;

}
