package com.candyshop.islodycze.delivery;

import com.candyshop.islodycze.model.Order;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@DynamicInsert
@Table(name = "delivery")
public class DeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String name;

    private String surname;

    private String phoneArea;

    private String phoneNumber;

    private String postalCode;

    private String country;

    private String city;

    private String address;

    @Enumerated(value = EnumType.STRING)
    private DeliveryMode mode;

    private BigDecimal deliveryCost;
}
