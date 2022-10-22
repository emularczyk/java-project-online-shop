package com.candyshop.islodycze.product.list;

import com.candyshop.islodycze.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}