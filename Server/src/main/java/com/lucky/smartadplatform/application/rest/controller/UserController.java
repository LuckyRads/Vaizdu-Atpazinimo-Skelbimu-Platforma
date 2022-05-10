package com.lucky.smartadplatform.application.rest.controller;

import com.lucky.smartadplatform.application.rest.model.RoleDto;
import com.lucky.smartadplatform.application.rest.model.StatusResponse;
import com.lucky.smartadplatform.application.rest.model.StatusResponse.StatusCode;
import com.lucky.smartadplatform.domain.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Secured({ "ROLE_ADMIN" })
    @PutMapping("/roles")
    public ResponseEntity<StatusResponse> addRoleForUser(@RequestBody RoleDto roleDto) {
        var response = new StatusResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            userService.addRoleToUser(roleDto.getUsername(), roleDto.getRole());
            response.setMessage(String.format("Role %s added for user %s.", roleDto.getRole(), roleDto.getUsername()));
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(StatusCode.FAILED);
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(response, httpStatus);
    }

}
