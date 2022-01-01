package com.learning.config;

import com.learning.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    CookieService cookieService;

    public AuthFailureHandler(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Cookie authCookie = cookieService.getAuthCookie(request.getCookies());
        if (authCookie != null) {
            cookieService.resetAuthCookie(response);
        }
    }
}
