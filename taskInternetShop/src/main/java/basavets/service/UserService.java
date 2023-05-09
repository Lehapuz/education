package basavets.service;

import basavets.beans.User;
import basavets.dto.*;
import basavets.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        RegistrationResponse registrationResponse = new RegistrationResponse();
        try {
            RegisterErrorResponse registerErrorResponse = new RegisterErrorResponse();
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setName(registrationRequest.getName());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
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
        } catch (Exception e) {
            registrationResponse.setResult(false);
        }
        return registrationResponse;
    }


    public LoginResponse authenticationUser(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);

            org.springframework.security.core.userdetails.User user
                    = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

            Optional<User> currentUser = userRepository.findUserByEmail(user.getUsername());

            UserLoginResponse userLoginResponse = new UserLoginResponse();

            if (currentUser.isEmpty()) {
                loginResponse.setResult(false);
            } else {
                userLoginResponse.setEmail(loginRequest.getEmail());
                userLoginResponse.setPassword(loginRequest.getPassword());
                userLoginResponse.setId(currentUser.get().getId());
                userLoginResponse.setName(currentUser.get().getName());
                loginResponse.setUserLoginResponse(userLoginResponse);
                loginResponse.setResult(true);
            }
        } catch (Exception e) {
            loginResponse.setResult(false);
        }
        return loginResponse;
    }


    public LoginResponse logOutUser() {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        return loginResponse;
    }
}
