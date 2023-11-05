package com.order.service;

import com.order.client.EmailClient;
import com.order.dto.OrderRequestDto;
import com.order.dto.OrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private EmailClient emailClient;

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto){

        ResponseEntity<String> stringResponseEntity = emailClient.sendEmail();
        String responseEntityBody = stringResponseEntity.getBody();
        return OrderResponseDto.builder()
                .orderAmount(orderRequestDto.getOrderAmount())
                .status(true)
                .orderId(orderRequestDto.getOrderId())
                .orderDesc(orderRequestDto.getOrderDesc())
                .email(responseEntityBody)
                .build();

    }

}
