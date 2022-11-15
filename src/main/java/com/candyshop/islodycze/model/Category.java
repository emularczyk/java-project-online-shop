package com.candyshop.islodycze.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "categories")
@ToString
public class Category {
    @Id
    private String categoryName;

    @JsonIgnore
    @ManyToMany(mappedBy = "categoryFk")
    private Set<Product> products = new HashSet<>();
}
