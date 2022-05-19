package DAO;

import Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the connection to the database for Countries objects
 */
public class DBCountries {
    // Observable List variable holds Countries objects
    private static ObservableList<Countries> countryList = FXCollections.observableArrayList();
    public static ObservableList<Countries> getAllCountries() {

        try {
            countryList.clear();
            String sql = "SELECT * FROM Countries";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet result = ps.executeQuery();

            while(result.next()) {
                int countryId = result.getInt("Country_ID");
                String countryName = result.getString("Country");
                Countries c = new Countries(countryId, countryName);
                countryList.add(c);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return countryList;
    }

}
