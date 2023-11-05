package com.apigateway.filter;

import com.apigateway.dto.JwtAuthRequest;
import com.apigateway.exception.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

//    @Autowired
//    private AuthCleint authCleint;

    @Autowired
    private RestTemplate restTemplate;

    public AuthenticationFilter() {
        super(Config.class);
    }

    // any logic will comes in this apply method.
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain)->{

        if(routeValidator.isSecured.test(exchange.getRequest())){
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                throw new JwtException("missing autherization header...");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if(authHeader != null && authHeader.startsWith("Bearer ")){

                String token = authHeader.substring(7);

                try {
                    JwtAuthRequest j = new JwtAuthRequest();
                    boolean validate = restTemplate.getForObject("http://localhost:8083/api/auth/validate?token=" + token, boolean.class);
//                    boolean validate = authCleint.isValidate(token, j);
                    if(validate){

                    }else{
                        System.out.println("token not valid...");
                        throw new JwtException("Token not valid....");
                    }
                }catch (Exception e){
                    throw new JwtException(e.getMessage()+" Token not valid....");
                }

            }else{
                throw new JwtException("Auth header is null so does not start with bearer...");
            }
        }

            return chain.filter(exchange);
        });
    }

    public static class Config{

    }
}
