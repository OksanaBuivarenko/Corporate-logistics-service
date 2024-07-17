package com.micro.inventory_service.service.impl;

import com.micro.inventory_service.dto.ProductRq;
import com.micro.inventory_service.dto.ProductRs;
import com.micro.inventory_service.entity.Product;
import com.micro.inventory_service.exception.NotEnoughProductException;
import com.micro.inventory_service.exception.ProductNotFoundException;
import com.micro.inventory_service.mapper.ProductMapper;
import com.micro.inventory_service.repository.ProductRepository;
import com.micro.inventory_service.service.InventoryService;
import com.micro.starter.dto.ProductKafkaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductRs> getAllProducts() {
        return productRepository.findAll().stream().map(ProductMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductRs getProductRsById(Long productId) {
        return ProductMapper.INSTANCE.toDto(getProductById(productId));
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(()
                -> new ProductNotFoundException(productId));
    }

    @Override
    public Product getProductByName(String productName) {
        return productRepository.findByName(productName).orElseThrow(()
                -> new ProductNotFoundException(productName));
    }

    @Override
    public ProductRs getProductRsByName(String productName) {
        return ProductMapper.INSTANCE.toDto(getProductByName(productName));
    }

    @Override
    public ProductRs addProduct(ProductRq productRq) {
        Product createProduct;
        Product product = ProductMapper.INSTANCE.toEntity(productRq);
        Optional<Product> savedProduct = productRepository.findByName(product.getName());
        if (savedProduct.isPresent()) {
            savedProduct.get().setCount(savedProduct.get().getCount() + product.getCount());
            createProduct = productRepository.save(savedProduct.get());
        } else {
            createProduct = productRepository.save(product);
        }
        return ProductMapper.INSTANCE.toDto(createProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.delete(getProductById(productId));
    }

    @Override
    public Map<Product, Integer> getProductList(List<ProductKafkaDTO> productList) {
        Map<Product, Integer> products = new HashMap<>();
        StringBuilder isCurrentPrice = new StringBuilder();
        StringBuilder isEnoughProduct = new StringBuilder();
        for (ProductKafkaDTO productDTO : productList) {
            Product product = getProductByName(productDTO.getName());

            if (!Objects.equals(product.getPrice(), productDTO.getPrice())) {
                isCurrentPrice.append(" Цена товара ");
                isCurrentPrice.append(productDTO.getName());
                isCurrentPrice.append(" изменилась");
            }
            if (product.getCount() < productDTO.getCount()) {
                isEnoughProduct.append(" В наличии на складе только ");
                isEnoughProduct.append(product.getCount());
                isEnoughProduct.append(" ");
                isEnoughProduct.append(product.getName());
                isEnoughProduct.append(System.lineSeparator());
            }
            products.put(product, productDTO.getCount());
        }
        if (!isEnoughProduct.isEmpty()) {
            throw new NotEnoughProductException(isEnoughProduct.toString());
        }
        if (!isCurrentPrice.isEmpty()) {
            throw new NotEnoughProductException(isCurrentPrice.toString());
        }
        return products;
    }

    @Override
    @Transactional
    public void decreaseProduct(Map<Product, Integer> products) {
        for (Product product : products.keySet()) {
            product.setCount(product.getCount() - products.get(product));
            productRepository.save(product);
        }
    }

    @Override
    public void increaseProduct(Map<Product, Integer> products) {
        for (Product product : products.keySet()) {
            product.setCount(product.getCount() + products.get(product));
            productRepository.save(product);
        }
    }
}
