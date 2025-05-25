package projects.farmersbay;

import projects.farmersbay.model.User;

import java.sql.Connection;

import projects.farmersbay.controller.UserController;
import projects.farmersbay.database.Database;

public class Main {
    public static void main(String[] args) {
        UserController userController = new UserController();
        Connection conn = Database.connect();

        // Example usage of UserController
        User user1 = new User(1, "Alice", "password123");
        User user2 = new User(2, "Bob", "password456");
        userController.create(user1);

        User fetchedUser = userController.read(1);
        if (fetchedUser != null) {
            System.out.println("Fetched User: " + fetchedUser.getName());
        }
    }
}