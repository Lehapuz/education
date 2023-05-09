package basavets.controller;

import basavets.connectinglayer.ConnectingLayer;
import basavets.dto.ProductResponse;
import basavets.dto.RegistrationRequest;
import basavets.dto.RegistrationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class DefaultController {

    private final ConnectingLayer connectingLayer;

    public DefaultController(ConnectingLayer connectingLayer) {
        this.connectingLayer = connectingLayer;
    }

    @GetMapping(value = "/")
    public ResponseEntity<ProductResponse> getProducts() {
        return ResponseEntity.ok(connectingLayer.getAllProducts());
    }

    @PostMapping(value = "/")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(connectingLayer.registerUser(registrationRequest));
    }
}