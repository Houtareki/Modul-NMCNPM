package dao;

import model.Appointment;

import java.sql.*;
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

    public void updateStatus(ArrayList<Integer> appointmentIds, int billId, String status) {
        if (appointmentIds.isEmpty()) {
            return;
        }
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < appointmentIds.size(); i++) {
            placeholders.append("?");
            if (i < appointmentIds.size() - 1) placeholders.append(",");
        }

        String sql = "UPDATE tblAppointment SET status = ?, tblBillID = ? WHERE ID IN (" + placeholders + ")";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, status);

            if (status.equalsIgnoreCase("Pending")) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, billId);
            }

            for (int i = 0; i < appointmentIds.size(); i++) {
                ps.setInt(i + 3, appointmentIds.get(i));
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getAppointmentIdsById(int billId) {
        ArrayList<Integer> appointmentIds = new ArrayList<>();
        String sql = "SELECT ID FROM tblAppointment WHERE tblBillID = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                appointmentIds.add(rs.getInt("ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentIds;
    }

}
