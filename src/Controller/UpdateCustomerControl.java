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
import java.util.ResourceBundle;

/**
 * This class handles data in Update Customer screen view, for updating a Customer selected from Customer tableview
 * in Main Landing.
 */
public class UpdateCustomerControl implements Initializable {
    @FXML
    public AnchorPane updateCustomerForm;
    public Label updateCustomerTitle;
    public ComboBox<Division> updateDivisionCBox;
    public ComboBox<Countries> updateCountryCBox;
    public TextField updateCustIDText;
    public TextField updateCustNameTxt;
    public TextField updateCustAddressTxt;
    public TextField updatePostalTxt;
    public TextField updatePhoneTxt;
    public Button updateCustomerSaveButton;
    public Button updateCustomerCancelButton;

    public int currentCustId;
    public Countries currentCountry;
    ObservableList<Division> divList = DBDivision.getallDivisions();
    ObservableList<Countries> countryList = DBCountries.getAllCountries();

    /**
     * Method to check for any empty fields in customer form to ensure no empty or null entries.
     * @return boolean true if all fields are filled; false if any are empty or null
     */
    public boolean checkForEmptyFields() {

        // Check that all fields are filled in
        boolean filled = true;
        try {
            if (updateCustNameTxt.getText().isEmpty() || updateCustAddressTxt.getText().isEmpty() ||
                    updatePostalTxt.getText().isEmpty() || updatePhoneTxt.getText().isEmpty() ||
                    updateCountryCBox.getValue() == null || updateDivisionCBox.getValue() == null) {

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

    public void onUpSelectDivision(ActionEvent actionEvent) {
    }

    /**
     * Button handler utilizes Lambda expression in forEach statement to efficiently populate Division ComboBox based upon Country selection.
     * Utilizes Consumer Functional Interface and returns no value.
     * @param actionEvent Select from ComboBox
     */
    public void onUpSelectCountry(ActionEvent actionEvent) {
        updateDivisionCBox.getItems().clear();
        Countries selectedCountry = updateCountryCBox.getValue();


        // Populate Division ComboBox dependent of Country selection
        if (selectedCountry == null) {
            return;
        }

        if (selectedCountry.getId() == 1) {
            divList.forEach(
                    d -> {
                        if (d.getCountryId() == 1)
                            updateDivisionCBox.getItems().add(d);
                    }
            );
        } else if (selectedCountry.getId() == 2) {
            divList.forEach(
                    d -> {
                        if (d.getCountryId() == 2)
                            updateDivisionCBox.getItems().add(d);
                    }
            );
        } else if (selectedCountry.getId() == 3) {
            divList.forEach(
                    d -> {
                        if (d.getCountryId() == 3)
                            updateDivisionCBox.getItems().add(d);
                    }
            );
        }
    }

    /**
     * Button handler to confirm changes and update Customer fields by calling
     * Update Statement in DBCustomer to update in database
     * @param actionEvent Button click
     * @throws SQLException
     * @throws IOException
     */
    public void onSaveUpdateCustomer(ActionEvent actionEvent) throws SQLException, IOException {

        if (checkForEmptyFields()) {

            // If customer ID of Database member matches current Customer ID, update
            for (Customer c : DBCustomer.getAllCustomers()) {
                if (c.getCustId() == currentCustId) {
                    c.setName(updateCustNameTxt.getText());
                    c.setAddress(updateCustAddressTxt.getText());
                    c.setPostalCode(updatePostalTxt.getText());
                    c.setPhone(updatePhoneTxt.getText());
                    c.setDivId(updateDivisionCBox.getSelectionModel().getSelectedItem().getDivId());

                    // Call Update Statement in DBCustomer to update in database
                    DBCustomer.updateCustomer(c);
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
    }

    /**
     * Lambda method for Alert used to simplify and make Alert more efficent.  Utilizes Consumer Interface.
     * @param actionEvent
     */
    public void onCancelUpdateCustomer(ActionEvent actionEvent) throws IOException {
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

    /**
     * Method to send Customer selection from Tableview to Update Customer Form.
     * Utilizes lambda expression to populate Division ComboBox based upon current Country selection.
     * Lambda method used to simplify and make code more efficent.  Utilizes Consumer Functional Interface and returns no value.
     * @param selectCustomer Selected Customer from Customers Table
     */
    public void sendCustomer(Customer selectCustomer) {
        // Grab current Customer ID
        currentCustId = selectCustomer.getCustId();

        // Populate Update form with selected Customer attributes
        updateCustIDText.setText(String.valueOf(selectCustomer.getCustId()));
        updateCustNameTxt.setText(selectCustomer.getName());
        updateCustAddressTxt.setText(selectCustomer.getAddress());
        updatePostalTxt.setText(selectCustomer.getPostalCode());
        updatePhoneTxt.setText(selectCustomer.getPhone());

        // Set Division ComboBox
        for (Division d : divList) {
            if(selectCustomer.getDivId() == d.getDivId()) {
                updateDivisionCBox.setValue(d);

                // Set Country Combobox
                for (Countries c : countryList) {
                    if (d.getCountryId() == c.getId()) {
                        updateCountryCBox.setValue(c);
                        currentCountry = c;
                    }
                }
            }
        }

        // Populate Division ComboBox
        try {
            if (currentCountry.getId() == 1) {
                divList.forEach(
                        d -> {
                            if (d.getCountryId() == 1)
                                updateDivisionCBox.getItems().add(d);
                        }
                );
            } else if (currentCountry.getId() == 2) {
                divList.forEach(
                        d -> {
                            if (d.getCountryId() == 2)
                                updateDivisionCBox.getItems().add(d);
                        }
                );
            } else if (currentCountry.getId() == 3) {
                divList.forEach(
                        d -> {
                            if (d.getCountryId() == 3)
                                updateDivisionCBox.getItems().add(d);
                        }
                );
            }
        } catch (NullPointerException e) {
            // Do Nothing
        }
    }

    /**
     * Initialize Update Customer Controller class.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateCountryCBox.setItems(DBCountries.getAllCountries());
    }
}
