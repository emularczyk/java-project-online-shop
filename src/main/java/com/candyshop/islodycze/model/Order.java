package com.candyshop.islodycze.model;

import com.candyshop.islodycze.model.Enum.OrderStatus;
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
    private Long orderId;

    private String totalCost;

    private LocalDateTime orderDate;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id_fk")
    private UserEntity userIdFk;

    @ManyToMany
    @JoinTable(
            name = "product_orders_orders",
            joinColumns = @JoinColumn(name = "orders_id"),
            inverseJoinColumns = @JoinColumn(name = "product_order_id")
    )
    @ToString.Exclude
    private Set<ProductOrder> productOrderFk;
}
