package com.example.demo.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address adress;

    @Enumerated(EnumType.STRING) // ORDINAL은 1,2,3... 만약 enum이 추가되면 어쩔래? 조조되는거여..
    private DeliveryStatus deliveryStatus;
}
