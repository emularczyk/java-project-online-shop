package com.candyshop.islodycze.exceptions;

public class ProductNotFoundException extends ApplicationException {
    public ProductNotFoundException(Long productId) {
        super("Product with given ID: " + productId + "was not found.");
    }
}
