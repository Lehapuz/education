package basavets.bean;

import java.util.List;
import java.util.Objects;

public class Location {

    private String name;
    private String address;
    private User user;
    private List<User> usersInLocation;

    public Location() {
    }

    public Location(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsersInLocation() {
        return usersInLocation;
    }

    public void setUsersInLocation(List<User> usersInLocation) {
        this.usersInLocation = usersInLocation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Location location = (Location) obj;
        return Objects.equals(name, location.name) && Objects.equals(address, location.address);
    }

    @Override
    public String toString() {
        return "Мое текущее положение - " + name;
    }
}
