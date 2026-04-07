package dao;

import model.Bill;
import java.sql.*;
import java.util.ArrayList;

public class BillDAO {
    private Connection con;

    public BillDAO() {
        try {
            String dbUrl = "jdbc:mysql://localhost:3306/ClinicManagement_Mini";
            String dbClass = "com.mysql.cj.jdbc.Driver";
            Class.forName(dbClass);
            con = DriverManager.getConnection(dbUrl, "springstudent", "springstudent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Bill> searchBill(String idStr, String name, String phone, String dateStr, String amountStr, String method) {
        ArrayList<Bill> bills = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT b.ID, b.paymentDate, b.paymentAmount, b.paymentMethod, b.note, c.fullName, c.phone " +
                        "FROM tblBill b " +
                        "JOIN tblAppointment a ON b.ID = a.tblBillID " +
                        "JOIN tblClient c ON a.tblClientID = c.ID " +
                        "WHERE b.isValid = 1 "
        );

        //kiểm tra trường nhập
        if (!idStr.isEmpty()) sql.append("AND b.ID = ? ");
        if (!name.isEmpty()) sql.append("AND c.fullName LIKE ? ");
        if (!phone.isEmpty()) sql.append("AND c.phone LIKE ? ");
        if (!dateStr.isEmpty()) sql.append("AND b.paymentDate = ? ");
        if (!amountStr.isEmpty()) sql.append("AND b.paymentAmount = ? ");
        if (!method.equals("Tất cả")) sql.append("AND b.paymentMethod = ? ");

        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int index = 1;

            if (!idStr.isEmpty()) ps.setInt(index++, Integer.parseInt(idStr));
            if (!name.isEmpty()) ps.setString(index++, "%" + name + "%");
            if (!phone.isEmpty()) ps.setString(index++, "%" + phone + "%");
            if (!dateStr.isEmpty()) ps.setDate(index++, Date.valueOf(dateStr)); // Định dạng yyyy-mm-dd
            if (!amountStr.isEmpty()) ps.setFloat(index++, Float.parseFloat(amountStr));
            if (!method.equals("Tất cả")) ps.setString(index++, method);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("ID"));
                bill.setPaymentDate(rs.getDate("paymentDate"));
                bill.setPaymentAmount(rs.getFloat("paymentAmount"));
                bill.setPaymentMethod(rs.getString("paymentMethod"));
                bill.setNote(rs.getString("note"));
                bill.setClientName(rs.getString("fullName"));
                bill.setClientPhone(rs.getString("phone"));
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    public int createBill(Bill bill) {
        int generatedId = -1;
        String sql = "INSERT INTO tblBill (paymentDate, paymentAmount, paymentMethod, note, isValid, tblUserID) " +
                "VALUES (?, ?, ?, ?, 1, 1)"; // Mặc định Lễ tân ID = 1

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, bill.getPaymentDate());
            ps.setFloat(2, bill.getPaymentAmount());
            ps.setString(3, bill.getPaymentMethod());
            ps.setString(4, bill.getNote());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public Bill getBillDetail(int id) {
        Bill bill = null;

        String sql = "SELECT b.*, c.fullName, c.phone FROM tblBill b " +
                "JOIN tblAppointment a ON b.ID = a.tblBillID " +
                "JOIN tblClient c ON a.tblClientID = c.ID " +
                "WHERE b.ID = ? LIMIT 1";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bill = new Bill();
                bill.setId(rs.getInt("ID"));
                bill.setClientName(rs.getString("fullName"));
                bill.setClientPhone(rs.getString("phone"));
                bill.setPaymentDate(rs.getDate("paymentDate"));
                bill.setPaymentAmount(rs.getFloat("paymentAmount"));
                bill.setPaymentMethod(rs.getString("paymentMethod"));
                bill.setNote(rs.getString("note"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bill;
    }

    public boolean cancelBill(int id, String note) {
        String sql = "UPDATE tblBill SET isValid = 0, note = ? WHERE ID = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, note);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
