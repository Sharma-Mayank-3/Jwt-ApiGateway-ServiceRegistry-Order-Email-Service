package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private int orderId;
    private String orderDesc;
    private int orderAmount;
    private boolean status;
    private String email;
}
