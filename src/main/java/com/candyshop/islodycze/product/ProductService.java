package com.candyshop.islodycze.product;

import com.candyshop.islodycze.exceptions.ApplicationException;
import com.candyshop.islodycze.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public Product createProduct(final Product product) {
        if (productRepository.findByProductName(product.getProductName()) != null) {
            throw new ProductAlreadyExistsException(product.getProductName());
        }
        return productRepository.save(product);
    }

    public Product getProductById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(final Product product) {
        if(isProductExist(product)) {
            throw new ProductNotFoundException(product.getProductId());
        }
        return productRepository.save(product);
    }

    public Product deleteProductById(final Long id) {
        Product productToDelete = getProductById(id);
        productRepository.delete(productToDelete);
        return productToDelete;
    }

    public boolean isProductExist(final Product product){
        return productRepository.findById(product.getProductId())
                .isEmpty();
    }

}
