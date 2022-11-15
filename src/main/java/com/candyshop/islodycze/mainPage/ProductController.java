package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.model.Category;
import com.candyshop.islodycze.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/addProductForm")
    public String addProductForm(Model model) {
        List<Category> categoryList;
        categoryList = categoryService.getAllCategories();
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categoryList", categoryList);
        return "MainPage/addProduct";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(ProductDTO productDTO) {
        final Product product = productMapper.toEntity(productDTO);
        final Product createdProduct = productService.saveProduct(product);
        return "redirect:/searchProducts";
    }

    @GetMapping("/searchProducts")
    public String searchProducts(Model model,
                                 ProductPage productPage,
                                 ProductSearchCriteria productSearchCriteria) {
        List<Category> categoryList;
        categoryList = categoryService.getAllCategories();
        Sort.Direction reverseSortDir = productPage.getSortingDirection()
                .equals(Sort.Direction.ASC) ? Sort.Direction.DESC : Sort.Direction.ASC;
        final Page<Product> pageProduct = productService.getProducts(productPage, productSearchCriteria);
        model.addAttribute("productList", pageProduct);
        model.addAttribute("productPage", productPage);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("categoryList", categoryList);
        return "MainPage/main_page";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProductById(@PathVariable final Long id) {
        Product deletedProduct = productService.deleteProductById(id);
        return "redirect:/searchProducts";
    }

    @GetMapping("/editProductForm/{productId}")
    public String updateProduct(Model model, @PathVariable final long productId) {
        final Product product = productService.getProductById(productId);
        final ProductDTO productDTO = productMapper.toDto(product);
        List<Category> categoryList;
        categoryList = categoryService.getAllCategories();
        productDTO.setCategory(null);
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("id", productId);
        return "MainPage/addProduct";
    }

    //    @GetMapping("/product/{id}")
//    public ResponseEntity<ProductDTO> getProductById(@PathVariable final Long id) {
//        final Product product = productService.getProductById(id);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(productMapper.toDto(product));
//    }

}
