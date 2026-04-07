package view;

import dao.BillDAO;
import model.Bill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BillListFrm extends JFrame {
    private JPanel mainPanel;
    private JTextField txtSearchID;
    private JTextField txtSearchName;
    private JTextField txtSearchPhone;
    private JTextField txtSearchDate;
    private JTextField txtSearchAmount;
    private JComboBox<String> cbPaymentMethod;
    private JButton btnSearch;
    private JTable tblBillList;

    private DefaultTableModel tableModel;

    public BillListFrm() {
        setTitle("Danh sách hóa đơn");
        setContentPane(mainPanel);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // bảng
        String[] columns = {"ID", "Tên khách hàng", "SĐT", "Ngày lập", "Phương thức", "Thành tiền"};
        tableModel = new DefaultTableModel(columns, 0);
        tblBillList.setModel(tableModel);

        // nút tìm kiếm
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBills();
            }
        });

        searchBills();
    }

    private void searchBills() {
        // Lấy keyword
        String idStr = txtSearchID.getText().trim();
        String name = txtSearchName.getText().trim();
        String phone = txtSearchPhone.getText().trim();
        String dateStr = txtSearchDate.getText().trim();
        String amountStr = txtSearchAmount.getText().trim();
        String method = cbPaymentMethod.getSelectedItem() != null ? cbPaymentMethod.getSelectedItem().toString() : "Tất cả";

        BillDAO dao = new BillDAO();
        try {
            ArrayList<Bill> list = dao.searchBill(idStr, name, phone, dateStr, amountStr, method);

            tableModel.setRowCount(0); // Clear

            for (Bill bill : list) {
                tableModel.addRow(new Object[]{
                        bill.getId(),
                        bill.getClientName(),
                        bill.getClientPhone(),
                        bill.getPaymentDate(),
                        bill.getPaymentMethod(),
                        String.format("%,.0f", bill.getPaymentAmount())
                });
            }

            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu nào khớp với bộ lọc!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu đầu vào. Vui lòng kiểm tra lại định dạng Ngày (yyyy-mm-dd) hoặc Thành tiền/ID (số).", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BillListFrm().setVisible(true);
        });
    }
}