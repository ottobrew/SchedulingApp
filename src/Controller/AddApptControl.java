package Controller;

import DAO.DBAppt;
import DAO.DBContact;
import DAO.DBCustomer;
import DAO.DBUser;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/**
 * This class handles data for Add new Appointment screen view
 */
public class AddApptControl implements Initializable {
    @FXML
    public AnchorPane addApptForm;
    public ComboBox<Contact> addApptContact;
    public TextField addApptTitle;
    public TextField addApptDescription;
    public TextField addApptLocation;
    public TextField addApptId;
    public ComboBox<String> addApptType;
    public Button addApptSaveButton;
    public Button addApptCancelButton;
    public ComboBox<Customer> addApptCustId;
    public ComboBox<User> addApptUserId;
    public TextField addApptStartTime;
    public TextField addApptEndTime;
    public DatePicker addApptDate;

    LocalDateTime now = LocalDateTime.now();
    LocalTime displayStartTime;
    LocalTime displayEndTime;
    LocalDate apptDate;
    LocalDateTime localStartDT;
    ZonedDateTime zonedStartDT;
    ZonedDateTime startDTinEST;
    LocalDateTime localEndDT;
    ZonedDateTime zonedEndDT;
    ZonedDateTime endDTinEST;

    LocalTime officeOpen = LocalTime.of( 8,00);
    LocalTime officeClose = LocalTime.of(22, 00);
    ObservableList<Appointment> aList = DBAppt.getAllAppts();

    /**
     * Method to check display appointment time against Office Hours (EST)
     * @return boolean true if within office hours; false if outside office hours
     */
    public boolean checkOfficeHours() {
        // Convert display time to EST then check if times are during business hours (08:00-22:00 EST)

        startDTinEST = zonedStartDT.withZoneSameInstant(ZoneId.of("UTC-05:00"));
        endDTinEST = zonedEndDT.withZoneSameInstant(ZoneId.of("UTC-05:00"));
        LocalTime estStart = startDTinEST.toLocalTime();
        LocalTime estEnd = endDTinEST.toLocalTime();

        if (estStart.isBefore(officeOpen) || estStart.isAfter(officeClose)) {
            // Do not add
            Alert start = new Alert(Alert.AlertType.ERROR, "Appointment start time falls outside of office hours. " +
                    "Please select a new start time.");
            start.show();
            return false;

        } else if (estEnd.isBefore(officeOpen) || estEnd.isAfter(officeClose)) {
            // Do not add
            Alert end = new Alert(Alert.AlertType.ERROR, "Appointment end time falls " +
                    "outside of office hours. Please select new ending time.");
            end.show();
            return false;

        } else {
            return true;
        }
    }

    /**
     * Method to check if proposed appointment times conflict with scheduled appointments in database
     * @return boolean true if proposed appointment is clear of overlaps; false if overlap with other appointments
     */

    public boolean checkTimeOverlaps() {
        LocalDateTime s = localStartDT;
        LocalDateTime e = localEndDT;
        boolean clear = false;

        // If clear of overlaps, return true; else return false

        if (aList.isEmpty()) {
            clear = true;

        } else {

            for (Appointment a : aList) {

                if ((s.isEqual(a.getStartDT()) || s.isAfter(a.getStartDT())) && (s.isBefore(a.getEndDT()))) {
                    clear = false;
                    break;
                } else if (e.isAfter(a.getStartDT()) && (e.isBefore(a.getEndDT()) || e.isEqual(a.getEndDT()))) {
                    clear = false;
                    break;
                } else if ((s.isBefore(a.getStartDT()) || s.isEqual(a.getStartDT())) && (e.isEqual(a.getEndDT()) || e.isAfter(a.getEndDT()))) {
                    clear = false;
                    break;
                } else if ((s.isBefore(a.getStartDT())) && (e.isAfter(a.getStartDT())) && e.isBefore(a.getEndDT())) {
                    clear = false;
                    break;
                } else {
                    clear = true;
                }
            }
        }

        if (!clear) {
            Alert overlap = new Alert(Alert.AlertType.WARNING, "The appointment time you entered overlaps with " +
                    "an existing scheduled appointment. Please adjust appointment times.");
            overlap.show();
        }

        return clear;
    }

