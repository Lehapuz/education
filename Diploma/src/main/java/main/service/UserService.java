package main.service;

import main.api.request.LoginRequest;
import main.api.request.RegistrationRequest;
import main.api.response.LoginResponse;
import main.api.response.RegisterErrorResponse;
import main.api.response.RegisterResponse;
import main.api.response.UserLoginResponse;
import main.model.CaptchaCode;
import main.model.ModerationStatus;
import main.model.User;
import main.repositories.CaptchaCodeRepository;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CaptchaCodeRepository captchaCodeRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepository userRepository, PostRepository postRepository, CaptchaCodeRepository captchaCodeRepository,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.captchaCodeRepository = captchaCodeRepository;
        this.authenticationManager = authenticationManager;
    }

    public RegisterResponse registerNewUser(RegistrationRequest registrationRequest) {

        RegisterResponse registerResponse = new RegisterResponse();
        RegisterErrorResponse registerErrorResponse = new RegisterErrorResponse();
        CaptchaCode captchaCode = new CaptchaCode();

        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setName(registrationRequest.getName());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setIsModerator(0);
        user.setRegTime(LocalDateTime.now());
        captchaCode.setCode(registrationRequest.getCaptcha());
        captchaCode.setSecretCode(registrationRequest.getCaptchaSecret());

        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            registerErrorResponse.setEmail("Этот e-mail уже зарегистрирован");
            registerResponse.setResult(false);
            registerResponse.setErrors(registerErrorResponse);
        }
        if (registrationRequest.getName().isBlank()) {
            registerErrorResponse.setName("Имя указано неверно");
            registerResponse.setResult(false);
            registerResponse.setErrors(registerErrorResponse);
        }
        if (registrationRequest.getPassword().length() < 6) {
            registerErrorResponse.setPassword("Пароль короче 6-ти символов");
            registerResponse.setResult(false);
            registerResponse.setErrors(registerErrorResponse);
        }
        if (!registrationRequest.getCaptcha().equals(captchaCodeRepository
                .findBySecretCode(registrationRequest.getCaptchaSecret()).get().getCode())) {
            registerErrorResponse.setCaptcha("Код с картинки введен неверно");
            registerResponse.setResult(false);
            registerResponse.setErrors(registerErrorResponse);
        }
        if (registerResponse.isResult()) {
            userRepository.save(user);
        }
        return registerResponse;
    }


    public LoginResponse loginResponse(LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);

            org.springframework.security.core.userdetails.User user
                    = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

            User currentUser = userRepository.findByEmail(user.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));

            LoginResponse loginResponse = new LoginResponse();

            UserLoginResponse userLoginResponse = new UserLoginResponse();
            userLoginResponse.setEmail(currentUser.getEmail());
            userLoginResponse.setModeration(currentUser.getIsModerator() == 1);
            userLoginResponse.setId(currentUser.getId());
            userLoginResponse.setName(currentUser.getName());
            userLoginResponse.setPhoto(currentUser.getPhoto());
            userLoginResponse.setModerationCount(moderationPostCount(userLoginResponse.isModeration()));
            userLoginResponse.setSettings(userLoginResponse.isModeration());

            loginResponse.setResult(true);
            loginResponse.setUser(userLoginResponse);

            return loginResponse;

        } catch (Exception e) {
            return new LoginResponse();
        }
    }


    private int moderationPostCount(boolean isModerator) {
        if (!isModerator) return 0;
        return (int) postRepository
                .findPostForModeration(ModerationStatus.NEW, PageRequest.of(0, 10))
                .getTotalElements();
    }


    public LoginResponse logout() {
        LoginResponse response = new LoginResponse();
        response.setResult(true);
        return response;
    }


    public LoginResponse checkUser(Principal principal) {
        LoginResponse response = new LoginResponse();

        if (principal == null) {
            return new LoginResponse();
        }
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setModeration(currentUser.getIsModerator() == 1);
        userLoginResponse.setId(currentUser.getId());
        userLoginResponse.setName(currentUser.getName());
        userLoginResponse.setPhoto(currentUser.getPhoto());
        userLoginResponse.setModerationCount(moderationPostCount(userLoginResponse.isModeration()));
        userLoginResponse.setSettings(userLoginResponse.isModeration());

        response.setResult(true);
        response.setUser(userLoginResponse);

        return response;
    }
}
