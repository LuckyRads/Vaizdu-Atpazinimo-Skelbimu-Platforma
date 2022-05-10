package com.lucky.smartadplatform.application.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String username;

    private String email;

    private String accessToken;

}
