package projects.farmersbay.view.Public;

import java.util.InputMismatchException; // Import for handling non-integer input
import java.util.Scanner;

import projects.farmersbay.controller.Public.CartController;
import projects.farmersbay.controller.Public.AuthController; // To access currentUserId, though CartController uses it internally

public class Cart {

    private Scanner scanner;
    private CartController cartController;

    public Cart() {
        this.scanner = new Scanner(System.in);
        this.cartController = new CartController();
    }

    public void show() {
        System.out.println("\n--- Your Shopping Cart ---");

        // Display the current cart every time the menu is shown
        cartController.cart(); // This method already prints cart details

        int choice = -1;
        while (choice != 0) {
            System.out.println("\nCart Options:");
            System.out.println("1. Add Item to Cart");
            System.out.println("2. Remove Item from Cart (by Item ID)"); // Updated prompt
            System.out.println("3. Update Item Quantity in Cart (by Item ID)"); // Updated prompt
            System.out.println("4. Proceed to Checkout");
            System.out.println("0. Go Back");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        addItemToCart();
                        break;
                    case 2:
                        removeItemFromCart();
                        break;
                    case 3:
                        updateItemInCart();
                        break;
                    case 4:
                        proceedToCheckout();
                        choice = 0; // Exit cart menu after checkout attempt
                        break;
                    case 0:
                        System.out.println("Returning to previous menu.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                // After each operation, refresh the cart view, unless exiting
                if (choice != 0) {
                    System.out.println("\n--- Updated Shopping Cart ---");
                    cartController.cart();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
                choice = -1; // Reset choice to keep the loop going
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addItemToCart() {
        System.out.println("\n--- Add Item to Cart ---");
        int itemId = -1;
        int quantity = -1;

        try {
            System.out.print("Enter Item ID to add: ");
            itemId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Quantity: ");
            quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            cartController.addToCart(itemId, quantity);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for Item ID and Quantity.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    private void removeItemFromCart() {
        System.out.println("\n--- Remove Item from Cart ---");
        // IMPORTANT: Prompt for Item ID, as per the schema, we identify by ItemID within an OrderID.
        System.out.print("Enter Item ID to remove from cart: ");
        try {
            int itemIdToRemove = scanner.nextInt(); // Changed variable name to reflect it's an Item ID
            scanner.nextLine(); // Consume newline
            cartController.removeFromCart(itemIdToRemove); // Call controller with Item ID
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for Item ID.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    private void updateItemInCart() {
        System.out.println("\n--- Update Item Quantity ---");
        // IMPORTANT: Prompt for Item ID, as per the schema, we identify by ItemID within an OrderID.
        System.out.print("Enter Item ID to update quantity for: ");
        try {
            int itemIdToUpdate = scanner.nextInt(); // Changed variable name to reflect it's an Item ID
            scanner.nextLine(); // Consume newline

            System.out.print("Enter new Quantity: ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            cartController.updateCart(itemIdToUpdate, newQuantity); // Call controller with Item ID
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid numbers.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    private void proceedToCheckout() {
        System.out.println("\n--- Proceeding to Checkout ---");
        int orderId = cartController.checkout();
        if (orderId != -1) {
            System.out.println("Checkout successful! Your order ID is: " + orderId);
        } else {
            System.out.println("Checkout failed. Please check your cart and try again.");
        }
    }

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}