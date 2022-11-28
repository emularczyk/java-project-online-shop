package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.mappers.Mapper;
import com.candyshop.islodycze.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class ProductMapper extends Mapper<Product, ProductDTO> {

    @Override
    public Product toEntity(final ProductDTO productDTO) {
        return new Product()
                .setProductId(productDTO.getProductId())
                .setProductName(productDTO.getProductName())
                .setPrice(productDTO.getPrice())
                .setAmount(productDTO.getAmount())
                .setPhoto(productDTO.getPhoto())
                .setYoutube(productDTO.getYoutube())
                .setDescription(productDTO.getDescription())
                .setCategoryFk(productDTO.getCategory());
    }

    @Override
    public ProductDTO toDto(final Product product) {
        return new ProductDTO()
                .setProductId(product.getProductId())
                .setProductName(product.getProductName())
                .setPrice(product.getPrice())
                .setAmount(product.getAmount())
                .setPhoto(product.getPhoto())
                .setYoutube(product.getYoutube())
                .setDescription(product.getDescription())
                .setCategory(product.getCategoryFk());
    }

    @Override
    public List<Product> toEntity(final List<ProductDTO> productDTOS) {
        return productDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> toDto(final List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
