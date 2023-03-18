package basavets.service;

import basavets.bean.AdminFriendsLocation;
import basavets.bean.Location;
import basavets.bean.User;
import basavets.dao.AdminFriendsLocationDAO;
import basavets.dao.LocationDAO;
import basavets.dao.UserDAO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserDAO userDAO = new UserDAO();
    private LocationDAO locationDAO = new LocationDAO();
    private AdminFriendsLocationDAO adminFriendsLocationDAO = new AdminFriendsLocationDAO();

    public UserService() {
    }

    public UserService(UserDAO userDAO, LocationDAO locationDAO, AdminFriendsLocationDAO adminFriendsLocationDAO) {
        this.userDAO = userDAO;
        this.locationDAO = locationDAO;
        this.adminFriendsLocationDAO = adminFriendsLocationDAO;
    }

    public List<User> getAllUsers() {
        return userDAO.getUsers();
    }

    public Optional<User> findUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public void saveUser(User user) {
        userDAO.addUser(user);
    }

    public List<Location> getAllLocations() {
        return locationDAO.getLocations();
    }

    public void saveLocation(Location location) {
        locationDAO.addLocation(location);
    }

    public void saveCurrentLocation(User user, Location location) {
        userDAO.saveCurrentLocation(user, location);
    }

    public void deleteCurrentLocation(User user) {
        userDAO.deleteCurrentLocation(user);
    }

    public Optional<Location> findLocationByName(String name) {
        return locationDAO.getLocationByName(name);
    }

    public void addUserOnLocation(Location location, User user) {
        locationDAO.addUserOnLocation(location, user);
    }

    public void deleteUserFromLocation(Location location, User user) {
        locationDAO.deleteUserFromLocation(location, user);
    }

    public boolean emailIsPresent(String email) {
        return findUserByEmail(email).isPresent();
    }

    public boolean nameLocationIsPresent(String name) {
        return findLocationByName(name).isPresent();
    }

    public List<Location> getUserLocation(User currentUser) {
        List<Location> locations = getAllLocations();
        List<Location> myLocations = new ArrayList<>();
        for (Location location : locations) {
            if (currentUser.getEmail().equals(location.getUser().getEmail())) {
                myLocations.add(location);
            }
        }
        return myLocations;
    }

    public List<User> getUsersWithoutCurrent(User user) {
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

    public List<User> getUsersWithLocation(User user) {
        List<User> users = getAllUsers();
        List<User> usersWithLocation = new ArrayList<>();
        for (User user1 : users) {
            if (user1.getLocation() != null && !user.getEmail().equals(user1.getEmail())) {
                usersWithLocation.add(user1);
            }
        }
        return usersWithLocation;
    }

    public List<AdminFriendsLocation> getAllAdminFriendsLocationList() {
        return adminFriendsLocationDAO.getAllAdminLocations();
    }

    public void saveAdminFriendsLocation(AdminFriendsLocation adminFriendsLocation) {
        adminFriendsLocationDAO.addAdminFriendLocation(adminFriendsLocation);
    }

    public List<AdminFriendsLocation> getMyAdminAccessLocationList(User user) {
        return getAllAdminFriendsLocationList().stream().filter(adminFriendsLocation -> adminFriendsLocation
                .getUserEmail().equals(user.getEmail())).collect(Collectors.toList());
    }

    public List<Location> getLocationsWithUsers() {
        List<Location> locationList = getAllLocations();
        List<Location> locationsWithUsers = new ArrayList<>();
        for (Location location : locationList) {
            if (location.getUsersInLocation() != null) {
                locationsWithUsers.add(location);
            }
        }
        return locationsWithUsers;
    }

    public List<User> getUsersOnLocation(String name) {
        Optional<Location> location = findLocationByName(name);
        List<User> userList = new ArrayList<>();
        for (User user : location.get().getUsersInLocation()) {
            if (findUserByEmail(user.getEmail()).isPresent()) {
                Optional<User> user1 = findUserByEmail(user.getEmail());
                userList.add(user1.get());
            }
        }
        return userList;
    }
}