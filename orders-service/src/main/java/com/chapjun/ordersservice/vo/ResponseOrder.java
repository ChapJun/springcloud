package com.chapjun.ordersservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {

    private String orderId;
    private String productId;
    private Integer qty;

    private Integer unitPrice;
    private Integer totalPrice;

    private Date createdAt;
}
