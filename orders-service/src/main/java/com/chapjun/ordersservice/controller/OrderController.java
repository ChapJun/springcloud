package com.chapjun.ordersservice.controller;

import com.chapjun.ordersservice.dto.OrderDto;
import com.chapjun.ordersservice.entity.OrderEntity;
import com.chapjun.ordersservice.service.OrderService;
import com.chapjun.ordersservice.vo.RequestOrder;
import com.chapjun.ordersservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    final Environment env;
    final OrderService orderService;

    public OrderController(Environment env, OrderService orderService) {
        this.env = env;
        this.orderService = orderService;
    }

    @GetMapping("/health_check")
    public String status() {
        return "It's Working in Order Service. Server Port : " + env.getProperty("local.server.port") ;
    }


    @PostMapping("/{user_id}/orders")
    public ResponseEntity<ResponseOrder> createUser(@RequestBody RequestOrder requestOrder, @PathVariable String user_id) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(user_id);

        ResponseOrder responseOrder = mapper.map(orderService.createOrder(orderDto), ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{user_id}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrdersByUserId(@PathVariable String user_id) {

        Iterable<OrderEntity> orderEntities = orderService.getOrdersByUserId(user_id);

        List<ResponseOrder> list = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        orderEntities.forEach(order -> {
            list.add(mapper.map(order, ResponseOrder.class));
        });

        return  ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/orders/{order_id}")
    public ResponseEntity<ResponseOrder>  getOrderByOrderId(@PathVariable String order_id) {

        OrderDto orderDto = orderService.getOrderByOrderId(order_id);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);
    }
}
