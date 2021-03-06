package com.chapjun.userservice.controller;

import com.chapjun.userservice.dto.UserDto;
import com.chapjun.userservice.jpa.UserEntity;
import com.chapjun.userservice.service.UserService;
import com.chapjun.userservice.vo.Greeting;
import com.chapjun.userservice.vo.RequestUser;
import com.chapjun.userservice.vo.ResponseUser;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final Environment env;

    @Autowired
    private Greeting greeting;

    @Autowired
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/health_check")
    public String status() {

        return "It's Working in User Service."
                + " Local Server port : " + env.getProperty("local.server.port")
                + " Config Server port : " + env.getProperty("server.port")
                + " Token Secret : " + env.getProperty("token.secret")
                + " Token expiration time : " + env.getProperty("token.expiration_time");
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getMessage() + " : " + env.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@Valid @RequestBody RequestUser user) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        ResponseUser responseuser = mapper.map(userService.createUser(userDto), ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseuser);
    }

    @GetMapping("/users")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userList = userService.getUserAll();

        List<ResponseUser> result = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();
        userList.forEach(v -> {
                result.add(mapper.map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {

        UserDto userDto = userService.getUserByUserId(userId);
        ModelMapper mapper = new ModelMapper();

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> deleteUser(@PathVariable String userId) {

        UserDto userDto = userService.deleteByUserId(userId);

        ModelMapper mapper = new ModelMapper();
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);

    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> modifyUser(@Valid @RequestBody RequestUser user, @PathVariable String userId) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.setUserId(userId);

        ResponseUser responseuser = mapper.map(userService.modifyUser(userDto), ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseuser);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> patchUser(@Valid @RequestBody RequestUser user, @PathVariable String userId) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.setUserId(userId);

        ResponseUser responseuser = mapper.map(userService.modifyUser(userDto), ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseuser);
    }

}
