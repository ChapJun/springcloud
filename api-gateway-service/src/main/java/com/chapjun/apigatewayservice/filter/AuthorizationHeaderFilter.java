package com.chapjun.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }
    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {

        // login -> token -> users (with token) -> header (include token)
        return ((exchange, chain) -> {
            ServerHttpRequest req = exchange.getRequest();

            if(!req.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = req.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if(!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        });
    }

    // Mono, Flux -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {

        ServerHttpResponse res =  exchange.getResponse();
        res.setStatusCode(httpStatus);

        log.error(err);
        return res.setComplete();
    }

    private boolean isJwtValid(String jwt) {

        boolean isValid = true;

        String subject = null;

        try {

            //   setSigningKey 메소드 (jaxb-api -> DatatypeConverter)
            //   Header에 저장된 JWT를 해석하는 과정에서 Base64가 사용되고 이때, DatatypeConverter가 사용
            subject= Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody().getSubject();
        }
        catch (Exception e) {
            isValid = false;
        }

        if(subject == null || subject.isEmpty()) {
            isValid = false;
        }

        return isValid;
    }
}

