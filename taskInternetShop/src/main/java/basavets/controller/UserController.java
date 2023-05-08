package basavets.controller;

import basavets.dto.LoginRequest;
import basavets.dto.LoginResponse;
import basavets.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "login")
    public ResponseEntity<LoginResponse> getCurrentUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.authenticationUser(loginRequest));
    }

    @GetMapping(value = "logout")
    public ResponseEntity<LoginResponse> logOut() {
        return ResponseEntity.ok(userService.logOutUser());
    }
}
