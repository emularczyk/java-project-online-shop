package com.candyshop.islodycze.product;

import com.candyshop.islodycze.exceptions.ApplicationException;

public class ProductAlreadyExistsException extends ApplicationException {

    public ProductAlreadyExistsException(final String productName) {
        super("Product with given ID: " + productName + " already exists.");
    }
}
