package basavets

import basavets.bean.Access
import basavets.bean.Location
import basavets.bean.User
import basavets.dao.LocationDAO
import basavets.dao.Storage
import basavets.dao.UserDAO
import basavets.service.UserService
import spock.lang.Specification

class FirstSpecification extends Specification {
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


    def "save user"() {
        given:
        userList.add(user1)
        userList.add(user2)

        when:
        userDAO.addUser(user1)
        userDAO.addUser(user2)

        then:
        userDAO.getUsers() == userList

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "get user by email"() {
        given:
        userDAO.addUser(user1)
        userDAO.addUser(user2)

        when:
        Optional<User> user = userDAO.getUserByEmail("leha@mail.ru")

        then:
        user.get() == user2

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "save current location"() {
        given:
        userDAO.addUser(user1)
        userDAO.addUser(user2)
        user3.setLocation(location1)

        when:
        userDAO.saveCurrentLocation(user1, location1)
        Optional<User> user = userDAO.getUserByEmail(user1.getEmail())

        then:
        user.get().getLocation().getName() == user3.getLocation().getName()

        cleanup:
        Storage.writeFileUser("src/test/resources/userTest.csv", cleanUserList)
    }

    def "delete current location"() {
        given:
        user1.setLocation(location1)
        userDAO.addUser(user1)
        userDAO.addUser(user2)


        when:
        userDAO.deleteCurrentLocation(user1)
        Optional<User> user = userDAO.getUserByEmail(user1.getEmail())

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
        locationDAO.addLocation(location1)
        locationDAO.addLocation(location2)

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
        Optional<Location> location = locationDAO.getLocationByName("Home")

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
        locationDAO.addLocation(location1)
        locationDAO.addLocation(location2)

        when:
        locationDAO.addUserOnLocation(location2, user1)
        Optional<Location> location = locationDAO.getLocationByName(location2.getName())

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
        locationDAO.addUserOnLocation(location1, user2)
        Optional<Location> location = locationDAO.getLocationByName(location1.getName())

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
        locationDAO.addLocation(location1)
        locationDAO.addLocation(location2)
        locationDAO.addUserOnLocation(location2, user1)

        when:
        locationDAO.deleteUserFromLocation(location2, user1)
        Optional<Location> location = locationDAO.getLocationByName(location2.getName())

        then:
        location.get().getUsersInLocation() == null

        cleanup:
        Storage.writeFileUser("src/test/resources/locationTest.csv", cleanUserList)
    }

    }



