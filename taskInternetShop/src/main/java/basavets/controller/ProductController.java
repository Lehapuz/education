package basavets.controller;

import basavets.dto.*;
import basavets.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "order/{productId}")
    public ResponseEntity<ProductOrderResponse> orderProduct(@RequestBody ProductOrderRequest productOrderRequest
            , @PathVariable Integer productId) {
        return ResponseEntity.ok(productService.orderProduct(productOrderRequest, productId));
    }

    @PostMapping(value = "set")
    public ResponseEntity<ProductSetResponse> setProduct(@RequestBody ProductSetRequest productSetRequest) {
        return ResponseEntity.ok(productService.setProduct(productSetRequest));
    }

    @GetMapping(value = "status/{purchaseId}")
    public ResponseEntity<ChangeProductStatusResponse> changeStatus(@PathVariable Integer purchaseId) {
        return ResponseEntity.ok(productService.changeStatus(purchaseId));
    }
}
