package com.candyshop.islodycze.product;

import com.candyshop.islodycze.mappers.Mapper;
import com.candyshop.islodycze.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class ProductMapper extends Mapper<Product, ProductDTO> {

    @Override
    public Product toEntity(ProductDTO productDTO) {
        return new Product()
                .setProductId(productDTO.getProductId())
                .setProductName(productDTO.getProductName())
                .setPrice(productDTO.getPrice())
                .setAmount(productDTO.getAmount())
                .setPhoto(productDTO.getPhoto())
                .setYoutube(productDTO.getYoutube())
                .setDescription(productDTO.getDescription())
                .setCategory(productDTO.getCategory());
    }

    @Override
    public ProductDTO toDto(Product product) {
        return new ProductDTO()
                .setProductId(product.getProductId())
                .setProductName(product.getProductName())
                .setPrice(product.getPrice())
                .setAmount(product.getAmount())
                .setPhoto(product.getPhoto())
                .setYoutube(product.getYoutube())
                .setDescription(product.getDescription())
                .setCategory(product.getCategory());
    }

    @Override
    public List<Product> toEntity(List<ProductDTO> productDTOS) {
        return productDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> toDto(List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
