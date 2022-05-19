package DAO;


import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;


/**
 * This class handles the connection to the database for Appointment objects
 */
public class DBAppt {
    private static ObservableList<Appointment> apptList = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getAllAppts() {
        try {
            apptList.clear();
            String sqlSelectAll = "SELECT * FROM Appointments";

            PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlSelectAll);

            ResultSet result = p.executeQuery();

            while(result.next()) {
                int apptId = result.getInt("Appointment_ID");
                String title = result.getString("Title");
                String description = result.getString("Description");
                String location = result.getString("Location");
                String type = result.getString("Type");
                LocalDateTime startDT = result.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDT = result.getTimestamp("End").toLocalDateTime();
                int contactId = result.getInt("Contact_ID");
                int custId = result.getInt("Customer_ID");
                int userId = result.getInt("User_ID");
                LocalDateTime createDate = result.getTimestamp("Create_Date").toLocalDateTime();


                Appointment a = new Appointment(apptId, title, description, location, type,
                        startDT, endDT, contactId, custId, userId, createDate);
                apptList.add(a);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return apptList;
    }

    /**
     * Method to add new Appointment to database with INSERT statement
     * @param appt Appointment object
     * @throws SQLException
     */
    public static void addAppt(Appointment appt) throws SQLException {
        String sqlInsertAppt = "INSERT INTO Appointments(Title, Description, Location, Type, Start, End, Customer_ID, " +
                "User_ID, Contact_ID, Create_Date) VALUES(?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlInsertAppt);

        p.setString(1, appt.getTitle());
        p.setString(2, appt.getDescription());
        p.setString(3, appt.getLocation());
        p.setString(4, appt.getType());
        p.setTimestamp(5, Timestamp.valueOf(appt.getStartDT()));
        p.setTimestamp(6, Timestamp.valueOf(appt.getEndDT()));
        p.setString(7, String.valueOf(appt.getCustId()));
        p.setString(8, String.valueOf(appt.getUserId()));
        p.setString(9, String.valueOf(appt.getContactId()));
        p.setTimestamp(10, Timestamp.valueOf(appt.getCreateDate()));

        p.executeUpdate();
    }

    /**
     * Method to update selected Appointment in database with UPDATE statement
     * @param appt Appointment object
     * @throws SQLException
     */

    public static void updateAppt(Appointment appt) throws SQLException {
        String sqlUpdateAppt = "UPDATE Appointments SET Title = ?, Description = ?," +
                "Location = ?, Type = ?, Start = ?, End = ?," +
                "Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlUpdateAppt);

        p.setString(1, appt.getTitle());
        p.setString(2, appt.getDescription());
        p.setString(3, appt.getLocation());
        p.setString(4, appt.getType());
        p.setTimestamp(5, Timestamp.valueOf(appt.getStartDT()));
        p.setTimestamp(6, Timestamp.valueOf(appt.getEndDT()));
        p.setString(7, String.valueOf(appt.getCustId()));
        p.setString(8, String.valueOf(appt.getUserId()));
        p.setString(9, String.valueOf(appt.getContactId()));
        p.setString(10, String.valueOf(appt.getApptId()));

        p.executeUpdate();
    }

    /**
     * Method to delete selected Appointment from database using DELETE FROM statement
     * @param appt Appointment object
     * @throws SQLException
     */
    public static void deleteAppt(Appointment appt) throws SQLException {
        String sqlDeleteAppt = "DELETE FROM Appointments WHERE Appointment_ID = ?";

        PreparedStatement p = DBConnection.getConnection().prepareStatement(sqlDeleteAppt);

        p.setString(1, String.valueOf(appt.getApptId()));

        p.execute();

    }
}
