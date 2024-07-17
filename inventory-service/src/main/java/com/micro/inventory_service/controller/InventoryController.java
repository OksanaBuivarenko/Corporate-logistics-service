package com.micro.inventory_service.controller;

import com.micro.inventory_service.dto.ProductRq;
import com.micro.inventory_service.dto.ProductRs;
import com.micro.inventory_service.service.InventoryService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory-service/api/v1")
@OpenAPIDefinition(info =
@Info(title = "Inventory API", version = "1.0", description = "Documentation Inventory API v1.0")
)
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/product")
    public List<ProductRs> getAllProducts() {
        return inventoryService.getAllProducts();
    }

    @GetMapping("/product/{productId}")
    public ProductRs getProductById(@PathVariable Long productId) {
        return inventoryService.getProductRsById(productId);
    }

    @GetMapping("/product/name/{productName}")
    public ProductRs getProductByName(@PathVariable String productName) {
        return inventoryService.getProductRsByName(productName);
    }

    @PostMapping("/product")
    public ProductRs addProduct(@RequestBody ProductRq productRq) {
        return inventoryService.addProduct(productRq);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        inventoryService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
