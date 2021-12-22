package com.chapjun.userservice.service;

import com.chapjun.userservice.dto.UserDto;
import com.chapjun.userservice.jpa.UserEntity;
import com.chapjun.userservice.repository.UserRepository;
import com.chapjun.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    final UserRepository userRepository;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RestTemplate restTemplate;
    private final Environment env;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RestTemplate restTemplate, Environment env) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restTemplate = restTemplate;
        this.env = env;
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userRepository.save(userEntity);

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        /* using as rest template */
        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ResponseOrder>>() {
        });

        List<ResponseOrder> orderList = orderListResponse.getBody();
        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null)
            throw new UsernameNotFoundException(email);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        return userDto;
    }

    @Override
    public UserDto deleteByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        userRepository.delete(userEntity);
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null)
            throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    @Transactional
    //@Transactional(propagation = , isolation = ,noRollbackFor = ,readOnly = ,rollbackFor = ,timeout = )
    public UserDto modifyUser(UserDto userDto) {

        UserEntity userEntity = userRepository.findByUserId(userDto.getUserId());

        if(userEntity == null) {
            throw new UsernameNotFoundException("User Not Found");
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        userEntity.setEmail(userDto.getEmail());
        userEntity.setName(userDto.getName());

        if(userDto.getCurBal() != null)
            userEntity.setCurBal(userDto.getCurBal());

        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userRepository.save(userEntity);

        return mapper.map(userEntity, UserDto.class);
    }
}
