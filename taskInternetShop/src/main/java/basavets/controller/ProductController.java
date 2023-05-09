package basavets.controller;

import basavets.connectinglayer.ConnectingLayer;
import basavets.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ProductController {

    private final ConnectingLayer connectingLayer;

    public ProductController(ConnectingLayer connectingLayer) {
        this.connectingLayer = connectingLayer;
    }

    @PostMapping(value = "order/{productId}")
    public ResponseEntity<ProductOrderResponse> orderProduct(@RequestBody ProductOrderRequest productOrderRequest
            , @PathVariable Integer productId) {
        return ResponseEntity.ok(connectingLayer.orderProduct(productOrderRequest, productId));
    }

    @PostMapping(value = "set")
    public ResponseEntity<ProductSetResponse> setProduct(@RequestBody ProductSetRequest productSetRequest) {
        return ResponseEntity.ok(connectingLayer.setProduct(productSetRequest));
    }

    @GetMapping(value = "status/{purchaseId}")
    public ResponseEntity<ChangeProductStatusResponse> changeStatus(@PathVariable Integer purchaseId) {
        return ResponseEntity.ok(connectingLayer.changeStatus(purchaseId));
    }
}