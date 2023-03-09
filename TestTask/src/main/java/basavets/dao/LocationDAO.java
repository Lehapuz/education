package basavets.dao;

import basavets.bean.Location;
import basavets.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LocationDAO {

    private final String filePathLocation = "src/main/resources/location.csv";


    public void addLocation(Location location) {
        List<Location> locations = Storage.readFileLocation(filePathLocation);
        locations.add(location);
        Storage.writeFileLocation(filePathLocation, locations);
    }


    public List<Location> getLocations() {
        return Storage.readFileLocation(filePathLocation);
    }


    public Optional<Location> getLocationByName(String name) {
        List<Location> locations = Storage.readFileLocation(filePathLocation);
        return locations.stream().filter(location -> location.getName().equals(name)).findAny();
    }


    public void addUserOnLocation(Location location, User user) {
        List<Location> locations = Storage.readFileLocation(filePathLocation);
        List<Location> newLocations = new ArrayList<>();
        for (Location location1 : locations) {
            if (location1.getName().equals(location.getName())) {
                List<User> userList;
                if (location1.getUsersInLocation() != null) {
                    userList = location1.getUsersInLocation();
                } else {
                    userList = new ArrayList<>();
                }
                userList.add(user);
                location1.setUsersInLocation(userList);
            }
            newLocations.add(location1);
        }
        Storage.writeFileLocation(filePathLocation, newLocations);
    }


    public void deleteUserFromLocation(Location location, User user) {
        List<Location> locations = Storage.readFileLocation(filePathLocation);
        List<Location> newLocations = new ArrayList<>();
        for (Location location1 : locations) {
            if (location1.getName().equals(location.getName())) {
                System.out.println("DELETE");
                List<User> userList = location1.getUsersInLocation();
                System.out.println("DELETE");
                location1.setUsersInLocation(userList.stream().filter(u -> !u.getEmail()
                        .equals(user.getEmail())).collect(Collectors.toList()));
            }
            newLocations.add(location1);
        }
        Storage.writeFileLocation(filePathLocation, newLocations);
    }
}
