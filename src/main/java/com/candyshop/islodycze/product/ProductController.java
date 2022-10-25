package com.candyshop.islodycze.product;

import com.candyshop.islodycze.model.Product;
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
    public ResponseEntity<ProductDTO> createProduct(@RequestBody final ProductDTO productDTO) {
        final Product product = productMapper.toEntity(productDTO);
        final Product createdProduct = productService.createProduct(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.toDto(createdProduct));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable final Long id) {
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

    @PutMapping("/product/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable final Long id, @RequestBody final ProductDTO productDTO) {
        final Product product = productMapper
                .toEntity(productDTO)
                .setProductId(id);
        final Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productMapper.toDto(updatedProduct));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<ProductDTO> deleteProductById(@PathVariable final Long id) {
        Product deletedProduct = productService.deleteProductById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productMapper.toDto(deletedProduct));
    }

}
