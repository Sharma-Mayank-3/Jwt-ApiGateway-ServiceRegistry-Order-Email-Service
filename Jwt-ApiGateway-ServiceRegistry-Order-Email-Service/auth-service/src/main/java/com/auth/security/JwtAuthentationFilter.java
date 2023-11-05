package com.auth.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthentationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. step 1 get Token.
        // Bearer 123nnv123;
        String requestToken = request.getHeader("Authorization");

        String userName = null;
        String token = null;

        if(requestToken != null && requestToken.startsWith("Bearer")){

            token = requestToken.substring(7);
            try{
                userName = this.jwtTokenHelper.getUsernameFromToken(token);
            }catch (IllegalArgumentException e){
                System.out.println("Illegal Argument while fetching the username !!");
            }catch (ExpiredJwtException e){
                System.out.println("Given jwt token is expired !!");
            }catch (MalformedJwtException e){
                System.out.println("Some changed has done in token !! Invalid Token");
            }



        }else {
            System.out.println("Jwt token is not valid and does not start with bearer...+ Invalid Header Value !! ");
        }

        // step 2 validate token.
        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

            if(this.jwtTokenHelper.validateToken(token, userDetails)){

                // step 3 set authentication
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }else{
                System.out.println("invalid jwt token... Validation fails !!");
            }
        }else {
            System.out.println("invalid jwt token...");
        }

        // if auth fails then it will go to the JwtAuthentationEntryPoint commence method
        filterChain.doFilter(request, response);

    }
}
