package com.candyshop.islodycze.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "orders")
@ToString
@DynamicInsert
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersId;

    private String totalCost;

    private LocalDateTime date;

    @Enumerated(value=EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    private User userId;

    @ManyToMany
    private Set<ProductOrder> productOrder;
}
