package com.candyshop.islodycze.product;

import com.candyshop.islodycze.exceptions.ApplicationException;

class ProductNotFoundException extends ApplicationException {
    ProductNotFoundException(Long productId) {
        super("Product with given ID: " + productId + "was not found.");
    }
}
