package projects.farmersbay;


public class Main {
    public static void main(String[] args) {
        
        projects.farmersbay.view.Auth.Login loginView = new projects.farmersbay.view.Auth.Login();
        projects.farmersbay.view.Auth.SignUp signUpView = new projects.farmersbay.view.Auth.SignUp();
        // Initialize the views
        System.out.println("FarmersBay application started.");

        //chose view or signup using scanner
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("Choose an option: \n1. Login \n2. Sign Up");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        if (choice == 1) {
            loginView.show(); // Show the login view
        } else if (choice == 2) {
            signUpView.show(); // Show the sign-up view
        } else {
            System.out.println("Invalid choice. Please restart the application.");
        }
    }
}
