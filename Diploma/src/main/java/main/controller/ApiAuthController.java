package main.controller;

import main.api.request.RegistrationRequest;
import main.api.response.CaptchaResponse;
import main.api.response.CheckResponse;
import main.api.response.RegisterResponse;
import main.service.CaptchaService;
import main.service.CheckService;
import main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class ApiAuthController {

    private final CheckService checkService;
    private final CaptchaService captchaService;
    private final UserService userService;


    public ApiAuthController(CheckService checkService, CaptchaService captchaService, UserService userService) {
        this.checkService = checkService;
        this.captchaService = captchaService;
        this.userService = userService;
    }

    @GetMapping("auth/check")
    public ResponseEntity<CheckResponse> checkResponse(){
        return ResponseEntity.ok(checkService.getCheck());
    }


    @GetMapping("auth/captcha")
    public ResponseEntity<CaptchaResponse> captchaResponse(){
        return ResponseEntity.ok(captchaService.getCaptchaResponse());
    }


    @PostMapping("auth/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.registerNewUser(registrationRequest));
    }
}
