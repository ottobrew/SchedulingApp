package Controller;

import DAO.DBAppt;
import DAO.DBContact;
import Model.Appointment;
import Model.Contact;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Reports Controller Class handles report generation on Reports View
 */

public class ReportsControl implements Initializable {
    @FXML
    public TableView<Appointment> scheduleByContactTable;
    public TableColumn<Appointment, Integer> schApptIdCol;
    public TableColumn<Appointment, String> schTitleCol;
    public TableColumn<Appointment, String> schTypeCol;
    public TableColumn<Appointment, String> schDescCol;
    public TableColumn<Appointment, LocalDateTime> schStartCol;
    public TableColumn<Appointment, LocalDateTime> schEndCol;
    public TableColumn<Appointment, Integer> schCustIdCol;
    public ComboBox<String> typeComboBox;
    public ComboBox<String> monthComboBox;
    public Label totalTypeMonth;
    public Button getTotalApptsButton;
    public ComboBox<Contact> selContactNameComboBox;
    public Button exitButton;

    public ObservableList<Appointment> aList = DBAppt.getAllAppts();
    public int apptCount = 0;
    public int dayCount = 0;
    public Label totalDayOfWeek;
    public ComboBox<String> dayComboBox;

    public void onTypeComboBox(ActionEvent actionEvent) {
    }

    public void onMonthComboBox(ActionEvent actionEvent) {
    }

    /**
     * Method to generate report of total Appointments matching Type and Month selections
     * @param actionEvent Button
     */
    public void onGetTotalAppts(ActionEvent actionEvent) {
        apptCount = 0;

        try {
            for (Appointment a : aList) {
                if ((typeComboBox.getSelectionModel().getSelectedItem().equals(a.getType())) &&
                        (monthComboBox.getSelectionModel().getSelectedItem().equals(a.getStartDT().getMonth().toString()))) {
                    apptCount++;
                }
            }
            totalTypeMonth.setText("Total: " + apptCount);
        } catch (NullPointerException e) {
            // Do nothing
        }
    }

    /**
     * Method to populate tableview with the schedule for the selected Contact
     * @param actionEvent
     */

    public void onSelContactName(ActionEvent actionEvent) {
        scheduleByContactTable.getItems().clear();
        for (Appointment a : aList) {
            if (selContactNameComboBox.getSelectionModel().getSelectedItem().getId() == a.getContactId()) {
                scheduleByContactTable.getItems().add(a);
            }
        }
    }



    /**
     * Method to calculate number of appointments based on user's selection of a given day of week
     * @param actionEvent
     */

    public void onDayComboBox(ActionEvent actionEvent) {
        dayCount = 0;

        for (Appointment a : aList) {

            if (a.getStartDT().getDayOfWeek().toString().equals(dayComboBox.getSelectionModel().getSelectedItem())) {
                dayCount++;
            }
        }
        totalDayOfWeek.setText("Total: " + dayCount);
    }

    public void onExit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/MainLanding.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1020, 750);
        stage.setTitle("Scheduling Application");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    // ComboBox set items
        typeComboBox.getItems().add("Planning Session");
        typeComboBox.getItems().add("De-Briefing");
        typeComboBox.getItems().add("Triage");
        typeComboBox.getItems().add("Follow-Up");

        monthComboBox.getItems().add("JANUARY");
        monthComboBox.getItems().add("FEBRUARY");
        monthComboBox.getItems().add("MARCH");
        monthComboBox.getItems().add("APRIL");
        monthComboBox.getItems().add("MAY");
        monthComboBox.getItems().add("JUNE");
        monthComboBox.getItems().add("JULY");
        monthComboBox.getItems().add("AUGUST");
        monthComboBox.getItems().add("SEPTEMBER");
        monthComboBox.getItems().add("OCTOBER");
        monthComboBox.getItems().add("NOVEMBER");
        monthComboBox.getItems().add("DECEMBER");

        dayComboBox.getItems().add("SUNDAY");
        dayComboBox.getItems().add("MONDAY");
        dayComboBox.getItems().add("TUESDAY");
        dayComboBox.getItems().add("WEDNESDAY");
        dayComboBox.getItems().add("THURSDAY");
        dayComboBox.getItems().add("FRIDAY");
        dayComboBox.getItems().add("SATURDAY");

        selContactNameComboBox.setItems(DBContact.getAllContacts());

        schApptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        schTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        schTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        schDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        schStartCol.setCellValueFactory(new PropertyValueFactory<>("startDT"));
        schEndCol.setCellValueFactory(new PropertyValueFactory<>("endDT"));
        schCustIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }
}
