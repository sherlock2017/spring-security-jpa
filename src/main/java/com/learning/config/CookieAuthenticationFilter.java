package com.learning.config;

import com.learning.service.CookieService;
import com.learning.service.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class CookieAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Starting cookieAuthenticationFilter :: doFilterInternal");

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            log.info("Found {} cookies", cookies.length);
            Optional<Cookie> username = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("username")).findFirst();

            if(username.isPresent()){
                String usernameStr = username.get().getValue();
                authenticateUser(usernameStr, request, response);
            }
            else{
                log.info("Found no username cookie");
            }
        }
        else{
            log.info("Found no cookies");
        }

        log.info("Finished cookieAuthenticationFilter :: doFilterInternal");
        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String usernameStr, HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(usernameStr);

            if(userDetails != null){
                log.info("Found User Details :: {} ", userDetails.toString());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        catch (UsernameNotFoundException e){
            log.warn("{}",e);
            Cookie empty = new Cookie(CookieService.AUTH_COOKIE, "");
            empty.setMaxAge(0);
            response.addCookie(empty);
            SecurityContextHolder.clearContext();
        }
    }
}
