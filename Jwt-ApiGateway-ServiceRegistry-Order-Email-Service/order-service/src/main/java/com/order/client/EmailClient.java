package com.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "EMAIL-SERVICE", url = "http://localhost:8082/api/email")
public interface EmailClient {

    @GetMapping("/send")
    public ResponseEntity<String> sendEmail();


}
