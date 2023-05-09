package basavets.controller;

import basavets.connectinglayer.ConnectingLayer;
import basavets.dto.LoginRequest;
import basavets.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final ConnectingLayer connectingLayer;

    public UserController(ConnectingLayer connectingLayer) {
        this.connectingLayer = connectingLayer;
    }

    @PostMapping(value = "login")
    public ResponseEntity<LoginResponse> getCurrentUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(connectingLayer.authenticationUser(loginRequest));
    }

    @GetMapping(value = "logout")
    public ResponseEntity<LoginResponse> logOut() {
        return ResponseEntity.ok(connectingLayer.logOutUser());
    }
}