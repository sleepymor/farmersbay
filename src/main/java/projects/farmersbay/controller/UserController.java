package projects.farmersbay.controller;

import projects.farmersbay.controller.Controller;
import projects.farmersbay.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserController extends Controller<User> {
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public void create(User user) {
        users.put(user.getId(), user);
        System.out.println("User created: " + user.getName());
    }

    @Override
    public void update(User object) {
        if (users.containsKey(object.getId())) {
            users.put(object.getId(), object);
            System.out.println("User updated: " + object.getName());
        } else {
            System.out.println("User not found: " + object.getName());
        }
    }

    @Override
    public void delete(Integer id) {
        if (users.remove(id) != null) {
            System.out.println("User deleted with ID: " + id);
        } else {
            System.out.println("User not found with ID: " + id);
        }
    }

    @Override
    public User read(Integer id) {
        User user = users.get(id);
        if (user != null) {
            System.out.println("User found: " + user.getName());
            return user;
        } else {
            System.out.println("User not found with ID: " + id);
            return null;
        }
    }
    
}
