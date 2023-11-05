package com.auth.controller;

import com.auth.dto.JwtAuthRequest;
import com.auth.dto.JwtAuthResponse;
import com.auth.exception.JwtException;
import com.auth.security.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest jwtAuthRequest) throws Exception {
        this.authenticate(jwtAuthRequest.getUserName(), jwtAuthRequest.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUserName());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JwtAuthResponse jwtResponse = JwtAuthResponse.builder().token(token).userName(jwtAuthRequest.getUserName()).build();

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public boolean isValidate(@RequestParam("token") String token) throws Exception {
        Boolean validateToke = this.jwtTokenHelper.validate(token);
        if(validateToke){
            return true;
        }else{
            System.out.println("invalid jwt token... Validation fails !!");
            return false;
        }
    }

    private void authenticate(String userName, String password) throws Exception {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        try{
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (BadCredentialsException e){
            System.out.println("invalid credentials....");
            throw new JwtException("invalid credentials....");
        }
    }

}
