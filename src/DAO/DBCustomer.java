package DAO;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * This class handles the connection to the database for Customer objects
 */
public class DBCustomer {
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public static ObservableList<Customer> getAllCustomers() {

        try{
            customerList.clear();
            String sqlSelectAll = "SELECT * FROM Customers";

            PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlSelectAll);

            ResultSet result = p.executeQuery();

            while(result.next()) {
                int custId = result.getInt("Customer_ID");
                String name = result.getString("Customer_Name");
                String address = result.getString("Address");
                String postalCode = result.getString("Postal_Code");
                String phone = result.getString("Phone");
                int divId = result.getInt("Division_ID");
                LocalDateTime createDate = result.getTimestamp("Create_Date").toLocalDateTime();

                Customer c = new Customer(custId, name, address, postalCode, phone, divId, createDate);
                customerList.add(c);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }

    /**
     * Method to add new Customer to database with Insert statement
     * @param customer Customer object
     * @throws SQLException
     */
    public static void addCustomer(Customer customer) throws SQLException {
        String sqlInsertCustomer = "INSERT INTO Customers(Customer_Name, Address, Postal_Code," +
                "Phone, Division_ID, Create_Date) VALUES(?,?,?,?,?,?)";

        PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlInsertCustomer);

        p.setString(1, customer.getName());
        p.setString(2, customer.getAddress());
        p.setString(3, customer.getPostalCode());
        p.setString(4, customer.getPhone());
        p.setString(5, String.valueOf(customer.getDivId()));
        p.setTimestamp(6, Timestamp.valueOf(customer.getCreateDate()));

        p.executeUpdate();
    }

    /**
     * Method to delete selected Customer from database with DELETE FROM statement
     * @param customer Customer object
     * @throws SQLException
     */

    public static void deleteCustomer(Customer customer) throws SQLException {
        String sqlDeleteCustomer = "DELETE FROM Customers WHERE Customer_ID = ?";

        PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlDeleteCustomer);

        p.setString(1, String.valueOf(customer.getCustId()));

        p.execute();

    }

    /**
     * Method to update selected Customer in database with UPDATE statement
     * @param customer Customer object
     * @throws SQLException
     */

    public static void updateCustomer(Customer customer) throws SQLException {
        String sqlUpdateCustomer = "UPDATE Customers SET Customer_Name = ?, Address = ?," +
                "Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";

        PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlUpdateCustomer);

        p.setString(1, customer.getName());
        p.setString(2, customer.getAddress());
        p.setString(3, customer.getPostalCode());
        p.setString(4, customer.getPhone());
        p.setString(5, String.valueOf(customer.getDivId()));
        p.setString(6, String.valueOf(customer.getCustId()));

        p.executeUpdate();
    }
}