    /**
     * Method to check if text entered by user can be parsed as LocalTime
     * @return boolean true if user entered times can be parsed; false if outside of HH:mm format
     */
    public boolean checkTimeFormat() {

        try {
            displayStartTime = LocalTime.parse(addApptStartTime.getText());
            displayEndTime = LocalTime.parse(addApptEndTime.getText());
            return true;
        }
        catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter appointment time in format HH:mm");
            alert.show();
            return false;
        }
    }

    /**
     * Method to check if proposed appointment start time is before proposed end time
     * @return boolean true if start is before end; false if not
     */
    public boolean checkStartEnd() {
        if (displayEndTime.isBefore(displayStartTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please ensure appointment end time is after start time.");
            alert.show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method to parse user entered data and format start LocalDateTime for database entry
     * @throws DateTimeParseException
     */
    public void getStartDT() throws DateTimeParseException {

        apptDate = addApptDate.getValue();

        // Displayed LocalDateTime from Datepicker + ComboBox
        localStartDT = LocalDateTime.of(apptDate.getYear(), apptDate.getMonth(),
                apptDate.getDayOfMonth(), displayStartTime.getHour(), displayStartTime.getMinute());

        // ZonedDateTime of localStartDT
        zonedStartDT = ZonedDateTime.of(localStartDT, ZoneId.systemDefault());
    }

    /**
     * Method to parse user entered data and format end LocalDateTime for database entry
     * @throws DateTimeParseException
     */

    public void getEndDT() throws DateTimeParseException {

        apptDate = addApptDate.getValue();

        // LocalDateTime from Datepicker + ComboBox
        localEndDT = LocalDateTime.of(apptDate.getYear(), apptDate.getMonth(),
                apptDate.getDayOfMonth(), displayEndTime.getHour(), displayEndTime.getMinute());

        // ZonedDateTime of localEndDT
        zonedEndDT = ZonedDateTime.of(localEndDT, ZoneId.systemDefault());
    }

    /**
     * Method to check for any empty fields in appointment form to ensure no empty or null entries.
     * @return boolean true if all fields are filled; false if any are empty or null
     */
    public boolean checkForEmptyFields() {
        // Check that all fields are filled in
        boolean filled = true;
        try {
            if (addApptTitle.getText().isEmpty() || addApptDescription.getText().isEmpty() || addApptLocation.getText().isEmpty() ||
                    addApptStartTime.getText().isEmpty() || addApptEndTime.getText().isEmpty() || addApptDate.getValue() == null ||
                    addApptType.getValue() == null || addApptContact.getValue() == null || addApptCustId.getValue() == null ||
                    addApptUserId.getValue() == null) {

                filled = false;

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill in all " +
                        "form fields before submitting.");
                alert.show();
            }
        }
        catch (NullPointerException e) {
            // Do nothing
        }
        return filled;
    }

    /**
     * Button handler to create new Appointment object and to call UPDATE statement in DBAppt to update in database,
     * after all logic conditions have been met.
     * @param actionEvent Button click
     * @throws IOException
     * @throws SQLException
     */

    public void onSaveAddAppt(ActionEvent actionEvent) throws IOException, SQLException {

        // If any method returns false, method returns respective Alert and returns.
        if (!checkForEmptyFields())
            return;

        if (!checkTimeFormat())
            return;

        getStartDT();
        getEndDT();

        if (!checkStartEnd())
            return;

        if (!checkOfficeHours())
            return;

        if (!checkTimeOverlaps())
            return;

        // All conditions must be true in order to proceed with Creating Appointment:

            // Set values for Appointment object
            int apptId = 0;
            String title = addApptTitle.getText();
            String description = addApptDescription.getText();
            String location = addApptLocation.getText();
            LocalDateTime startDT = localStartDT;
            LocalDateTime endDT = localEndDT;
            String type = addApptType.getSelectionModel().getSelectedItem();
            int contactId = addApptContact.getSelectionModel().getSelectedItem().getId();
            int custId = Integer.parseInt(addApptCustId.getSelectionModel().getSelectedItem().toString());
            int userId = Integer.parseInt(addApptUserId.getSelectionModel().getSelectedItem().toString());
            LocalDateTime createDate = now;

            // Create new Appointment Object
            Appointment newAppt = new Appointment(apptId, title, description, location, type, startDT, endDT,
                    contactId, custId, userId, createDate);

            // Call Insert statement in DBAppt
            DBAppt.addAppt(newAppt);

            // Return to Main Landing
            Parent root = FXMLLoader.load(getClass().getResource("/View/MainLanding.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1020, 750);
            stage.setTitle("Scheduling Application");
            stage.setScene(scene);
            stage.show();
    }

    /**
     * Button handler to cancel out of adding appointment, utilizes Lambda
     * expression (Consumer interface) to make code more efficient.
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelAddAppt(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any changes will be lost. Do you wish to continue?");
        alert.showAndWait().ifPresent((result -> {
            if (result == ButtonType.OK) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/View/MainLanding.fxml"));
                    Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 1020, 750);
                    stage.setTitle("Scheduling Application");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void onAddApptCustId(ActionEvent actionEvent) {
    }

    public void onAddApptUserId(ActionEvent actionEvent) {
    }

    public void onAddApptContact(ActionEvent actionEvent) {
    }

    public void onAddApptType(ActionEvent actionEvent) {
    }

    public void onAddDate(ActionEvent actionEvent) {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//  ComboBox set items
        addApptCustId.setItems(DBCustomer.getAllCustomers());
        addApptContact.setItems(DBContact.getAllContacts());
        addApptUserId.setItems(DBUser.getAllUsers());
        addApptType.getItems().add("Planning Session");
        addApptType.getItems().add("De-Briefing");
        addApptType.getItems().add("Triage");
        addApptType.getItems().add("Follow-Up");
    }

}
