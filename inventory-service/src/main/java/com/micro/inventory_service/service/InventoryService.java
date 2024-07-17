package com.micro.inventory_service.service;

import com.micro.inventory_service.dto.ProductRq;
import com.micro.inventory_service.dto.ProductRs;
import com.micro.inventory_service.entity.Product;
import com.micro.starter.dto.ProductKafkaDTO;

import java.util.List;
import java.util.Map;

public interface InventoryService {

    List<ProductRs> getAllProducts();

    ProductRs getProductRsById(Long productId);

    Product getProductById(Long productId);

    Product getProductByName(String productName);

    ProductRs getProductRsByName(String productName);

    ProductRs addProduct(ProductRq productRq);

    void deleteProduct(Long productId);

    Map<Product, Integer> getProductList(List<ProductKafkaDTO> productList);

    void decreaseProduct(Map<Product, Integer> productList);

    void increaseProduct(Map<Product, Integer> productList);
}