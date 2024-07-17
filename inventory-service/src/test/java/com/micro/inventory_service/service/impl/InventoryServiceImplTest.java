package com.micro.inventory_service.service.impl;

import com.micro.inventory_service.entity.Product;
import com.micro.inventory_service.repository.ProductRepository;
import com.micro.starter.dto.ProductKafkaDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final InventoryServiceImpl inventoryService = new InventoryServiceImpl(productRepository);
    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Milk");
        product.setCount(2);
        product.setPrice(100);
    }

    @AfterEach
    void tearDown() {
        product = null;
    }

    @Test
    @DisplayName("Get product by id")
    void getProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
        var result = inventoryService.getProductById(1L);
        assertEquals("Milk", result.getName());
    }

    @Test
    @DisplayName("Get product by name")
    void getProductByName() {
        when(productRepository.findByName("Milk")).thenReturn(Optional.ofNullable(product));
        var result = inventoryService.getProductByName("Milk");
        assertEquals(1, result.getId());
    }

    @Test
    @DisplayName("Delete product")
    void deleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
        doNothing().when(productRepository).delete(product);
        inventoryService.deleteProduct(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    @DisplayName("Get product list")
    void getProductList() {
        ProductKafkaDTO productKafkaDTO = new ProductKafkaDTO();
        productKafkaDTO.setCount(1);
        productKafkaDTO.setName("Milk");
        productKafkaDTO.setPrice(100);
        List<ProductKafkaDTO> productList = new ArrayList<>();
        productList.add(productKafkaDTO);

        when(productRepository.findByName("Milk")).thenReturn(Optional.ofNullable(product));
        Map<Product, Integer> result = inventoryService.getProductList(productList);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Decrease product")
    void decreaseProduct() {
        Map<Product, Integer> products = new HashMap<>();
        products.put(product, 1);
        inventoryService.decreaseProduct(products);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Increase product")
    void increaseProduct() {
        Map<Product, Integer> products = new HashMap<>();
        products.put(product, 1);
        inventoryService.increaseProduct(products);
        verify(productRepository, times(1)).save(product);
    }
}