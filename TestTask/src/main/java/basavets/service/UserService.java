package basavets.service;

import basavets.bean.Location;
import basavets.bean.User;
import basavets.dao.LocationDAO;
import basavets.dao.UserDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDAO userDAO = new UserDAO();
    private final LocationDAO locationDAO = new LocationDAO();

    public List<User> getAllUsers(){
        return userDAO.getUsers();
    }

    public Optional <User> findUserByEmail(String email){
        return userDAO.getUserByEmail(email);
    }

    public void saveUser(User user){
        userDAO.addUser(user);
    }

    public Optional<User> getCurrentUserLocation (Location location){
        List<User> users = userDAO.getUsers();
        return users.stream().filter(user -> user.getLocation().getName()
                .equals(location.getName())).findAny();
    }

    public List<Location> getAllLocations(){
        return locationDAO.getLocations();
    }

    public void saveLocation(Location location){
        locationDAO.addLocation(location);
    }

    public void saveCurrentLocation(User user, Location location){
        userDAO.saveCurrentLocation(user, location);
    }

    public void deleteCurrentLocation(User user){
        userDAO.deleteCurrentLocation(user);
    }

    public Optional<Location> findLocationByName(String name){
        return locationDAO.getLocationByName(name);
    }

    public void addUserOnLocation(Location location, User user){
        locationDAO.addUserOnLocation(location, user);
    }

    public void deleteUserFromLocation(Location location, User user){
        locationDAO.deleteUserFromLocation(location, user);
    }
}

