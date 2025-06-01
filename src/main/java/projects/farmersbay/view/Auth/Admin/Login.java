package projects.farmersbay.view.Auth.Admin;

import java.util.Scanner;

import projects.farmersbay.controller.Admin.AuthController;
import projects.farmersbay.model.Admin;

public class Login {

    public void show() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter username:");
            String name = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            AuthController auth = new AuthController();
            Admin admin = auth.login(name, password);

            if (admin != null) {
                System.out.println("Welcome, " + admin.getName() + "!");
                System.out.println("Current Admin ID set to: " + AuthController.currentAdminId);
                
            } else {
                System.out.println("Invalid username or password. Try again.");
            }
        }
    }
}
