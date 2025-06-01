package projects.farmersbay;


public class Main {
    public static void main(String[] args) {
        
        projects.farmersbay.view.Auth.Public.Login loginView = new projects.farmersbay.view.Auth.Public.Login();
        projects.farmersbay.view.Auth.Public.SignUp signUpView = new projects.farmersbay.view.Auth.Public.SignUp();

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("Choose an option: \n1. Login \n2. Sign Up");
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        if (choice == 1) {
            loginView.show(); 
        } else if (choice == 2) {
            signUpView.show(); 
        } else {
            System.out.println("Invalid choice. Please restart the application.");
        }
    }
}
