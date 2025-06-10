package projects.farmersbay.view.Public;

import java.util.InputMismatchException; 
import java.util.Scanner;

import projects.farmersbay.controller.Public.CartController;
import projects.farmersbay.controller.Public.AuthController; 

public class Cart {

    private Scanner scanner;
    private CartController cartController;

    public Cart() {
        this.scanner = new Scanner(System.in);
        this.cartController = new CartController();
    }

    public void show() {
        System.out.println("\n--- Your Shopping Cart ---");
        cartController.cart(); 

        int choice = -1;
        while (choice != 0) {
            System.out.println("\nCart Options:");
            System.out.println("1. Add Item to Cart");
            System.out.println("2. Remove Item from Cart (by Item ID)"); 
            System.out.println("3. Update Item Quantity in Cart (by Item ID)");
            System.out.println("4. Proceed to Checkout");
            System.out.println("0. Go Back");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 

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
                        choice = 0; 
                        break;
                    case 0:
                        System.out.println("Returning to previous menu.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                
                if (choice != 0) {
                    System.out.println("\n--- Updated Shopping Cart ---");
                    cartController.cart();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
                choice = -1; 
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
            scanner.nextLine(); 

            System.out.print("Enter Quantity: ");
            quantity = scanner.nextInt();
            scanner.nextLine(); 

            cartController.addToCart(itemId, quantity);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for Item ID and Quantity.");
            scanner.nextLine(); 
        }
    }

    private void removeItemFromCart() {
        System.out.println("\n--- Remove Item from Cart ---");
        System.out.print("Enter Item ID to remove from cart: ");
        try {
            int itemIdToRemove = scanner.nextInt(); 
            scanner.nextLine(); 
            cartController.removeFromCart(itemIdToRemove); 
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for Item ID.");
            scanner.nextLine(); 
        }
    }

    private void updateItemInCart() {
        System.out.println("\n--- Update Item Quantity ---");
        System.out.print("Enter Item ID to update quantity for: ");
        try {
            int itemIdToUpdate = scanner.nextInt(); 
            scanner.nextLine(); 

            System.out.print("Enter new Quantity: ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine(); 

            cartController.updateCart(itemIdToUpdate, newQuantity); 
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid numbers.");
            scanner.nextLine(); 
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