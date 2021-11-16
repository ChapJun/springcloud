package com.chapjun.userservice.dto;

import com.chapjun.userservice.vo.ResponseOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class UserDto {

    private String email;
    private String name;
    private String password;
    private String userId;
    private Date createdAt;

    private BigDecimal curBal;

    private String encryptedPwd;
    private List<ResponseOrder> orders;
}
