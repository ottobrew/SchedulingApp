package Model;

import DAO.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

/** Javadoc resides in folder at /SchedulingAppV1/Javadoc
 * @author Ryan Otto
 */

/**
 * Main class creates Application for Scheduling Appointments and Managing Customers.
 * Application connects to database for holding and organizing the data.
 */
public class Main extends Application {

    /**
     * Start method initiates JavaFX stage, which opens Login Form. Start method is abstract so it is overridden.
     * @param primaryStage Instance of Stage where JavaFX scenes are created.
     * @throws Exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Locale.setDefault(new Locale("fr", "FR"));
        ResourceBundle rb = ResourceBundle.getBundle("Utilities/Login", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource(("../View/LogInForm.fxml")), (rb));
        primaryStage.setScene(new Scene(root, 500, 250));
        primaryStage.show();
    }

    /**
     * Main method where Database connection is opened. JavaFX application is launched.
     * Database connection is closed.
     * @param args
     */
    public static void main(String[] args) {
        DBConnection.openConnection();

        launch(args);

        DBConnection.closeConnection();
    }
}
