package com.candyshop.islodycze.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "users")
@ToString
@DynamicInsert
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    private BigDecimal price;

    private int amount;

    private String photo;

    private String youtube;

    private String description;

    @ManyToMany
    private Set<Category> category;
}
