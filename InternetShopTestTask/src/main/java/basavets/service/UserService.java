package basavets.service;

import basavets.dto.LoginRequest;
import basavets.dto.LoginResponse;
import basavets.dto.RegistrationRequest;
import basavets.dto.RegistrationResponse;

public interface UserService {

    RegistrationResponse registerUser(RegistrationRequest registrationRequest);

    LoginResponse authenticationUser(LoginRequest loginRequest);

    LoginResponse logOutUser();
}
