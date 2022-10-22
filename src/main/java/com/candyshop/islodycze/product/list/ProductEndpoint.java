package com.candyshop.islodycze.product.list;

import com.candyshop.islodycze.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductEndpoint {

    @Autowired
    private ProductRepository repository;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return repository.findAll();
    }
}
