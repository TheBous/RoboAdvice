package com.roboadvice.security;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configurable
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/",
                                         "/index.html",
                                         "/test.html",
                                         "/html/**",
                                         "/js/**",
                                         "/bower_components/**",
                                         "/css/**",
                                         "/img/**",
                                         "/fonts/**",
                                         "/user/signup",  //open REST API for login and signup
                                         "/user/login",
                                         "/assetsclass/history");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated().and()
                .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }



}
