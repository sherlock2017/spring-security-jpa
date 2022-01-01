package com.learning.config;

import com.learning.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private CookieAuthenticationFilter cookieAuthenticationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // for JPA there is no out of the box implementation for configuration
        // in order to work ss work with JPA we need to create an instance of user service
        // so that ss can use to lookup user
        auth.userDetailsService(userDetailsService);
    }

    /**
     * This is the authorization setup for our app
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/admin").hasAuthority("ADMIN")
                    .antMatchers("/user").hasAnyAuthority("ADMIN", "USER")
                    .antMatchers("/fail").denyAll()
                    .antMatchers("/").permitAll()
                    .and()
                    .formLogin().loginPage("/login").permitAll()
                    .successHandler(new AuthSuccessHandler(getCookieService())).permitAll()
                    .and()
                    .exceptionHandling().accessDeniedPage("/accessDenied")
                    .and()
                    .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(cookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }

    public CookieService getCookieService(){
        return new CookieService();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }





}
