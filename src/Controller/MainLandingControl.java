package Controller;

import DAO.DBAppt;
import DAO.DBCustomer;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main Landing Control Class.  Main view of all Appointments and Customer records, with user ability
 * to add/update/delete Appointments and Customers.  Navigates to and from Appointment, Customer, and Report views.
 */
public class MainLandingControl implements Initializable {
    @FXML
    public AnchorPane mainLandingScreen;
    public TableView<Appointment> apptsTable;
    public TableColumn<Appointment, Integer> apptIdCol;
    public TableColumn<Appointment, String> titleCol;
    public TableColumn<Appointment, String> descripCol;
    public TableColumn<Appointment, String> locationCol;
    public TableColumn<Appointment, String> contactCol;
    public TableColumn<Appointment, String> apptTypeCol;
    public TableColumn<Appointment, LocalDate> startDTCol;
    public TableColumn<Appointment, LocalDate> endDTCol;
    public TableColumn<Appointment, String> apptCustIdCol;
    public TableColumn<Appointment, String> userIdCol;
    public TableView<Customer> customersTable;
    public TableColumn<Customer, Integer> customerCustIdCol;
    public TableColumn<Customer, String> custNameCol;
    public TableColumn<Customer, String> custAddressCol;
    public TableColumn<Customer, String> custPostalCol;
    public TableColumn<Customer, String> custPhoneCol;
    public TableColumn<Customer, String> custDivisionCol;
    public Button updateApptMain;
    public Button addApptMain;
    public Button deleteApptMain;
    public Button updateCustomerMain;
    public Button addCustomerMain;
    public Button deleteCustomerMain;
    public RadioButton viewAllApptsRadio;
    public RadioButton viewByMonthApptsRadio;
    public RadioButton viewByWeekApptsRadio;
    public ToggleGroup apptView;
    public Label currentTime;

    public static Appointment apptSelection;
    public static Customer customerSelection;
    public LocalDateTime now = LocalDateTime.now();
    public Button reportsButton;
    private ObservableList<Appointment> customerAppts = FXCollections.observableArrayList();
    public ObservableList<Appointment> aList = DBAppt.getAllAppts();

    /**
     * Method to instantiate Update Appointment form once an Appointment is selected from tableview and Update button is clicked.
     * Data fields from selected object are populated in Update form.
     * @param actionEvent Button click
     * @throws IOException
     */

    public void onUpdateApptMain(ActionEvent actionEvent) throws IOException {
        // Load selected Appointment data into UpdateApptControl


        // Instantiate FXML Loader with specified View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/UpdateApptForm.fxml"));
        loader.load();

        // Get selected Appt from Tableview
        apptSelection = apptsTable.getSelectionModel().getSelectedItem();

        // Ignore Update click if no Customer is selected
        if (apptSelection == null)
            return;

        // Send data from tableview selection to Update Appt Controller
        UpdateApptControl updateSelected = loader.getController();
        updateSelected.sendAppt(apptSelection);

        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Update Appointment");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Method to instantiate Add Appointment Form.
     * @param actionEvent Button click
     * @throws IOException
     */

    public void onAddApptMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/AddApptForm.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 550);
        stage.setTitle("Add New Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to delete Appointment object selected in tableview. Calls Delete statement from DBAppt
     * to delete from database.
     * @param actionEvent Button click
     * @throws SQLException
     */

    public void onDeleteApptMain(ActionEvent actionEvent) throws SQLException {

        apptSelection = apptsTable.getSelectionModel().getSelectedItem();

        if (apptSelection == null) {
            return;
        }

        // Alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish" +
                " to delete this scheduled " + apptSelection.getType() + " appointment?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            // Call deleteAppt method to delete from database
            DBAppt.deleteAppt(apptSelection);

            // Reset Tableview
            apptsTable.getItems().clear();
            apptsTable.getItems().addAll(DBAppt.getAllAppts());

            // Confirmation alert
            Alert confirm = new Alert(Alert.AlertType.INFORMATION,apptSelection.getType() + " appointment with Appt ID " + apptSelection.getApptId() + " was cancelled.");
            confirm.show();

        }
    }

    /**
     * Method to instantiate Update Customer form once an Appointment is selected from tableview and Update button is clicked.
     * Data fields from selected object are populated in Update form.
     * @param actionEvent
     * @throws IOException
     */
    public void onUpdateCustomerMain(ActionEvent actionEvent) throws IOException {

        // Instantiate FXML Loader with specified View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/UpdateCustomerForm.fxml"));
        loader.load();

        // Get selected Customer from Tableview
        customerSelection = customersTable.getSelectionModel().getSelectedItem();

        // Ignore Update click if no Customer is selected
        if (customerSelection == null)
            return;

                    // Send data from Database not Tableview?
        UpdateCustomerControl updateSelected = loader.getController();
        updateSelected.sendCustomer(customerSelection);

        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Update Customer");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Method to instantiate Add Customer Form.
     * @param actionEvent Button click.
     * @throws IOException
     */

    public void onAddCustomerMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/AddCustomerForm.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Add New Customer");
        stage.setScene(scene);
        stage.show();
    }

