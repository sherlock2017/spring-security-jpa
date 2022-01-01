package com.learning.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class CookieService {
    public Cookie createCookie(String userName) {
        Cookie c = new Cookie("username", userName);
        c.setPath("/");

        return c;
    }
}
