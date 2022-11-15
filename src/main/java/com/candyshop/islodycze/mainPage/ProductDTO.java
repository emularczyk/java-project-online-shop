package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.model.Category;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
class ProductDTO{

    private Long productId;
    private String productName;
    private BigDecimal price;
    private int amount;
    private String photo;
    private String youtube;
    private String description;
    public Set<Category> category;
}
