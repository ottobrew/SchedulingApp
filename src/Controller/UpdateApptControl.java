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
 * This class handles data in Update Appointment screen view, for updating an Appointment
 * selected from Appointment tableview in Main Landing
 */
public class UpdateApptControl implements Initializable {
    @FXML
    public AnchorPane updateApptForm;
    public ComboBox<Contact> updateApptContact;
    public TextField updateApptTitle;
    public TextField updateApptDescription;
    public TextField updateApptLocation;
    public TextField updateApptStartTime;
    public TextField updateApptId;
    public ComboBox<String> updateApptType;
    public Button updateApptSaveButton;
    public Button updateApptCancelButton;
    public TextField updateApptEndTime;
    public ComboBox<Customer> updateApptCustId;
    public ComboBox<User> updateApptUserId;
    public DatePicker updateApptDate;

    LocalDate apptDate;
    LocalDateTime localStartDT;
    ZonedDateTime zonedStartDT;
    ZonedDateTime startDTinEST;
    LocalDateTime localEndDT;
    ZonedDateTime zonedEndDT;
    ZonedDateTime endDTinEST;
    LocalTime displayStartTime;
    LocalTime displayEndTime;
    public int currentApptId;
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

        for (Appointment a : aList) {
            if (a.getApptId() == currentApptId) {
                clear = true;

            } else {
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
            displayStartTime = LocalTime.parse(updateApptStartTime.getText());
            displayEndTime = LocalTime.parse(updateApptEndTime.getText());
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

        apptDate = updateApptDate.getValue();

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

        apptDate = updateApptDate.getValue();

        // LocalDateTime from Datepicker + ComboBox
        localEndDT = LocalDateTime.of(apptDate.getYear(), apptDate.getMonth(),
                apptDate.getDayOfMonth(), displayEndTime.getHour(), displayEndTime.getMinute());

        // ZonedDateTime of LDT
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
            if (updateApptTitle.getText().isEmpty() || updateApptDescription.getText().isEmpty() || updateApptLocation.getText().isEmpty() ||
                    updateApptStartTime.getText().isEmpty() || updateApptEndTime.getText().isEmpty() || updateApptDate.getValue() == null ||
                    updateApptType.getValue() == null || updateApptContact.getValue() == null ||
                    updateApptCustId.getValue() == null || updateApptUserId.getValue() == null) {

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
     * Button handler to update Appointment object and to call UPDATE statement in DBAppt to update in database,
     * after all logic conditions have been met.
     * @param actionEvent Button click
     * @throws IOException
     * @throws SQLException
     */

    public void onSaveUpdateAppt(ActionEvent actionEvent) throws IOException, SQLException {

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

        // All conditions must be true in order to proceed with Updating Appointment:

            // If Appt ID of Database Member matches current Appt ID, update
            for (Appointment a : aList) {
                if (a.getApptId() == currentApptId) {
                    // update fields
                    a.setTitle(updateApptTitle.getText());
                    a.setDescription(updateApptDescription.getText());
                    a.setLocation(updateApptLocation.getText());
                    a.setType(updateApptType.getSelectionModel().getSelectedItem());
                    a.setContactId(updateApptContact.getSelectionModel().getSelectedItem().getId());
                    a.setCustId(Integer.parseInt(updateApptCustId.getSelectionModel().getSelectedItem().toString()));
                    a.setUserId(Integer.parseInt(updateApptUserId.getSelectionModel().getSelectedItem().toString()));
                    a.setStartDT(localStartDT);
                    a.setEndDT(localEndDT);

                    // Call Update Statement in DBAppt to update in database
                    DBAppt.updateAppt(a);

                }
            }

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
    public void onCancelUpdateAppt(ActionEvent actionEvent) throws IOException {
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


    /**
     * Method to send Appointment selection from Tableview to Update Appt Form
     * @param selectAppt Selected Appointment from Appt Table
     */

    public void sendAppt(Appointment selectAppt) {
        // Grab current Appt ID
        currentApptId = selectAppt.getApptId();

        // System default Zoned to be displayed
        ZonedDateTime displayStartZDT = ZonedDateTime.of(selectAppt.getStartDT(), ZoneId.systemDefault());
        ZonedDateTime displayEndZDT = ZonedDateTime.of(selectAppt.getEndDT(), ZoneId.systemDefault());

        // Populate Update form with selected Appointment attributes
        updateApptTitle.setText(selectAppt.getTitle());
        updateApptId.setText(String.valueOf(selectAppt.getApptId()));
        updateApptDescription.setText(selectAppt.getDescription());
        updateApptLocation.setText(selectAppt.getLocation());
        updateApptType.setValue(selectAppt.getType());
        updateApptDate.setValue(displayStartZDT.toLocalDate());
        updateApptStartTime.setText(String.valueOf(displayStartZDT.toLocalTime()));
        updateApptDate.setValue(displayEndZDT.toLocalDate());
        updateApptEndTime.setText(String.valueOf(displayEndZDT.toLocalTime()));

        // Populate ComboBoxes
        for (Contact c : DBContact.getAllContacts()) {
            if (c.getId() == selectAppt.getContactId()) {
                updateApptContact.setValue(c);
            }
        }

        for (Customer c : DBCustomer.getAllCustomers()) {
            if (c.getCustId() == selectAppt.getCustId()) {
                updateApptCustId.setValue(c);
            }
        }

        for (User u : DBUser.getAllUsers()) {
            if (u.getId() == selectAppt.getUserId()) {
                updateApptUserId.setValue(u);
            }
        }
    }


    public void onUpdateApptContact(ActionEvent actionEvent) {
    }

    public void onUpdateApptType(ActionEvent actionEvent) {
    }

    public void onUpdateApptCustId(ActionEvent actionEvent) {
    }

    public void onUpdateApptUserId(ActionEvent actionEvent) {
    }

    public void onUpdateDate(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateApptContact.setItems(DBContact.getAllContacts());
        updateApptCustId.setItems(DBCustomer.getAllCustomers());
        updateApptUserId.setItems(DBUser.getAllUsers());
        updateApptType.getItems().add("Planning Session");
        updateApptType.getItems().add("De-Briefing");
        updateApptType.getItems().add("Triage");
        updateApptType.getItems().add("Follow-Up");

    }
}
