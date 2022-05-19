package DAO;

import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the connection to the database for User objects
 */
public class DBUser {
    private static ObservableList<User> userList = FXCollections.observableArrayList();
    public static ObservableList<User> getAllUsers() {

        try{
            userList.clear();
            String sqlSelectAll = "SELECT * FROM Users";

            PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlSelectAll);

            ResultSet result = p.executeQuery();

            while(result.next()) {
                int id = result.getInt("User_ID");
                String userName = result.getString("User_Name");
                String password = result.getString("Password");

                User u = new User(id, userName, password);
                userList.add(u);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userList;
    }
}
