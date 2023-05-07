package basavets.controller;

import basavets.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DefaultController {

    private final UserRepository userRepository;

    public DefaultController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/")
    public String index() {
    System.out.println(userRepository.findUserByEmail("lesha@mail.ru").get().getName());
        return userRepository.findUserByEmail("lesha@mail.ru").get().getName() ;
    }
}
