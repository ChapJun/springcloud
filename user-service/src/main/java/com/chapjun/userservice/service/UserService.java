package com.chapjun.userservice.service;

import com.chapjun.userservice.dto.UserDto;
import com.chapjun.userservice.jpa.UserEntity;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserAll();
}
