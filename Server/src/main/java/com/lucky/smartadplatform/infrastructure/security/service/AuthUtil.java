package com.lucky.smartadplatform.infrastructure.security.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtil {

    private AuthUtil() {
    }

    public static UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getCurrentUsername() {
        return getUserDetails().getUsername();
    }

}
