package basavets.connectinglayer;

import basavets.dto.*;
import basavets.service.ProductService;
import basavets.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class ConnectingLayer {

    private final UserService userService;
    private final ProductService productService;


    public ConnectingLayer(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        return userService.registerUser(registrationRequest);
    }

    public LoginResponse authenticationUser(LoginRequest loginRequest) {
        return userService.authenticationUser(loginRequest);
    }

    public LoginResponse logOutUser() {
        return userService.logOutUser();
    }

    public ProductResponse getAllProducts() {
        return productService.getAllProducts();
    }

    public ProductOrderResponse orderProduct(ProductOrderRequest productOrderRequest, int productId) {
        return productService.orderProduct(productOrderRequest, productId);
    }

    public ProductSetResponse setProduct(ProductSetRequest productSetRequest) {
        return productService.setProduct(productSetRequest);
    }

    public ChangeProductStatusResponse changeStatus(int purchaseId) {
        return productService.changeStatus(purchaseId);
    }
}
