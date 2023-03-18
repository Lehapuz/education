package basavets.bean;

import java.util.Objects;

public class AdminFriendsLocation {

    private String userEmail;
    private String locationName;

    public AdminFriendsLocation(){}

    public AdminFriendsLocation(String userEmail, String locationName) {
        this.userEmail = userEmail;
        this.locationName = locationName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, locationName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        AdminFriendsLocation adminFriendsLocation = (AdminFriendsLocation) obj;
        return Objects.equals(userEmail, adminFriendsLocation.userEmail)
                && Objects.equals(locationName, adminFriendsLocation.locationName);
    }

    @Override
    public String toString() {
        return "Пользователь - " + userEmail + "Имеет права администратора к локации - " + locationName;
    }
}
