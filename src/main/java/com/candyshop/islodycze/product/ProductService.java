package com.candyshop.islodycze.product;

import com.candyshop.islodycze.exceptions.ApplicationException;
import com.candyshop.islodycze.model.Product;
import com.candyshop.islodycze.product.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(final Product product) {
        if (productRepository.findByProductName(product.getProductName()) != null) {
            throw new ApplicationException("Product with name " + product.getProductName() + " already exists.");
        }
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(("Product with given ID: " + productId + " was not found.")));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(final Product product) {
        if(productRepository.findById(product.getProductId()).isEmpty()) {
            // wyjątek nie uruchamia się - DO POPRAWY
            throw new ApplicationException("Product with id " + product.getProductId() + " does not exist.");
        }
        return productRepository.save(product);
    }

    public Product deleteProductById(Long id) {
        Product productToDelete = getProductById(id);
        productRepository.delete(productToDelete);
        return productToDelete;
    }

}
