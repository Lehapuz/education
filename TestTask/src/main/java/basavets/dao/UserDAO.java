package basavets.dao;

import basavets.bean.Location;
import basavets.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    private String filePathUser = "src/main/resources/users.csv";

    public UserDAO(){}

    public UserDAO(String filePathUser){
        this.filePathUser = filePathUser;
    }


    public List<User> getUsers() {
        return Storage.readFileUser(filePathUser);
    }


    public Optional<User> getUserByEmail(String email) {
        List<User> users = Storage.readFileUser(filePathUser);
        return users.stream().filter(user1 -> user1.getEmail().equals(email)).findAny();
    }


    public void addUser(User user) {
        List<User> users = Storage.readFileUser(filePathUser);
        users.add(user);
        Storage.writeFileUser(filePathUser, users);
    }


    public void saveCurrentLocation(User user, Location location) {
        List<User> users = Storage.readFileUser(filePathUser);
        for (User user1 : users) {
            if (user.getEmail().equals(user1.getEmail())) {
                user1.setLocation(location);
            }
        }
        Storage.writeFileUser(filePathUser, users);
    }


    public void deleteCurrentLocation(User user) {
        List<User> users = Storage.readFileUser(filePathUser);
        List<User> newUsers = new ArrayList<>();
        for (User user1 : users) {
            if (user.getEmail().equals(user1.getEmail())) {
                user1.setName(user.getName());
                user1.setEmail(user.getEmail());
                user1.setLocation(null);
            }
            newUsers.add(user1);
        }
        Storage.writeFileUser(filePathUser, newUsers);
    }
}
