package com.candyshop.islodycze.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "categories_product")
@ToString
@DynamicInsert
public class CategoryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOrderId;

    @ManyToMany
    private Set<Product> product;

    @ManyToMany
    private Set<Category> category;

}
