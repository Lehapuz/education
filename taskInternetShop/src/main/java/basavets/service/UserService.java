package basavets.service;

import basavets.beans.User;
import basavets.dto.*;
import basavets.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        RegistrationResponse registrationResponse = new RegistrationResponse();
        RegisterErrorResponse registerErrorResponse = new RegisterErrorResponse();
        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setName(registrationRequest.getName());
        user.setPassword(registrationRequest.getPassword());
        user.setIsModerator(0);

        if (registrationRequest.getName().isBlank()) {
            registerErrorResponse.setName("Имя не должно быть пустым");
            registrationResponse.setResult(false);
            registrationResponse.setRegisterErrorResponse(registerErrorResponse);
        } else if (registrationRequest.getPassword().length() < 6) {
            registerErrorResponse.setPassword("Пароль меньше шести символов");
            registrationResponse.setResult(false);
            registrationResponse.setRegisterErrorResponse(registerErrorResponse);
        } else if (userRepository.findUserByEmail(registrationRequest.getEmail()).isPresent()) {
            registerErrorResponse.setEmail("Такой адрес электронной почты уже зарегистрирован");
            registrationResponse.setResult(false);
            registrationResponse.setRegisterErrorResponse(registerErrorResponse);
        } else {
            userRepository.save(user);
            registrationResponse.setResult(true);
        }
        return registrationResponse;
    }

    public LoginResponse authenticationUser(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        UserLoginResponse userLoginResponse = new UserLoginResponse();

        Optional<User> currentUser = userRepository.findUserByEmail(loginRequest.getEmail());

        if (currentUser.isEmpty()) {
            loginResponse.setResult(false);
        } else {
            userLoginResponse.setEmail(loginRequest.getEmail());
            if (currentUser.get().getPassword().equals(loginRequest.getPassword())) {
                userLoginResponse.setPassword(loginRequest.getPassword());
                loginResponse.setUserLoginResponse(userLoginResponse);
                loginResponse.setResult(true);
            }
            else {
                loginResponse.setResult(false);
            }
        }
        return loginResponse;
    }
}
