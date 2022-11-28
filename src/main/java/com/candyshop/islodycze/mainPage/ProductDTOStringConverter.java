package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.model.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProductDTOStringConverter implements Converter<String, Set<Category>> {

    @Override
    public Set<Category> convert(String categoryString) {
        final Set<Category> category = new HashSet<>();
            category.add(new Category()
                    .setCategoryName(categoryString));
        return category;
    }
}