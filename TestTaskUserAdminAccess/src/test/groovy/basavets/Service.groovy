package basavets


import basavets.bean.AdminFriendsLocation
import basavets.bean.Location
import basavets.bean.User
import basavets.controller.DefaultController
import basavets.dao.AdminFriendsLocationDAO
import basavets.dao.LocationDAO
import basavets.dao.Storage
import basavets.dao.UserDAO
import basavets.service.UserService
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.lang.Nullable
import org.springframework.ui.Model
import spock.lang.Specification

@SpringBootTest
class Service extends Specification {

    User user1 = new User("Vasil", "v@mail.ru")
    User user2 = new User("Alex", "leha@mail.ru")
    User user3 = new User("Petr", "p@mail.ru")
    UserDAO userDAO = new UserDAO("src/test/resources/userTest.csv")
    Location location1 = new Location("Home", "Prospect")
    Location location2 = new Location("Away", "Street")
    LocationDAO locationDAO = new LocationDAO("src/test/resources/locationTest.csv")
    AdminFriendsLocation adminFriendsLocation = new AdminFriendsLocation("v@mail.ru", "Away")
    AdminFriendsLocationDAO adminFriendsLocationDAO = new AdminFriendsLocationDAO(
            "src/test/resources/adminUserLocationTest.csv")
    List<User> userList = new ArrayList<>()
    List<User> cleanUserList = new ArrayList<>()
    List<Location> locationList = new ArrayList<>()
    List<Location> cleanLocationList = new ArrayList<>()
    List<AdminFriendsLocation> adminFriendsLocationList = new ArrayList<>();
    List<AdminFriendsLocation> cleanAdminFriendsLocationList = new ArrayList<>();
    UserService userService = new UserService(userDAO, locationDAO, adminFriendsLocationDAO);
    DefaultController defaultController = new DefaultController(userService)


    def "when context is loaded then all expected beans are created"() {
        expect: "the Controller is created"
        defaultController
    }

    def "add user, place, moderator"() {
        given:
        Model model = new Model() {
            @Override
            Model addAttribute(String attributeName, @Nullable Object attributeValue) {
                return null
            }

            @Override
            Model addAttribute(Object attributeValue) {
                return null
            }

            @Override
            Model addAllAttributes(Collection<?> attributeValues) {
                return null
            }

            @Override
            Model addAllAttributes(Map<String, ?> attributes) {
                return null
            }

            @Override
            Model mergeAttributes(Map<String, ?> attributes) {
                return null
            }

            @Override
            boolean containsAttribute(String attributeName) {
                return false
            }

            @Override
            Object getAttribute(String attributeName) {
                return null
            }

            @Override
            Map<String, Object> asMap() {
                return null
            }
        }

        when:
        defaultController.registerUser(user1, model)
        defaultController.registerUser(user2, model)
        defaultController.registerUser(user3, model)
        defaultController.addPlace(user1, location1, model)
        defaultController.addPlace(user2, location2, model)
        defaultController.addCurrentLocation(user1, location1.getName(), model)
        defaultController.addModerator(user2, "Away", "p@mail.ru")
        defaultController.authorizationUser(user1, model);

        then:
        userService.findUserByEmail(user1.getEmail()).get().getLocation().getName() == "Home"
        userService.getAllAdminFriendsLocationList().size() == 1

        cleanup:
        defaultController.deleteLocation(user1)
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
        Storage.writeFileLocation("src/test/resources/locationTest.csv", cleanLocationList)
        Storage.writeFileAdminFriendLocation("src/test/resources/adminUserLocationTest.csv", cleanAdminFriendsLocationList)
    }

