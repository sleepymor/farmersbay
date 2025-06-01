package projects.farmersbay.view.Auth.Public;

import java.util.Scanner;

import projects.farmersbay.controller.Public.AuthController;
import projects.farmersbay.model.User;

public class Login {

    public void show() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String name = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        AuthController auth = new AuthController();
        User user = auth.login(name, password);

        if (user != null) {
            System.out.println("Welcome, " + user.getName() + "!");
            // You can redirect them to the next menu/view here
        } else {
            System.out.println("Invalid username or password. Try again.");
        }
    }
}
