package com.chapjun.userservice.service;

import com.chapjun.userservice.dto.UserDto;
import com.chapjun.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserAll();

    UserDto getUserDetailsByEmail(String email);

    UserDto deleteByUserId(String userId);
    UserDto modifyUser(UserDto userDto);
}
