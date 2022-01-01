package com.learning.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieService {

    public static final String AUTH_COOKIE = "username";

    public Cookie createAuthCookie(String usernameValue) {
        Cookie c = new Cookie(AUTH_COOKIE, usernameValue);
        c.setPath("/");

        return c;
    }

    public Cookie getAuthCookie(Cookie[] cookies) {
            if (cookies == null) {
                return null;
            }

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTH_COOKIE)) {
                    return cookie;
                }
            }

            return null;
    }

    public void resetAuthCookie(HttpServletResponse response) {
        Cookie empty = new Cookie(CookieService.AUTH_COOKIE, "");
        empty.setMaxAge(0);
        response.addCookie(empty);
    }
}
