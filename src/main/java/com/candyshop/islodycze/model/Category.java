package com.candyshop.islodycze.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

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
}
