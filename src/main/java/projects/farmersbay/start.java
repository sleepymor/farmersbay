package projects.farmersbay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class start extends Application {

    private static Stage primaryStage;
    private static String userType; 

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        loadChooseUserScene();
        primaryStage.setTitle("FarmersBay");
        primaryStage.show();
    }

    // Load chooseuser.fxml scene
    public static void loadChooseUserScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/chooseuser.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    
    public static void loadOptionScene(String chosenUserType) throws Exception {
        userType = chosenUserType;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/option.fxml"));
        Parent root = loader.load();

        OptionController controller = loader.getController();
        controller.setUserType(userType);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    // Load login.fxml scene
    public static void loadLoginScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    // Load sign up.fxml scene
    public static void loadSignUpScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/sign up.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

