package com.candyshop.islodycze.product;

import com.candyshop.islodycze.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
class ProductDTO {

    private Long productId;
    private String productName;
    private BigDecimal price;
    private int amount;
    private String photo;
    private String youtube;
    private String description;
    private Set<Category> category;

}
