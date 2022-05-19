package DAO;

import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the connection to the database for Contact objects
 */
public class DBContact {
    // Observable List variable holds Contact objects
    private static ObservableList<Contact> contactList = FXCollections.observableArrayList();
    public static ObservableList<Contact> getAllContacts() {

        try {
            contactList.clear();
            String sqlSelectAll = "SELECT * FROM Contacts";

            PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlSelectAll);

            ResultSet result = p.executeQuery();

            while(result.next()) {
                int contactId = result.getInt("Contact_ID");
                String contactName = result.getString("Contact_Name");
                String email = result.getString("Email");

                Contact c = new Contact(contactId, contactName, email);
                contactList.add(c);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contactList;
    }

}
