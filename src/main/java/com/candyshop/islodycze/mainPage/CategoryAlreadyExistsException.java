package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.exceptions.ApplicationException;

public class CategoryAlreadyExistsException extends ApplicationException {

    public CategoryAlreadyExistsException(final String categoryName) {
        super("Category with given name: " + categoryName + " already exists.");
    }
}
