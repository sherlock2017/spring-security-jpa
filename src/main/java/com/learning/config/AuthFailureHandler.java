package com.learning.config;

import com.learning.service.CookieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    CookieService cookieService;

    public AuthFailureHandler(CookieService cookieService) {
        super("/login?error");
        this.cookieService = cookieService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("Started AuthFailureHandler :: onAuthenticationFailure");
        Cookie authCookie = cookieService.getAuthCookie(request.getCookies());
        if (authCookie != null) {
            log.info("Found auth cookie");
            cookieService.resetAuthCookie(response);
        }
        log.info("Finished AuthFailureHandler :: onAuthenticationFailure");
        super.onAuthenticationFailure(request, response, exception);
    }
}
