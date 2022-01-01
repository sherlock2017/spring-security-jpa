package com.learning.config;

import com.learning.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private CookieAuthenticationFilter cookieAuthenticationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/success").hasAuthority("ADMIN")
                .antMatchers("/fail").denyAll()
                .antMatchers("/home").permitAll()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .successHandler(new AuthSuccessHandler(getCookieService()))
                .failureHandler(new AuthFailureHandler(getCookieService()))
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies(CookieService.AUTH_COOKIE)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);

        http.addFilterBefore(cookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable();
    }

    @Bean
    public CookieService getCookieService() {
        return new CookieService();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
