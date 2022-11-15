package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductCriteriaRepository productCriteriaRepository;

    public Product saveProduct(final Product product) {
        Product foundProduct = productRepository.findByProductName(product.getProductName());
        if (foundProduct != null) {
            if (foundProduct.getProductId() != product.getProductId()) {
                throw new ProductAlreadyExistsException(product.getProductName());
            }
        }
        return productRepository.save(product);
    }

    public Product getProductById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Page<Product> getProducts(ProductPage productPage,
                                     ProductSearchCriteria productSearchCriteria) {
        return productCriteriaRepository.findAllWithFilter(productPage, productSearchCriteria);
    }


    public List<Product> sortProducts(String sortingValue) {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, sortingValue));
    }

    public Product updateProduct(final Product product) {
        if (isProductExist(product.getProductId())) {
            throw new ProductNotFoundException(product.getProductId());
        }
        return productRepository.save(product);
    }

    public Product deleteProductById(final Long id) {
        Product productToDelete = getProductById(id);
        productRepository.delete(productToDelete);
        return productToDelete;
    }

    public boolean isProductExist(final long productId) {
        return productRepository.findById(productId)
                .isEmpty();
    }
}
