package com.micro.gateway.config;

import com.micro.gateway.dto.UserDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component

public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final WebClient webClient;

    public AuthFilter(WebClient webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            final String jwt = authHeader.substring(7);
            final String url = "http://localhost:8765/auth/api/v1/validateToken?token=";

            return webClient.post()
                    .uri(url + jwt)
                    .retrieve().bodyToMono(UserDto.class)
                    .map(user -> {
                        ServerHttpRequest request = exchange.getRequest()
                                .mutate()
                                .header("X-auth-user-id", String.valueOf(user.getId()))
                                .build();
                        return exchange.mutate().request(request).build();
                    }).flatMap(chain::filter);
        };
    }

    public static class Config {
    }
}
