package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.model.Category;
import com.candyshop.islodycze.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@AllArgsConstructor
@Controller
public class ProductController {

    @Autowired
    private final ProductService productService;
    @Autowired
    private final ProductMapper productMapper;
    @Autowired
    private final CategoryService categoryService;

    //Page that allows admin to manage categories.
    @GetMapping("/categories_panel")
    public String categoryPanel(final Model model) {
        final List<Category> categoryList;
        categoryList = categoryService.getAllCategories();
        Category newCategory = new Category();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("newCategory", newCategory);
        return "add_category.html";
    }

    //Admin approves and add a new category.
    @PostMapping("/save_category")
    public String saveCategory(final Category category) {
        categoryService.saveCategory(category);
        return "redirect:/categories_panel";
    }

    //Admin deletes a category.
    @GetMapping("/deleteCategory/{categoryName}")
    public String deleteCategoryByName(@PathVariable final String categoryName) {
        categoryService.deleteCategoryByName(categoryName);
        return "redirect:/categories_panel";
    }

    //Page that allows admin to add new products.
    @GetMapping("/add_productForm")
    public String add_productForm(final Model model) {
        final List<Category> categoryList;
        categoryList = categoryService.getAllCategories();
        final ProductDTO productDTO = new ProductDTO();
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categoryList", categoryList);
        return "MainPage/add_product";
    }

    //Admin approves and add a new product.
    @PostMapping("/saveProduct")
    public String saveProduct(final ProductDTO productDTO) {
        final Product product = productMapper.toEntity(productDTO);
        productService.saveProduct(product);
        return "redirect:/";
    }

    //Admin deletes a product.
    @GetMapping("/deleteProduct/{id}")
    public String deleteProductById(@PathVariable final Long id) {
        productService.deleteProductById(id);
        return "redirect:/";
    }

    //The main page, list of top products, filterable and pageable list of products.
    @GetMapping("/")
    public String searchProducts(final Model model,
                                 final ProductPage productPage,
                                 final ProductSearchCriteria productSearchCriteria,
                                 final String sortingAction) {

        final List<Category> categoryList;
        categoryList = categoryService.getAllCategories();
        checkSortingOrder(sortingAction, productPage);

        if (isSearching(productSearchCriteria)) {
            final Page<Product> top10Products = getTop10();
            model.addAttribute("top10List", top10Products);
        }

        final Page<Product> pageProduct = productService.getProducts(productPage, productSearchCriteria);

        model.addAttribute("productCriteria", productSearchCriteria);
        model.addAttribute("productList", pageProduct);
        model.addAttribute("productPage", productPage);
        model.addAttribute("categoryList", categoryList);
        return "MainPage/main_page";
    }

    //Admin panel allows admin to manage products and categories and see order list.
    @GetMapping("/admin_panel")
    public String adminPanel(final Model model,
                                 final ProductPage productPage,
                                 final ProductSearchCriteria productSearchCriteria,
                                 final String sortingAction) {

        final List<Category> categoryList;
        categoryList = categoryService.getAllCategories();
        checkSortingOrder(sortingAction, productPage);

        if (isSearching(productSearchCriteria)) {
            final Page<Product> top10Products = getTop10();
            model.addAttribute("top10List", top10Products);
        }

        final Page<Product> pageProduct = productService.getProducts(productPage, productSearchCriteria);

        model.addAttribute("productCriteria", productSearchCriteria);
        model.addAttribute("productList", pageProduct);
        model.addAttribute("productPage", productPage);
        model.addAttribute("categoryList", categoryList);
        return "MainPage/admin_panel";
    }

    //Admin approve update of a product.
    @GetMapping("/editProductForm/{productId}")
    public String updateProduct(final Model model, @PathVariable final long productId) {
        final Product product = productService.getProductById(productId);
        final ProductDTO productDTO = productMapper.toDto(product);
        final List<Category> categoryList;
        categoryList = categoryService.getAllCategories();
        productDTO.setCategory(null);
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("id", productId);
        return "MainPage/add_product";
    }

    //Product details page.
    @GetMapping("/product/{id}")
    public String getProductById(final Model model, @PathVariable final Long id) {
        final Product product = productService.getProductById(id);
        model.addAttribute("product", productMapper.toDto(product));
        return "product_view";
    }

    //About section.
    @GetMapping("/index")
    public String about(final Model model) {
        return "index";
    }

    private void checkSortingOrder(final String sortBy, final ProductPage productPage) {
        if (sortBy != null) {
            if (productPage.getSortBy().equals(sortBy)) {
                productPage.changeSortingDirection();
            } else {
                productPage.setSortBy(sortBy);
            }
        }
    }

    private boolean isSearching(final ProductSearchCriteria productSearchCriteria) {
        return productSearchCriteria.getSearchedCategories().isEmpty() && productSearchCriteria.getSearchedPhrase().equals("");
    }

    private Page<Product> getTop10() {
        ProductSearchCriteria top10Criteria = new ProductSearchCriteria();
        ProductPage top10Page = new ProductPage();
        top10Page.setSortBy("popularity");
        return productService.getProducts(top10Page, top10Criteria);
    }
}
