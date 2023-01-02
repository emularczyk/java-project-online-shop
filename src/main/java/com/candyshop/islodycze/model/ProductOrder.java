package com.candyshop.islodycze.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "product_orders")
@ToString
@DynamicInsert
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOrderId;

    private int amount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "product_fk")
    private Product productFk;

    @JsonIgnore
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "order_id")
    private Order orders;
}
