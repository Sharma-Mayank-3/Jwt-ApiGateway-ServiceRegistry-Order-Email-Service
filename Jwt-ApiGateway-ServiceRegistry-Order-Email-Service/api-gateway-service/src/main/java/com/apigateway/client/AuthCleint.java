//package com.apigateway.client;
//
//import com.apigateway.dto.JwtAuthRequest;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient(name = "AUTH-SERVICE", url = "http://localhost:8083/api/auth")
//public interface AuthCleint {
//
//    @PostMapping("/validate")
//    public boolean isValidate(@RequestParam("token") String token,
//                              @RequestBody JwtAuthRequest jwtAuthRequest);
//
//}
