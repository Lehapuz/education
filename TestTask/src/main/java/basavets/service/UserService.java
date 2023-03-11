package basavets.service;

import basavets.bean.Location;
import basavets.bean.User;
import basavets.dao.LocationDAO;
import basavets.dao.UserDAO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserDAO userDAO = new UserDAO();
    private LocationDAO locationDAO = new LocationDAO();

    public UserService(){}

    public UserService(UserDAO userDAO, LocationDAO locationDAO){
        this.userDAO = userDAO;
        this.locationDAO = locationDAO;
    }

    public List<User> getAllUsers(){
        return userDAO.getUsers();
    }

    public Optional <User> findUserByEmail(String email){
        return userDAO.getUserByEmail(email);
    }

    public void saveUser(User user){
        userDAO.addUser(user);
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

    public boolean emailIsPresent (String email){
        return findUserByEmail(email).isPresent();
    }

    public boolean nameLocationIsPresent (String name){
        return findLocationByName(name).isPresent();
    }

    public List<Location> getUserLocation(User currentUser){
        List<Location> locations = getAllLocations();
        List<Location> myLocations = new ArrayList<>();
        for (Location location : locations) {
            if (currentUser.getEmail().equals(location.getUser().getEmail())) {
                myLocations.add(location);
            }
        }
        return myLocations;
    }

    public List<User> getUsersWithoutCurrent (User user){
        List<User> users = getAllUsers();
        List<User> usersWithoutCurrent = new ArrayList<>();
        for (User user1 : users) {
            if (user.getEmail().equals(user1.getEmail())) {
                continue;
            }
            usersWithoutCurrent.add(user1);
        }
        return usersWithoutCurrent;
    }

    public List<User> getUsersWithLocation (User user){
        List<User> users = getAllUsers();
        List<User> usersWithLocation = new ArrayList<>();
        for (User user1 : users) {
            if (user1.getLocation() != null && !user.getEmail().equals(user1.getEmail())) {
                usersWithLocation.add(user1);
            }
        }
        return usersWithLocation;
    }
}

