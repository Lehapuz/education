package basavets.controller;

import basavets.bean.Access;
import basavets.bean.Location;
import basavets.bean.User;
import basavets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@SessionAttributes(value = "user")
public class DefaultController {

    @Autowired
    private final UserService userService;
    private final String ADMIN_ACCESS = "ADMIN_ACCESS";
    private final String ACCESS = "ACCESS";


    public DefaultController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("index");
        return modelAndView;
    }


    @PostMapping("/")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (user.getName().isEmpty()) {
            model.addAttribute("registration", "Имя не должно быть пустым");
            return "index";
        } else if (user.getEmail().isEmpty()) {
            model.addAttribute("registration", "Адрес элктронной почты должен быть заполнен");
            return "index";
        } else if (userService.emailIsPresent(user.getEmail())) {
            model.addAttribute("registration", "Такой Емайл уже зарегистрирован");
            return "index";
        } else {
            model.addAttribute("registration", "Регистрация прошла успешно");
            userService.saveUser(user);
            return "index";
        }
    }


    @PostMapping("/authorizationUser")
    public ModelAndView authorizationUser(@ModelAttribute("user") User user, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (user.getEmail().isEmpty()) {
            model.addAttribute("authorization", "Адрес элктронной почты должен быть заполнен");
            modelAndView.setViewName("index");
        } else if (userService.findUserByEmail(user.getEmail()).isEmpty()) {
            model.addAttribute("authorization", "Такой адрес электронной почты отсутсвует");
            modelAndView.setViewName("index");
        } else {
            Optional<User> currentUser = userService.findUserByEmail(user.getEmail());
            model.addAttribute("user", currentUser.get());
            modelAndView.setViewName("userIndex");
            modelAndView.addObject(currentUser);

            model.addAttribute("locations", userService.getUserLocation(currentUser.get()));

            if (currentUser.get().getLocation() != null) {
                model.addAttribute("myLocation", currentUser.get().getLocation().getName());
            } else {
                model.addAttribute("myLocation", "Локация не установлена");
            }
        }
        return modelAndView;
    }


    @GetMapping("/authorizationUser")
    public ModelAndView getAuthorizationUser(@ModelAttribute User user, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            Optional<User> currentUser = userService.findUserByEmail(user.getEmail());
            model.addAttribute("user", currentUser.get());
            modelAndView.setViewName("userIndex");
            modelAndView.addObject(currentUser);

            model.addAttribute("locations", userService.getUserLocation(currentUser.get()));

            if (currentUser.get().getLocation() != null) {
                model.addAttribute("myLocation", currentUser.get().getLocation().getName());
            } else {
                model.addAttribute("myLocation", "Локация не установлена");
            }
        } catch (Exception e) {
            modelAndView.setViewName("errorPageIndex");
        }
        return modelAndView;
    }


    @GetMapping("/friends")
    public ModelAndView lookFriends(@ModelAttribute User user, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            model.addAttribute("usersWithLocation", userService.getUsersWithLocation(user));
            model.addAttribute("users", userService.getUsersWithoutCurrent(user));

            modelAndView.setViewName("friendsIndex");
            modelAndView.addObject(user);
        } catch (Exception e) {
            modelAndView.setViewName("errorPageIndex");
        }
        return modelAndView;
    }


    @PostMapping("/addLocation")
    public ModelAndView addPlace(@ModelAttribute User user, @ModelAttribute Location location, Model model,
                                 @RequestParam(name = "status", required = false) Access access) {
        ModelAndView modelAndView = new ModelAndView();
        if (location.getName().isEmpty()) {
            model.addAttribute("add", "Название локации должно быть заполнено");
            modelAndView.setViewName("userIndex");
        } else if (location.getAddress().isEmpty()) {
            model.addAttribute("add", "Адрес должен быть указан");
            modelAndView.setViewName("userIndex");
        } else if (userService.nameLocationIsPresent(location.getName())) {
            model.addAttribute("add", "Такое название локации уже есть в системе");
            modelAndView.setViewName("userIndex");
        } else {
            location.setUser(user);
            switch (String.valueOf(access)) {
                case ADMIN_ACCESS -> location.setAccess(Access.ADMIN_ACCESS);
                case ACCESS -> location.setAccess(Access.ACCESS);
            }
            userService.saveLocation(location);
            modelAndView.setViewName("userCompleteIndex");
        }
        return modelAndView;
    }


    @PostMapping("/addCurrentLocation")
    public ModelAndView addCurrentLocation(@ModelAttribute User user, @RequestParam(name = "name", required = false)
            String locationName, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Location location;
        List<Location> locations = userService.getAllLocations();

        if (locationName.isEmpty()) {
            model.addAttribute("addCurrent", "Заполните поле");
        } else {
            for (Location location1 : locations) {
                if (locationName.equals(location1.getName())) {
                    location = location1;
                    if (location.getUser().getEmail().equals(user.getEmail())) {
                        userService.saveCurrentLocation(user, location);
                        model.addAttribute("addCurrent", "Локация установлена");
                    } else {
                        model.addAttribute("addCurrent", "У вас нет такой локации");
                    }
                }
            }
        }
        modelAndView.setViewName("userCompleteIndex");
        return modelAndView;
    }


    @GetMapping("/deleteLocation")
    public ModelAndView deleteLocation(@ModelAttribute User user) {
        ModelAndView modelAndView = new ModelAndView();
        if (user.getLocation() != null) {
            userService.deleteCurrentLocation(user);
        }
        modelAndView.setViewName("userCompleteIndex");
        return modelAndView;
    }


    @GetMapping("/publicLocations")
    public ModelAndView getPublicLocations(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Location location1 = new Location();
        modelAndView.addObject("location", location1);
        List<Location> locations = userService.getAllLocations();
        List<Location> publicLocations = new ArrayList<>();
        for (Location location : locations) {
            if (location.getAccess().toString().equals(ADMIN_ACCESS)) {
                publicLocations.add(location);
            }
        }
        model.addAttribute("locations", publicLocations);
        modelAndView.setViewName("publicLocationsIndex");
        return modelAndView;
    }


    @GetMapping("/publicLocations/{name}")
    public ModelAndView getUserOnLocation(@PathVariable(value = "name") String name, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Location> location = userService.findLocationByName(name);
        Optional<User> currentUser = userService.findUserByEmail(location.get().getUser().getEmail());
        List<User> users = new ArrayList<>();
        List<User> allUsers = userService.getAllUsers();
        List<User> usersNotOnLocation = new ArrayList<>();
        if (location.get().getUsersInLocation() == null) {
            model.addAttribute("noUsers", "Нет пользователей на локации");
        } else {
            for (User user : location.get().getUsersInLocation()) {
                Optional<User> newUser;
                newUser = userService.findUserByEmail(user.getEmail());
                users.add(newUser.get());
                model.addAttribute("users", users);
            }
        }
        model.addAttribute("userName", currentUser.get().getName());
        model.addAttribute("name", location.get().getName());
        model.addAttribute("address", location.get().getAddress());

        for (User user : allUsers) {
            if (!user.getEmail().equals(currentUser.get().getEmail()) && location.get().getUsersInLocation() == null) {
                usersNotOnLocation.add(user);
            } else {
                if (users.stream().noneMatch(u -> u.getEmail().equals(user.getEmail()))
                        && !user.getEmail().equals(currentUser.get().getEmail())) {
                    usersNotOnLocation.add(user);
                }
            }
        }
        model.addAttribute("usersNotOnLocation", usersNotOnLocation);
        modelAndView.setViewName("publicLocationIndex");
        return modelAndView;
    }


    @GetMapping("/publicLocations/{nameLoc}/{email}")
    public ModelAndView addUserOnLocation(@PathVariable(value = "nameLoc") String name,
                                          @PathVariable(value = "email") String email) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<User> user = userService.findUserByEmail(email);
        Optional<Location> location = userService.findLocationByName(name);
        userService.addUserOnLocation(location.get(), user.get());
        modelAndView.setViewName("addUserOnLocationIndex");
        return modelAndView;
    }


    @GetMapping("/publicLocations/delete/{nameLoc}/{email}")
    public ModelAndView deleteUserFromLocation(@PathVariable(value = "nameLoc") String name,
                                               @PathVariable(value = "email") String email) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<User> user = userService.findUserByEmail(email);
        Optional<Location> location = userService.findLocationByName(name);
        userService.deleteUserFromLocation(location.get(), user.get());
        modelAndView.setViewName("deleteUserFromLocationIndex");
        return modelAndView;
    }
}
