package basavets

import basavets.bean.Access
import basavets.bean.Location
import basavets.bean.User
import basavets.controller.DefaultController
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
    List<User> userList = new ArrayList<>()
    List<User> cleanUserList = new ArrayList<>()
    List<Location> locationList = new ArrayList<>()
    List<Location> cleanLocationList = new ArrayList<>()
    UserService userService = new UserService(userDAO, locationDAO);

    DefaultController defaultController = new DefaultController(userService)


    def "save user"() {
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

    def "get user by email"() {
        given:
        userService.saveUser(user1)
        userService.saveUser(user2)

        when:
        Optional<User> user = userService.findUserByEmail("leha@mail.ru")

        then:
        user.get() == user2

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "save current location"() {
        given:
        userService.saveUser(user1)
        userService.saveUser(user2)
        user3.setLocation(location1)

        when:
        userService.saveCurrentLocation(user1, location1)
        Optional<User> user = userService.findUserByEmail(user1.getEmail())

        then:
        user.get().getLocation().getName() == user3.getLocation().getName()

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "delete current location"() {
        given:
        user1.setLocation(location1)
        userService.saveUser(user1)
        userService.saveUser(user2)

        when:
        userService.deleteCurrentLocation(user1)
        Optional<User> user = userService.findUserByEmail(user1.getEmail())

        then:
        user.get().getLocation() == null

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "save location"() {
        given:
        location1.setAccess(Access.ACCESS)
        location2.setAccess(Access.ADMIN_ACCESS)
        location1.setUser(user1)
        location2.setUser(user2)
        locationList.add(location1)
        locationList.add(location2)

        when:
        userService.saveLocation(location1)
        userService.saveLocation(location2)

        then:
        locationDAO.getLocations() == locationList

        cleanup:
        Storage.writeFileLocation("src/test/resources/locationTest.csv", cleanLocationList)
    }

    def "get location by name"() {
        given:
        location1.setAccess(Access.ACCESS)
        location2.setAccess(Access.ADMIN_ACCESS)
        location1.setUser(user1)
        location2.setUser(user2)
        locationDAO.addLocation(location1)
        locationDAO.addLocation(location2)

        when:
        Optional<Location> location = userService.findLocationByName("Home")

        then:
        location.get() == location1

        cleanup:
        Storage.writeFileUser("src/test/resources/locationTest.csv", cleanUserList)
    }

    def "add user on location"() {
        given:
        location1.setAccess(Access.ACCESS)
        location2.setAccess(Access.ADMIN_ACCESS)
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

    def "add user on forbidden location"() {
        given:
        location1.setAccess(Access.ACCESS)
        location2.setAccess(Access.ADMIN_ACCESS)
        location1.setUser(user1)
        location2.setUser(user2)
        locationDAO.addLocation(location1)
        locationDAO.addLocation(location2)

        when:
        userService.addUserOnLocation(location1, user2)
        Optional<Location> location = userService.findLocationByName(location1.getName())

        then:
        location.get().getUsersInLocation() == null

        cleanup:
        Storage.writeFileUser("src/test/resources/locationTest.csv", cleanUserList)
    }

    def "delete user from location"() {
        given:
        location1.setAccess(Access.ACCESS)
        location2.setAccess(Access.ADMIN_ACCESS)
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

    def "user is present"() {

        when:
        userService.saveUser(user1)
        userService.saveUser(user2)

        then:
        userService.emailIsPresent("leha@mail.ru")

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "location is present"() {

        given:
        location1.setAccess(Access.ACCESS)
        location2.setAccess(Access.ADMIN_ACCESS)
        location1.setUser(user1)
        location2.setUser(user2)

        when:
        userService.saveLocation(location1)
        userService.saveLocation(location2)

        then:
        userService.nameLocationIsPresent("Home")

        cleanup:
        Storage.writeFileUser("src/test/resources/locationTest.csv", cleanUserList)
    }

    def "get user location"() {

        given:
        location1.setAccess(Access.ACCESS)
        location2.setAccess(Access.ADMIN_ACCESS)
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

    def "when context is loaded then all expected beans are created"() {
        expect: "the Controller is created"
        defaultController
    }

    def "add user"() {

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

        User user = new User("Lena", "lena@mail.ru");
        Location location = new Location("current location", "there")

        when:
        defaultController.registerUser(user, model)
        defaultController.addPlace(user, location, model, Access.ACCESS)
        defaultController.addCurrentLocation(user, "current location", model)

        then:
        userService.findUserByEmail(user.getEmail()).get().getLocation().getName() == "current location"

        cleanup:
        defaultController.deleteLocation(user)
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
        defaultController.addPlace(user1, location, model, Access.ADMIN_ACCESS)
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
        defaultController.addPlace(user1, location, model, Access.ADMIN_ACCESS)
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
