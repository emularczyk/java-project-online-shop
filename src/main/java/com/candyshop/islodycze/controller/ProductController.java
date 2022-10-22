package com.candyshop.islodycze.controller;

import com.candyshop.islodycze.dto.ProductDTO;
import com.candyshop.islodycze.mappers.ProductMapper;
import com.candyshop.islodycze.model.Product;
import com.candyshop.islodycze.service.ProductService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class ProductController {

    @Autowired
    private final ProductService productService;
    @Autowired
    private final ProductMapper productMapper;

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody final ProductDTO productDTO){
        final Product product = productMapper.toEntity(productDTO);
        final Product createdProduct = productService.createProduct(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.toDto(createdProduct));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        final Product product = productService.getProductById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productMapper.toDto(product));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        final List<Product> productList = productService.getAllProducts();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productMapper.toDto(productList));
    }

    @PutMapping("/product")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody final ProductDTO productDTO){
        final Product product = productMapper.toEntity(productDTO);
        final Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productMapper.toDto(updatedProduct));
    }

//    @JsonIgnoreProperties(value= {"suppliers"})
    @DeleteMapping("/product/{id}")
    public ResponseEntity<ProductDTO> deleteProductById(@PathVariable Long id) {
        // działa, tylko nie zwraca JSONa po usunięciu - OGARNĄĆ
        final Product deletedProduct = productService.deleteProductById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productMapper.toDto(deletedProduct));
    }

}
