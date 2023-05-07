package basavets.controller;

import basavets.dto.LoginRequest;
import basavets.dto.LoginResponse;
import basavets.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "init")
    public ResponseEntity<LoginResponse> getCurrentUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.authenticationUser(loginRequest));
    }
}
