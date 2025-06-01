package projects.farmersbay;

import projects.farmersbay.model.User;
import projects.farmersbay.controller.UserController;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserController userController = new UserController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==== User Signup ====");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Create new user without ID
        User newUser = new User(null, name, password);
        userController.create(newUser); // ID will be auto-generated

        // Read the newly created user using their ID (if needed)
        if (newUser.getId() != null) {
            User user = userController.read(newUser.getId());
            if (user != null) {
                System.out.println("Retrieved user: " + user.getName());
            }
        }

        // List all users
        System.out.println("\n=== All Users ===");
        List<User> allUsers = userController.readAll();
        for (User u : allUsers) {
            System.out.println("User: " + u.getId() + " - " + u.getName());
        }

        scanner.close();
    }
}