    public ObservableList<Appointment> getCustomerAppts() {
        customerAppts.clear();

        for(Appointment a : aList) {
            if (a.getCustId() == customerSelection.getCustId()) {
                    customerAppts.add(a);
            }
        }
        return customerAppts;
    }

    /**
     * Method to delete Customer object selected in tableview. Calls Delete statement from DBCustomer
     * to delete from database. Alerts user if selected Customer has dependent Appointments.
     * If user confirms delete, associated Appointments will be deleted first, then the Customer will be deleted.
     * @param actionEvent Button click
     * @throws SQLException
     */
    public void onDeleteCustomerMain(ActionEvent actionEvent) throws SQLException {
        customerSelection = customersTable.getSelectionModel().getSelectedItem();

        if (customerSelection == null) {
            return;
        }

        // Alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish" +
                " to delete this customer with Customer ID " + customerSelection.getCustId() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Check for Appointment connected to this Customer
            getCustomerAppts();
            int apptCount = getCustomerAppts().size();

            // If no active Appointments, delete Customer
            if (apptCount == 0) {
                DBCustomer.deleteCustomer(customerSelection);

                Alert delAlert = new Alert(Alert.AlertType.INFORMATION, "This customer has " +
                        "been removed.");
                delAlert.showAndWait();

                // Reset Tableview
                customersTable.getItems().clear();
                customersTable.getItems().addAll(DBCustomer.getAllCustomers());

            // If Customer has one or more active Appointments...
            } else {
                // Confirm deletion of both
                Alert apptAlert = new Alert(Alert.AlertType.CONFIRMATION, "This customer " +
                        "has " + apptCount + " scheduled appointments. These appointments will be deleted " +
                        "along with the customer record. Do you wish to continue?");
                Optional<ButtonType> result2 = apptAlert.showAndWait();

                // Delete related Appointments
                if (result2.isPresent() && result2.get() == ButtonType.OK) {
                    for (Appointment t : getCustomerAppts()) {
                        DBAppt.deleteAppt(t);
                    }
                    // Delete Customer after deletion of related Appts
                    DBCustomer.deleteCustomer(customerSelection);

                    Alert delConfirm = new Alert(Alert.AlertType.INFORMATION, "This customer and " +
                            apptCount + " related appointments have been removed.");
                    delConfirm.showAndWait();

                    // Reset Tableviews
                    apptsTable.getItems().clear();
                    apptsTable.getItems().addAll(DBAppt.getAllAppts());
                    customersTable.getItems().clear();
                    customersTable.getItems().addAll(DBCustomer.getAllCustomers());

                } else {
                    Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION, "This customer and " +
                            "any related appointments have been left unchanged.");
                    cancelAlert.showAndWait();
                }
            }
        } else {
            Alert nonAlert = new Alert(Alert.AlertType.INFORMATION, "This customer has " +
                    "not been deleted.");
            nonAlert.showAndWait();
        }
    }

    public void onViewAllAppts(ActionEvent actionEvent) {
        apptsTable.getItems().clear();
        apptsTable.getItems().addAll(DBAppt.getAllAppts());

    }

    public void onViewByMonthAppts(ActionEvent actionEvent) {
        // Clear tableview contents
        apptsTable.getItems().clear();

        //Filter to current month (1-31st)
        for (Appointment a : aList) {
            if (a.getStartDT().getMonth() == now.getMonth()) {
                apptsTable.getItems().add(a);

            }
        }
    }

    public void onViewByWeekAppts(ActionEvent actionEvent) {
        // Clear tableview contents
        apptsTable.getItems().clear();

        //Filter to current week
        for (Appointment a : aList) {
            if (a.getStartDT().getDayOfMonth() >= now.getDayOfMonth() &&
                a.getStartDT().getDayOfMonth() <= (now.getDayOfMonth() + 6)) {
                    apptsTable.getItems().add(a);
            }
        }
    }


    DateTimeFormatter formatCurrent = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void onReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/ReportsView.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 750, 600);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Method to initialize Main Landing Controller
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentTime.setText("Current Time is: " + LocalDateTime.now().format(formatCurrent));

   apptsTable.getItems().addAll(DBAppt.getAllAppts());

   apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
   titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
   descripCol.setCellValueFactory(new PropertyValueFactory<>("description"));
   locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
   contactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
   apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
   startDTCol.setCellValueFactory(new PropertyValueFactory<>("startDT"));
   endDTCol.setCellValueFactory(new PropertyValueFactory<>("endDT"));
   apptCustIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
   userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

   customersTable.getItems().addAll(DBCustomer.getAllCustomers());

   customerCustIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
   custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
   custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
   custPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
   custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
   custDivisionCol.setCellValueFactory(new PropertyValueFactory<>("divId"));

}

}
