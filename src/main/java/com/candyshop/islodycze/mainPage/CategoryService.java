package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category saveCategory(final Category category) {
        Category foundCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (foundCategory != null) {
            throw new CategoryAlreadyExistsException(category.getCategoryName());
        }
        return categoryRepository.save(category);
    }
    public void deleteCategoryByName(final String categoryName) {
        Category categoryToDelete = categoryRepository.findByCategoryName(categoryName);
        categoryRepository.delete(categoryToDelete);
    }
}
