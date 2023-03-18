package basavets.dao;

import basavets.bean.AdminFriendsLocation;
import basavets.bean.Location;
import basavets.bean.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {


    public static List<User> readFileUser(String path) {
        List<String> lines = new ArrayList<>();
        List<User> users = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : lines) {
            User user = new User();
            String[] fragment = line.split("\\,");
            user.setName(fragment[0]);
            user.setEmail(fragment[1]);
            if (fragment.length == 3) {
                Location currentLocation = new Location();
                currentLocation.setName(fragment[2]);
                user.setLocation(currentLocation);
            }
            users.add(user);
        }
        return users;
    }


    public static void writeFileUser(String path, List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            String line;
            if (user.getLocation() != null) {
                line = user.getName() + "," + user.getEmail() + "," + user.getLocation().getName();
            } else {
                line = user.getName() + "," + user.getEmail();
            }
            lines.add(line);
        }
        try {
            Files.write(Paths.get(path), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Location> readFileLocation(String path) {
        List<String> lines = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : lines) {
            Location location = new Location();
            String[] fragment = line.split("\\,");
            location.setName(fragment[0]);
            location.setAddress(fragment[1]);
            User user = new User();
            user.setEmail(fragment[2]);
            location.setUser(user);
            if (fragment.length > 3) {
                List<User> usersInLocation = new ArrayList<>();
                for (int i = 3; i < fragment.length; i++) {
                    User userInLocation = new User();
                    userInLocation.setEmail(fragment[i]);
                    usersInLocation.add(userInLocation);
                }
                location.setUsersInLocation(usersInLocation);
            }
            locations.add(location);
        }
        return locations;
    }


    public static void writeFileLocation(String path, List<Location> locations) {
        List<String> lines = new ArrayList<>();
        for (Location location : locations) {
            String line;
            if (location.getUsersInLocation() != null) {
                StringBuilder str = new StringBuilder();
                location.getUsersInLocation().forEach(user -> str.append(user.getEmail()).append(","));
                line = location.getName() + "," + location.getAddress() + "," + location.getUser().getEmail()
                        + "," + str;
                lines.add(line.substring(0, line.length() - 1));
            } else {
                line = location.getName() + "," + location.getAddress() + "," + location.getUser().getEmail();
                lines.add(line);
            }
        }
        try {
            Files.write(Paths.get(path), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<AdminFriendsLocation> readFileAdminFriendLocation(String path) {
        List<String> lines = new ArrayList<>();
        List<AdminFriendsLocation> adminFriendsLocations = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : lines) {
            AdminFriendsLocation adminFriendsLocation = new AdminFriendsLocation();
            String[] fragment = line.split("\\,");
            adminFriendsLocation.setUserEmail(fragment[0]);
            adminFriendsLocation.setLocationName(fragment[1]);
            adminFriendsLocations.add(adminFriendsLocation);
        }
        return adminFriendsLocations;
    }


    public static void writeFileAdminFriendLocation(String path, List<AdminFriendsLocation> adminFriendsLocations) {
        List<String> lines = new ArrayList<>();
        for (AdminFriendsLocation adminFriendsLocation : adminFriendsLocations) {
            String line;
            line = adminFriendsLocation.getUserEmail() + "," + adminFriendsLocation.getLocationName();
            lines.add(line);
        }
        try {
            Files.write(Paths.get(path), lines);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
