package com.chapjun.userservice.repository;

import com.chapjun.userservice.jpa.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);
}
