package com.chapjun.ordersservice.entity;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
@SequenceGenerator(
        name = "ORDER_SEQ_GENERATOR",
        sequenceName = "ORDER_SEQ", // 매핑할 데이터베이스 시퀀스 이름
        initialValue = 1,
        allocationSize = 1)
public class OrderEntity implements Serializable {

// 직렬화를 넣는 목적 : 가지고 있는 객체를 네트워크로 전송, 데이터베이스 보관 (마샬링, 언마샬링)
// JAVA의 데이터 중 주소값이 들어있는 객체가 있다.
// 이 객체 정보를 다른 곳(다른 네트워크, DB, 파일 등)에서도 쓰고 싶으면 어떻게 해야할까? (주소 그대로 보내면 의미가 없다.)
// 이 주소값을 타고 들어가서 Primitive한 데이터로 전부 변조하는 작업을 직렬화라고 한다.
// 여러 타입의 데이터를 문자타입으로 만들어야 데이터를 주고 받을 수 있기 때문.
// 자바 직렬화 형태의 데이터 교환은 자바 시스템 간의 데이터 교환을 위해서 존재한다.

//                             DTO        DTO            -> JPA와 Service와 Controller 사이를 이동하는 DTO
//     JPA (Entity), Repository -> Serivce -> Controller -> VO 객체

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_SEQ_GENERATOR")
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private Integer qty;
    @Column(nullable = false)
    private Integer unitPrice;
    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String userId;
    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value="CURRENT_TIMESTAMP")
    private Date createdAt;

}
