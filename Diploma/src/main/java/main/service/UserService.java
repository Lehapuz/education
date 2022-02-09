package main.service;

import main.api.request.RegistrationRequest;
import main.api.response.RegisterErrorResponse;
import main.api.response.RegisterResponse;
import main.model.CaptchaCode;
import main.model.User;
import main.repositories.CaptchaCodeRepository;
import main.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final CaptchaCodeRepository captchaCodeRepository;

    public UserService(UserRepository userRepository, CaptchaCodeRepository captchaCodeRepository) {
        this.userRepository = userRepository;
        this.captchaCodeRepository = captchaCodeRepository;
    }

    public RegisterResponse registerNewUser(RegistrationRequest registrationRequest) {

        RegisterResponse registerResponse = new RegisterResponse();
        RegisterErrorResponse registerErrorResponse = new RegisterErrorResponse();
        CaptchaCode captchaCode = new CaptchaCode();

        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setName(registrationRequest.getName());
        user.setPassword(registrationRequest.getPassword());
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

        if (captchaCodeRepository.findBySecretCode(captchaCode.getSecretCode()).isEmpty()) {
            registerErrorResponse.setCaptcha("Код с картинки введен неверно");
            registerResponse.setResult(false);
            registerResponse.setErrors(registerErrorResponse);
        }

        if (registerResponse.isResult()) {
            userRepository.save(user);
        }
        return registerResponse;
    }
}
