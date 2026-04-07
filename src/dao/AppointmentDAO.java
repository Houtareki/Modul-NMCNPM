package dao;

import model.Appointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AppointmentDAO {
    private Connection con;

    public AppointmentDAO() {
        try {
            String dbUrl = "jdbc:mysql://localhost:3306/ClinicManagement_Mini";
            con = DriverManager.getConnection(dbUrl, "springstudent", "springstudent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Appointment> searchPendingAppointment(String keyword) {
        ArrayList<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT a.ID, a.bookingDate, a.bookingTime, a.status, a.note, " +
                "c.fullName, c.phone, c.CIN " +
                "FROM tblAppointment a " +
                "JOIN tblClient c ON a.tblClientID = c.ID " +
                "WHERE a.status = 'Pending' " +
                "AND (c.fullName LIKE ? OR c.phone LIKE ? OR c.CIN LIKE ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            String searchKeyWord = "%" + keyword + "%";
            ps.setString(1, searchKeyWord);
            ps.setString(2, searchKeyWord);
            ps.setString(3, searchKeyWord);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("ID"));
                appointment.setBookingDate(rs.getDate("bookingDate"));
                appointment.setBookingTime(rs.getString("bookingTime"));
                appointment.setStatus(rs.getString("status"));
                appointment.setNote(rs.getString("note"));
                appointment.setClientName(rs.getString("fullName"));
                appointment.setClientPhone(rs.getString("phone"));
                appointment.setClientCIN(rs.getString("CIN"));
                appointments.add(appointment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }
}
