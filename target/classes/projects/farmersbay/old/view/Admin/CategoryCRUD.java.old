package projects.farmersbay.view.Admin;

import java.util.List;
import java.util.Scanner;

import projects.farmersbay.controller.Admin.CategoryController;
import projects.farmersbay.model.Category;

public class CategoryCRUD {

    private final Scanner scanner = new Scanner(System.in);
    private final CategoryController categoryController = new CategoryController();

    public void showMenu() {
        while (true) {
            System.out.println("\n===== Category CRUD Menu =====");
            System.out.println("1. Create Category");
            System.out.println("2. Update Category");
            System.out.println("3. Delete Category");
            System.out.println("4. View Category by ID");
            System.out.println("5. List All Categories");
            System.out.println("6. Back to Dashboard");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 ->
                    createCategory();
                case 2 ->
                    updateCategory();
                case 3 ->
                    deleteCategory();
                case 4 ->
                    viewCategoryById();
                case 5 ->
                    listAllCategories();
                case 6 -> {
                    System.out.println("Returning to Dashboard...");
                    return;
                }
                default ->
                    System.out.println("Invalid option.");
            }
        }
    }

    private void createCategory() {
        Category category = new Category();
        System.out.print("Enter category title: ");
        category.setTitle(scanner.nextLine());

        categoryController.create(category);
    }

    private void updateCategory() {
        System.out.print("Enter category ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        Category category = categoryController.read(id);
        if (category == null) {
            System.out.println("Category not found.");
            return;
        }

        System.out.print("Enter new title (current: " + category.getTitle() + "): ");
        String newTitle = scanner.nextLine();
        if (!newTitle.isEmpty()) {
            category.setTitle(newTitle);
        }

        categoryController.update(category);
    }

    private void deleteCategory() {
        System.out.print("Enter category ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        Category category = categoryController.read(id);
        if (category == null) {
            System.out.println("Category not found.");
            return;
        }

        categoryController.delete(id);
        System.out.println("Category deleted successfully.");
        
    }

    private void viewCategoryById() {
        System.out.print("Enter category ID to view: ");
        int id = Integer.parseInt(scanner.nextLine());

        Category category = categoryController.read(id);
        if (category == null) {
            System.out.println("Category not found.");
        } else {
            System.out.println("Category ID: " + category.getCategoryID());
            System.out.println("Title: " + category.getTitle());
        }
    }

    private void listAllCategories() {
        List<Category> categories = categoryController.readAll();
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            System.out.println("\n===== All Categories =====");
            for (Category category : categories) {
                System.out.println("ID: " + category.getCategoryID() + ", Title: " + category.getTitle());
            }
        }
    }


}
