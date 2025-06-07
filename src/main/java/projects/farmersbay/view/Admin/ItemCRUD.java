package projects.farmersbay.view.Admin;

import java.util.List;
import java.util.Scanner;

import projects.farmersbay.controller.Admin.ItemsController;
import projects.farmersbay.controller.Admin.AuthController;
import projects.farmersbay.model.Items;

public class ItemCRUD {
    
    private final Scanner scanner = new Scanner(System.in);
    private final ItemsController itemsController = new ItemsController();

    public void showMenu() {
        while (true) {
            System.out.println("\n===== Item CRUD Menu =====");
            System.out.println("1. Create Item");
            System.out.println("2. Update Item");
            System.out.println("3. Delete Item");
            System.out.println("4. View Item by ID");
            System.out.println("5. List All Items");
            System.out.println("6. Back to Login");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createItem();
                case 2 -> updateItem();
                case 3 -> deleteItem();
                case 4 -> viewItemById();
                case 5 -> listAllItems();
                case 6 -> {
                    System.out.println("Returning to Login...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void createItem() {
        Items item = new Items();

        System.out.print("Enter item name: ");
        item.setTitle(scanner.nextLine());

        System.out.print("Enter item price: ");
        item.setPrice(Double.parseDouble(scanner.nextLine()));

        System.out.print("Enter item stock: ");
        item.setStock(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter item image URL: ");
        item.setImg(scanner.nextLine());

        item.setAdminId(AuthController.currentAdminId); // Important: Set to logged-in admin
        itemsController.create(item);
    }

    private void updateItem() {
        System.out.print("Enter item ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        Items item = itemsController.read(id);

        if (item != null) {
            System.out.print("New name [" + item.getTitle() + "]: ");
            item.setTitle(scanner.nextLine());

            System.out.print("New price [" + item.getPrice() + "]: ");
            item.setPrice(Double.parseDouble(scanner.nextLine()));

            System.out.print("New stock [" + item.getStock() + "]: ");
            item.setStock(Integer.parseInt(scanner.nextLine()));

            System.out.print("New image URL [" + item.getImg() + "]: ");
            item.setImg(scanner.nextLine());

            itemsController.update(item);
        } else {
            System.out.println("Item not found.");
        }
    }

    private void deleteItem() {
        System.out.print("Enter item ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        Items item = itemsController.read(id);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        itemsController.delete(id);
    }

    private void viewItemById() {
        System.out.print("Enter item ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Items item = itemsController.read(id);

        if (item != null) {
            System.out.println("Item ID: " + item.getItemId());
            System.out.println("Name: " + item.getTitle());
            System.out.println("Price: " + item.getPrice());
            System.out.println("Stock: " + item.getStock());
            System.out.println("Image URL: " + item.getImg());
            System.out.println("Admin ID: " + item.getAdminId());
        } else {
            System.out.println("Item not found.");
        }
    }

    private void listAllItems() {
        List<Items> items = itemsController.readAll();
        for (Items item : items) {
            System.out.println(item.getItemId() + " | " + item.getTitle() + " | Rp" + item.getPrice() + " | Stock: " + item.getStock() + " | Admin ID: " + item.getAdminId() + " | Image: " + item.getImg());
        }
    }
}
