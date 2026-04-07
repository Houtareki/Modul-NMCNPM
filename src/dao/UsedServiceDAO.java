package dao;

import model.UsedService;

import java.sql.*;
import java.util.ArrayList;

public class UsedServiceDAO {
    private Connection con;

    public UsedServiceDAO() {
        try {
            String dbUrl = "jdbc:mysql://localhost:3306/ClinicManagement_Mini";
            con = DriverManager.getConnection(dbUrl, "springstudent", "springstudent");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public ArrayList<UsedService> getUsedServicesByAppointmentIds(ArrayList<Integer> appointmentIds) {
        ArrayList<UsedService> usedServices = new ArrayList<>();
        if (appointmentIds.isEmpty()) return usedServices;

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < appointmentIds.size(); i++) {
            placeholders.append("?");
            if (i < appointmentIds.size() - 1) placeholders.append(",");
        }

        String sql = "SELECT us.ID, c.fullName, s.name, us.quantity, s.price " +
                "FROM tblUsedService us " +
                "JOIN tblService s ON us.tblServiceID = s.ID " +
                "JOIN tblAppointment a ON us.tblAppointmentID = a.ID " +
                "JOIN tblClient c ON a.tblClientID = c.ID " +
                "WHERE us.tblAppointmentID IN (" + placeholders + ")";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            for (int i = 0; i < appointmentIds.size(); i++) {
                ps.setInt(i + 1, appointmentIds.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UsedService usedService = new UsedService();
                usedService.setId(rs.getInt("ID"));
                usedService.setClientName(rs.getString("fullName"));
                usedService.setServiceName(rs.getString("name"));
                usedService.setQuantity(rs.getInt("quantity"));
                usedService.setPrice(rs.getFloat("price"));
                usedServices.add(usedService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usedServices;
    }
}
