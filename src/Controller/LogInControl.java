package Controller;

import DAO.DBAppt;
import DAO.DBUser;
import Model.Appointment;
import Model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Login Controller Class handles Login form interaction, login validation, log activity reporting
 */
public class LogInControl implements Initializable {

    @FXML
    public Button LogInButton;
    public Label LogInUserNameLabel;
    public Label LogInPasswordLabel;
    public TextField LogInUserTxt;
    public PasswordField LogInPasswordTxt;
    public Label AppLogInTitle;
    public Label loginLocation;
    public boolean loginValidated = false;
    private int activeUserId = 0;
    LocalDateTime now = LocalDateTime.now();
    ObservableList<Appointment> aList = DBAppt.getAllAppts();
    ObservableList<User> uList = DBUser.getAllUsers();

    /**
     * Method to handle user login. Calls method to validate login, then utilizes Lambda
     * expression (Consumer Functional Interface) in forEach statement to simplify code for getting active UserId,
     * and checks for upcoming appointments for the validated user.
     * @param actionEvent
     * @throws IOException
     */
    public void onLogIn(ActionEvent actionEvent) throws IOException {
        String currentUser = LogInUserTxt.getText();
        String passwordEntered = LogInPasswordTxt.getText();

        if(validateLogin(currentUser, passwordEntered)) {
            loginValidated = true;

            // If validated, get userId and check for appointments
            uList.forEach(
                    u-> {
                        if (u.getUserName().equals(currentUser))
                            setActiveUserId(u.getId());
                    }
            );

            checkUpcomingAppts();

            // If validated, show Main Landing
                Parent root = FXMLLoader.load(getClass().getResource("/View/MainLanding.fxml"));
                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1020, 750);
                stage.setTitle("Scheduling Application");
                stage.setScene(scene);
                stage.show();

            }
            else {
            loginValidated = false;

            Alert alert = new Alert(Alert.AlertType.ERROR);

            // Translate Alert to French
            ResourceBundle r = ResourceBundle.getBundle("Utilities/Login", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                alert.setContentText(r.getString("Incorrect") + " " + r.getString("Username") +
                " " + r.getString("or") + " " + r.getString("Password"));
                alert.show();
            } else if (Locale.getDefault().getLanguage().equals("en")) {
                alert.setContentText("Incorrect Username or Password");
                alert.show();
            }
        }
            logActivity();
        }

    /**
     * Method to validate username/password entered with the database Users objects
     * @param userName User input username
     * @param password User input password
     * @return
     */
    private boolean validateLogin(String userName, String password) {
        for (User u : uList) {
            // Check username
            if (u.getUserName().equals(userName)) {
                // Check password
                if (u.getPassword().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getActiveUserId() {
        return activeUserId;
    }

    public void setActiveUserId(int userId) {
        this.activeUserId = userId;
    }

    /**
     * Method to check at login if current validated user has any upcoming appointments
     * scheduled in the next 15 minutes.
     */
    public void checkUpcomingAppts() {
        Alert login = null;

        if (aList.isEmpty()) {
            login = new Alert(Alert.AlertType.INFORMATION, "You have no upcoming appointments.");
        }

        // Check userid from login
        for (Appointment a : aList) {

            if (a.getUserId() == getActiveUserId() && (a.getStartDT().isEqual(now) || (a.getStartDT().isAfter(now) && a.getStartDT().isBefore(now.plusMinutes(15))))) {
                login = new Alert(Alert.AlertType.INFORMATION, "You have an upcoming appointment " +
                        "at " + a.getStartDT().getHour() + ":" + a.getStartDT().getMinute() + " on " +
                        a.getStartDT().getMonth() + " " + a.getStartDT().getDayOfMonth() + "\n\n" +
                        "Appointment ID: " + a.getApptId() + " ");
            } else {
                login = new Alert(Alert.AlertType.INFORMATION, "You have no upcoming appointments.");
            }
        }
        assert login != null;
        login.showAndWait();
    }


    public void onUserName(ActionEvent actionEvent) {

    }

    public void onPassword(ActionEvent actionEvent) {
    }

    /**
     * Method to track all login attempts, gather attempted username and password,
     * timestamp, and if login was successful. All login activity is appended to the "login_activity.txt" file.
      * @throws IOException
     */

    public void logActivity() throws IOException {

        // Create FileWriter object
        FileWriter fWriter = new FileWriter("src/login_activity.txt", true);

        // Create and Open file
        PrintWriter outputFile = new PrintWriter(fWriter);

        outputFile.println("User Login: " + LogInUserTxt.getText());
        outputFile.println("Password: " + LogInPasswordTxt.getText());
        outputFile.println("Login Successful: " + loginValidated);
        outputFile.println("Timestamp: " + LocalDateTime.now());
        outputFile.close();

    }

    /**
     * Method to initialize Login Controller class.  Uses ResourceBundle to translate
     * to English or French depending upon the system's default Locale and language setting.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResourceBundle rb = ResourceBundle.getBundle("Utilities/Login", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                loginLocation.setText(rb.getString("Location") + ZoneId.systemDefault());
            }
            else if (Locale.getDefault().getLanguage().equals("en")){
                loginLocation.setText("Log-In Location: " + ZoneId.systemDefault());
            }




    }
}
