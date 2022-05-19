package DAO;

import Model.Division;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the connection to the database for Division objects (First Level Division)
 */
public class DBDivision {
    private static ObservableList<Division> divisionList = FXCollections.observableArrayList();
    public static ObservableList<Division> getallDivisions() {

        try {
            divisionList.clear();
            String sqlSelectAll = "SELECT * FROM First_Level_Divisions";

            PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlSelectAll);

            ResultSet result = p.executeQuery();

            while (result.next()) {
                int divId = result.getInt("Division_ID");
                String name = result.getString("Division");
                int countryId = result.getInt("Country_ID");

                Division d = new Division(divId, name, countryId);
                divisionList.add(d);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return divisionList;
    }
}
