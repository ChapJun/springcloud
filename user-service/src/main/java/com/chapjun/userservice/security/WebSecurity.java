package com.chapjun.userservice.security;


import com.chapjun.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 권한 관련

        // CSRF (Cross Script Resource Forgery)
        // 공격자가 사용자가 의도하지 않은 요청을 수행하게 하는 것
        http.csrf().disable();

//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();

        http.authorizeRequests().antMatchers("/**")
                .access("hasIpAddress('192.168.0.191/24') or hasIpAddress('127.0.0.1/24')")
//                .hasIpAddress("192.168.0.191")
                .and()
                .addFilter(getAuthenticationFilter());

        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {

        AuthenticationFilter filter =
                new AuthenticationFilter(authenticationManager(), userService, env);

//        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }

    // select pwd from user where email=?
    // db_pwd(encrypted) == input_pwd(encrypted)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 인증 관련
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

        // userDetailsService -> 사용자가 전달한 이메일과 패스워드로 로그인 처리 (사용자 데이터 검색 - select문)
        // passwordEncoder -> 사용자가 입력한 패스워드를 암호화
    }
}
