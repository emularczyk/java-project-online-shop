package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.exceptions.ApplicationException;

public class ProductNotFoundException extends ApplicationException {
    public ProductNotFoundException(final Long productId) {
        super("Product with given ID: " + productId + " was not found.");
    }
}
