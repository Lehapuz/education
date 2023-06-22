package basavets.controller;

import basavets.dto.ProductResponse;
import basavets.dto.RegistrationRequest;
import basavets.dto.RegistrationResponse;
import basavets.service.ProductService;
import basavets.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class DefaultController {

    private final UserService userService;
    private final ProductService productService;

    public DefaultController(UserController userController, UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<ProductResponse> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping(value = "/")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.registerUser(registrationRequest));
    }
}