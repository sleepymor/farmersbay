package projects.farmersbay.view.Auth.Admin;

import java.util.Scanner;

import projects.farmersbay.controller.Admin.AuthController;
import projects.farmersbay.model.Admin;

public class SignUp {

    public void show() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Create a username:");
            String name = scanner.nextLine();
            System.out.println("Create a password:");
            String password = scanner.nextLine();

            Admin admin = new Admin();
            admin.setName(name);
            admin.setPassword(password);

            AuthController auth = new AuthController();
            auth.create(admin);
        }
    }
}
