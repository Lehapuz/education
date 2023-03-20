package basavets.dao;

import basavets.bean.AdminFriendsLocation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminFriendsLocationDAO {

    private String filePathLocation = "src/main/resources/adminUserLocation.csv";

    public AdminFriendsLocationDAO(){}

    public AdminFriendsLocationDAO(String filePathLocation){
        this.filePathLocation = filePathLocation;
    }

    public void addAdminFriendLocation (AdminFriendsLocation adminFriendsLocation){
        List<AdminFriendsLocation> adminFriendsLocations = Storage.readFileAdminFriendLocation(filePathLocation);
        adminFriendsLocations.add(adminFriendsLocation);
        Storage.writeFileAdminFriendLocation(filePathLocation, adminFriendsLocations);
    }

    public List<AdminFriendsLocation> getAllAdminLocations(){
        return Storage.readFileAdminFriendLocation(filePathLocation);
    }
}
