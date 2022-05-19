package Controller;

import DAO.DBCountries;
import DAO.DBCustomer;
import DAO.DBDivision;
import Model.Countries;
import Model.Customer;
import Model.Division;
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
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class handles data for Add new Customer screen view.
 */
public class AddCustomerControl implements Initializable {
    @FXML
    public AnchorPane addCustomerForm;
    public Label addCustomerTitle;
    public ComboBox<Division> addDivisionCBox;
    public ComboBox<Countries> addCountryCBox;
    public TextField addCustIDText;
    public TextField addCustNameTxt;
    public TextField addCustAddressTxt;
    public TextField addPostalTxt;
    public TextField addPhoneTxt;
    public Button addCustomerSaveButton;
    public Button addCustomerCancelButton;

    LocalDateTime now = LocalDateTime.now();
    ObservableList<Division> divList = DBDivision.getallDivisions();

    /**
     * Method to check for any empty fields in customer form to ensure no empty or null entries.
     * @return boolean true if all fields are filled; false if any are empty or null
     */
    public boolean checkForEmptyFields() {

        // Check that all fields are filled in
        boolean filled = true;
        try {
            if (addCustNameTxt.getText().isEmpty() || addCustAddressTxt.getText().isEmpty() ||
            addPostalTxt.getText().isEmpty() || addPhoneTxt.getText().isEmpty() ||
            addCountryCBox.getValue() == null || addDivisionCBox.getValue() == null) {

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


    public void onAddSelectDivision(ActionEvent actionEvent) {
    }

    /**
     * Button handler utilizes Lambda expression in forEach statement to populate Division ComboBox based upon Country selection
     * Lambda method used to simplify and make code more efficent.  Utilizes Consumer Functional Interface and returns no value.
     * @param actionEvent Select from ComboBox
     */
    public void onAddSelectCountry(ActionEvent actionEvent) {
        addDivisionCBox.getItems().clear();
        Countries selectedCountry = addCountryCBox.getValue();

        // Populate Division ComboBox dependent of Country selection
        if (selectedCountry == null) {
            return;
        }

        if (selectedCountry.getId() == 1) {
            divList.forEach(
                    d -> {
                        if (d.getCountryId() == 1)
                            addDivisionCBox.getItems().add(d);
                    }
            );
        } else if (selectedCountry.getId() == 2) {
            divList.forEach(
                    d -> {
                        if (d.getCountryId() == 2)
                            addDivisionCBox.getItems().add(d);
                    }
            );
        } else if (selectedCountry.getId() == 3) {
            divList.forEach(
                    d -> {
                        if (d.getCountryId() == 3)
                            addDivisionCBox.getItems().add(d);
                    }
            );
        }
    }

    /**
     * Button handler to Save data fields to Customer object, create new Customer object,
     * and add object to the database.
     * @param actionEvent Button click
     * @throws SQLException
     * @throws IOException
     */
    public void onSaveAddCustomer(ActionEvent actionEvent) throws SQLException, IOException {

        if (checkForEmptyFields()) {

            // Create new Customer Object

            int custId = 0;
            String name = addCustNameTxt.getText();
            String address = addCustAddressTxt.getText();
            String postalCode = addPostalTxt.getText();
            String phone = addPhoneTxt.getText();
            int divId = addDivisionCBox.getSelectionModel().getSelectedItem().getDivId();
            LocalDateTime createDate = now;

            Customer newCust = new Customer(custId, name, address, postalCode, phone, divId, createDate);

            DBCustomer.addCustomer(newCust);

            // Return to Main Landing Page
            Parent root = FXMLLoader.load(getClass().getResource("/View/MainLanding.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1020, 750);
            stage.setTitle("Scheduling Application");
            stage.setScene(scene);
            stage.show();

        }
    }

    /**
     * Lambda method for Alert used to simplify and make code more efficent.  Utilizes Consumer Functional Interface and returns no value.
     * @param actionEvent
     */

    public void onCancelAddCustomer(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any changes will be lost. Do you wish to continue?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {

                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/View/MainLanding.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 1020, 750);
                    stage.setTitle("Scheduling Application");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }});
        }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addCountryCBox.setItems(DBCountries.getAllCountries());

    }
}