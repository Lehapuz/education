package main.controller;

import main.api.request.LoginRequest;
import main.api.request.RegistrationRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.RegisterResponse;
import main.service.CaptchaService;
import main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final UserService userService;


    public ApiAuthController(CaptchaService captchaService, UserService userService) {
        this.captchaService = captchaService;
        this.userService = userService;
    }


    @GetMapping("auth/check")
    public ResponseEntity<LoginResponse> check(Principal principal) {
        return ResponseEntity.ok(userService.checkUser(principal));
    }


    @GetMapping("auth/captcha")
    public ResponseEntity<CaptchaResponse> captchaResponse() {
        return ResponseEntity.ok(captchaService.getCaptchaResponse());
    }


    @PostMapping("auth/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.registerNewUser(registrationRequest));
    }


    @PostMapping("auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginResponse(loginRequest));
    }


    @GetMapping("auth/logout")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<LoginResponse> logout() {
        return ResponseEntity.ok(userService.logout());
    }
}
