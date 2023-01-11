package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductName(String productName);

    @Modifying
    @Query("update Product set popularity = popularity + 1 where productId in :productIds")
    void incrementProductPopularity(List<Long> productIds);
}