package com.chapjun.userservice.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // CSRF (Cross Script Resource Forgery)
        // 공격자가 사용자가 의도하지 않은 요청을 수행하게 하는 것
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.headers().frameOptions().disable();
    }
}