    def "get all users"() {
        given:
        userList.add(user1)
        userList.add(user2)

        when:
        userService.saveUser(user1)
        userService.saveUser(user2)

        then:
        userService.getAllUsers() == userList

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "add user on location"() {
        given:
        location1.setUser(user1)
        location2.setUser(user2)
        userService.saveLocation(location1)
        userService.saveLocation(location2)

        when:
        userService.addUserOnLocation(location2, user1)
        Optional<Location> location = userService.findLocationByName(location2.getName())

        then:
        location.get().getUsersInLocation().size() == 1

        cleanup:
        Storage.writeFileUser("src/test/resources/locationTest.csv", cleanUserList)
    }

    def "delete user from location"() {
        given:
        location1.setUser(user1)
        location2.setUser(user2)
        userService.saveLocation(location1)
        userService.saveLocation(location2)
        userService.addUserOnLocation(location2, user1)

        when:
        userService.deleteUserFromLocation(location2, user1)
        Optional<Location> location = userService.findLocationByName(location2.getName())

        then:
        location.get().getUsersInLocation() == null

        cleanup:
        Storage.writeFileUser("src/test/resources/locationTest.csv", cleanUserList)
    }

    def "get user location"() {
        given:
        location1.setUser(user1)
        location2.setUser(user1)

        when:
        userService.saveLocation(location1)
        userService.saveLocation(location2)

        then:
        userService.getUserLocation(user1).size() == 2

        cleanup:
        Storage.writeFileUser("src/test/resources/locationTest.csv", cleanUserList)
    }

    def "get user without current location"() {
        when:
        userService.saveUser(user1)
        userService.saveUser(user2)
        userService.saveUser(user3)

        then:
        userService.getUsersWithoutCurrent(user1).size() == 2

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "get user with location"() {

        when:
        user1.setLocation(location1)
        user2.setLocation(location2)
        userService.saveUser(user1)
        userService.saveUser(user2)
        userService.saveUser(user3)

        then:
        userService.getUsersWithLocation(user2).size() == 1

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "get my admin access location list"() {
        given:
        location1.setUser(user1)
        location2.setUser(user2)
        user1.setLocation(location1)
        user2.setLocation(location2)

        when:
        userService.saveLocation(location1);
        userService.saveLocation(location2)
        userService.saveUser(user1)
        userService.saveUser(user2)
        userService.saveUser(user3)
        userService.saveAdminFriendsLocation(adminFriendsLocation)

        then:
        userService.getMyAdminAccessLocationList(user1).size() == 1

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
        Storage.writeFileLocation("src/test/resources/locationTest.csv", cleanLocationList)
        Storage.writeFileAdminFriendLocation("src/test/resources/adminUserLocationTest.csv", cleanAdminFriendsLocationList)
    }

    def "get locations with users"() {
        given:
        location1.setUser(user1)
        location2.setUser(user2)
        user1.setLocation(location1)
        user2.setLocation(location2)
        userService.saveLocation(location1);
        userService.saveLocation(location2)
        userService.saveUser(user1)
        userService.saveUser(user2)
        userService.saveUser(user3)

        when:
        userService.addUserOnLocation(location1, user2)
        userService.addUserOnLocation(location2, user3)

        then:
        userService.getLocationsWithUsers().size() == 2

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
        Storage.writeFileLocation("src/test/resources/locationTest.csv", cleanLocationList)
    }

    def "get users on location"() {
        given:
        location1.setUser(user1)
        location2.setUser(user2)
        user1.setLocation(location1)
        user2.setLocation(location2)
        userService.saveLocation(location1);
        userService.saveLocation(location2)
        userService.saveUser(user1)
        userService.saveUser(user2)
        userService.saveUser(user3)

        when:
        userService.addUserOnLocation(location1, user2)
        userService.addUserOnLocation(location1, user3)

        then:
        userService.getUsersOnLocation(location1.getName()).size() == 2

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
        Storage.writeFileLocation("src/test/resources/locationTest.csv", cleanLocationList)
    }

    def "delete current location"() {
        given:
        location1.setUser(user1)
        user1.setLocation(location1)
        userService.saveLocation(location1);
        userService.saveUser(user1)

        when:
        userService.deleteCurrentLocation(user1)


        then:
        userService.findUserByEmail(user1.getEmail()).get().getLocation() == null

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
        Storage.writeFileLocation("src/test/resources/locationTest.csv", cleanLocationList)
    }

    def "save user on location"() {
        given:
        Model model = new Model() {
            @Override
            Model addAttribute(String attributeName, @Nullable Object attributeValue) {
                return null
            }

            @Override
            Model addAttribute(Object attributeValue) {
                return null
            }

            @Override
            Model addAllAttributes(Collection<?> attributeValues) {
                return null
            }

            @Override
            Model addAllAttributes(Map<String, ?> attributes) {
                return null
            }

            @Override
            Model mergeAttributes(Map<String, ?> attributes) {
                return null
            }

            @Override
            boolean containsAttribute(String attributeName) {
                return false
            }

            @Override
            Object getAttribute(String attributeName) {
                return null
            }

            @Override
            Map<String, Object> asMap() {
                return null
            }
        }

        User user1 = new User("Lena", "lena@mail.ru");
        User user2 = new User("Alex", "alex@mail.ru");
        Location location = new Location("current location", "there")
        defaultController.registerUser(user1, model)
        defaultController.registerUser(user2, model)
        defaultController.addPlace(user1, location, model)
        defaultController.addCurrentLocation(user1, "current location", model)

        when:
        defaultController.addUserOnLocation("current location", "alex@mail.ru")

        then:
        userService.findLocationByName(location.getName()).get().getUsersInLocation().size() == 1

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
        Storage.writeFileLocation("src/test/resources/locationTest.csv", cleanLocationList)
    }

    def "del user from location"() {
        given:
        Model model = new Model() {
            @Override
            Model addAttribute(String attributeName, @Nullable Object attributeValue) {
                return null
            }

            @Override
            Model addAttribute(Object attributeValue) {
                return null
            }

            @Override
            Model addAllAttributes(Collection<?> attributeValues) {
                return null
            }

            @Override
            Model addAllAttributes(Map<String, ?> attributes) {
                return null
            }

            @Override
            Model mergeAttributes(Map<String, ?> attributes) {
                return null
            }

            @Override
            boolean containsAttribute(String attributeName) {
                return false
            }

            @Override
            Object getAttribute(String attributeName) {
                return null
            }

            @Override
            Map<String, Object> asMap() {
                return null
            }
        }

        User user1 = new User("Lena", "lena@mail.ru");
        User user2 = new User("Alex", "alex@mail.ru");
        Location location = new Location("current location", "there")
        defaultController.registerUser(user1, model)
        defaultController.registerUser(user2, model)
        defaultController.addPlace(user1, location, model)
        defaultController.addCurrentLocation(user1, "current location", model)
        defaultController.addUserOnLocation("current location", "alex@mail.ru")

        when:
        defaultController.deleteUserFromLocation("current location", "alex@mail.ru")

        then:
        userService.findLocationByName(location.getName()).get().getUsersInLocation() == null

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
        Storage.writeFileLocation("src/test/resources/locationTest.csv", cleanLocationList)
    }
}
