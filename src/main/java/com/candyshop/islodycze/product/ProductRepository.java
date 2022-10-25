package com.candyshop.islodycze.product;

import com.candyshop.islodycze.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductName(String productName);
}