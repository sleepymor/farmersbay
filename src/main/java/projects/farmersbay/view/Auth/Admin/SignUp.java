package projects.farmersbay.view.Auth.Admin;

import java.util.Scanner;

import projects.farmersbay.controller.Public.AuthController;
import projects.farmersbay.model.User;

public class SignUp {

    public void show() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Create a username:");
        String name = scanner.nextLine();
        System.out.println("Create a password:");
        String password = scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setPassword(password);

        AuthController auth = new AuthController();
        auth.create(user);
    }
}
