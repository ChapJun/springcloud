package com.chapjun.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // NOT NULL만 포함한다.
public class ResponseUser {
    private String email;
    private String name;
    private String userId;

    private BigDecimal curBal;

    private List<ResponseOrder> orders;

}
