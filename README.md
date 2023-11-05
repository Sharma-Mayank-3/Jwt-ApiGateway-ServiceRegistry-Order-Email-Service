# Jwt-ApiGateway-ServiceRegistry-Order-Email-Service

Note : In This Project we will create in total 5 microservices.
1. Order-Service : order place api also call notification service synchronous call
2. Email-Service : email type just dummy app.
3. Service-Registry : to register our all services 
4. Api-Gateway : api gateway to route the app
5. Auth-Service : To validate userInfo, generate jwt token and all.

NOTE : Here first request will come to api-gateway, it will validate the jwt token.
Before every request it will validate the token, if valid then route the request to 
the particular service to get the response.

# 1. Order-Service and Email-Service
```properties
create Order no DB just 1 api to create order, it will also call Email-Service 
synchronously to send the email(dummy) and send request back to client.
```

# 2. Service-Registry
This Will resgiter all the other service to itself.
Pom dependencies to include 
1. Cloud bootstrap
2. eureka server

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>

<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
</dependencyManagement>
```

Note : Add this annotation in main class.
```java
@EnableEurekaServer
```

Note : Add this below confgiguration in application.properties file 
```properties
 # Eureka server configuration
       server.port=8761

       # Register this service with itself
       eureka.client.register-with-eureka=false
       eureka.client.fetch-registry=false

       eureka.instance.hostname=localhost
```

---------------------- To Register order and email-service ------------------------------------------------------------

# Add our Order and Email Service to service-descovery.
Note : Add 2 dependency to the order and email service
1. cloud bootstrap
2. eureka descovery client

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter</artifactId>
</dependency>

<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
</dependencyManagement>
```

Note : add under properties under java-version in pom if missing 
```xml
<spring-cloud.version>2022.0.4</spring-cloud.version>
```

Note : Add configuration in application.properties file of order and email-service
```properties
      eureka.instance.preferIpAddress=true
      eureka.client.register-with-eureka=true
      eureka.client.fetch-registry=true
      eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
      spring.application.name=USER-SERVICE
```

Note : Add this anootation in main class 
```java
@EnableDiscoveryClient
```

Note : Access this end point
```properties
http://localhost:8761/
```

-------------------------------- API-GATEWAY------------------------------------

# Implement Api Gateway
Steps 
A. Add these 5 dependencies

1. Add cloud bootstrap dependency.
2. Add Gateway dependency
3. lombok
4. webflux
5. eureka discovery client.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter</artifactId>
</dependency>

<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
</dependencyManagement>
```

Note : add this inside propertis under java-version
```xml
<spring-cloud.version>2022.0.4</spring-cloud.version>
```

NOTE : add the api-gateway to the service registry.
1. add below configuration to the application.yml file 
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    fetch-registry: 'true'
    register-with-eureka: 'true'
  instance:
    preferIpAddress: 'true'
spring:
  application:
    name: API-GATEWAY-SERVICE
server:
  port: '8084'
```

Note : add below annotation to the mail class 
```java
@EnableDiscoveryClient
```

Note : add gateway configurations to the application.yml file to route the requests
to the particular service

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    fetch-registry: 'true'
    register-with-eureka: 'true'
  instance:
    preferIpAddress: 'true'
spring:
  application:
    name: API-GATEWAY-SERVICE

  cloud:
    gateway:
      routes:
        - id: EMAIL-SERVICE
          uri: http://localhost:8082
          predicates:
            - Path=/api/email/**

        - id: ORDER-SERVICE
          uri: http://localhost:8081
          predicates:
            - Path=/api/order/**


server:
  port: '9090'

```

# NOTE : If you don't want to hard code the url and take it directly from service registry.
```yaml
cloud:
    gateway:
      routes:
        - id: EMAIL-SERVICE
          uri: lb://EMAIL-SERVICE
          predicates:
            - Path=/api/email/**
```


# NOTE : If you don't want to add other url present in service.
```yaml
cloud:
    gateway:
      routes:
        - id: EMAIL-SERVICE
          uri: lb://EMAIL-SERVICE
          predicates:
            - Path=/api/email/**, /yyy/**
```

------------------------------ Auth - Service -----------------------------------

# Auth-Service
1. This service is having user and role entity having a many to many relationship.
2. Create Entity UserEntity
```properties
userId, userName, userPassword, userAge.

@ManyToMany()
RoleEntity.
```

# Role Entity
```properties
1. roleId, roleName, userEntity(many to many)
2. In UserEntity class make fetch = FetchType.EAGER(many to many mapping)
```

# JWT dependencies to be included in pom
1. spring security 
2. 2 jwt dependency
3. 1 jwt-jackson dependency.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>


     <!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

1. Create a package security
```properties
1. create class CustomUserDetailsService.
implement this class with UserDeatilService and see the changes.
implement UserEntity class with UserService class and see the changes.
```
a. CustomUserDetailsService class


b. UserEntity class


c. Create 3 more classes in security package.
```properties
1. JwtAuthentationEntryPoint
2. JwtTokenHelper
3. JwtAuthentationFilter
```

2. Create 2 classes in DTO
```properties
1. JwtAuthRequest
2. JwtAuthResponse
```
3. Create a JWT Exception class also include the JWT exception in the GlobalException class.


4. Create a AuthController class


5. Create SecurityConfig Class
```properties
create 4 beans inside this class 
1. SecurityFilterChain
2. AuthenticationManager
3. DaoAuthenticationProvider
4. PasswordEncoder
```

# Note : For Reference take a look at my other JWT-User-Post-Swagger Project.

Note : Add this auth-service to service registry.
```properties
Add all required dependencies and configuration mentioned above for order \
  and email-service.
```

Note : Add this auth-service to the Api-Gateway.
```properties
follow above steps mentioned in api-gateway service.
```


----------------------------- Flow ---------------------------------
1. Api-gateway should validate the token first before every request comes in with token in header
2. If token validated then pass the request to the particular service.

Note : Steps.
1. Create a filter package and create a AuthenticationFilter class
```java
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

```


2. Every Request that comes in will go through this filter, now we want few request to bypass 
Create a class RouteValidator.
```java
package com.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndPoints = List.of(
            "api/auth/login",
            "/api/auth/validate",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndPoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}

```


3. From class AuthFilter make a restTemplate call to validate the token.
NOTE : Feign client is not working here because of some bean cyclic issue.


4. We want this filter to be exceuted before calling order and email service 
Then go to the application.properties file of gateway and add filter.
```yml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    fetch-registry: 'true'
    register-with-eureka: 'true'
  instance:
    preferIpAddress: 'true'
spring:
  application:
    name: API-GATEWAY-SERVICE

  cloud:
    gateway:
      routes:
        - id: EMAIL-SERVICE
          uri: http://localhost:8082
          predicates:
            - Path=/api/email/**
          filters:
            - AuthenticationFilter

        - id: ORDER-SERVICE
          uri: http://localhost:8081
          predicates:
            - Path=/api/order/**
          filters:
            - AuthenticationFilter

        - id: AUTH-SERVICE
          uri: http://localhost:8083
          predicates:
            - Path=/api/userEntity/**, /api/auth/**

server:
  port: '9090'

```


Reference Link : https://www.youtube.com/watch?v=MWvnmyLRUik&list=PPSV&ab_channel=JavaTechie


