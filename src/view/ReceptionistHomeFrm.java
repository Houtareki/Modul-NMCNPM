package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReceptionistHomeFrm extends JFrame {
    private JPanel mainPanel;
    private JButton btnPayment; // Nút Thanh toán (Tra cứu ca khám)
    private JButton btnViewBills; // Nút Xem danh sách hóa đơn

    public ReceptionistHomeFrm() {
        setTitle("Hệ thống Quản lý Phòng Khám - Lễ Tân");
        setContentPane(mainPanel);
        setSize(500, 350);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng app khi tắt màn hình chính

        // 1. Chức năng Thanh toán -> Dẫn ra PendingAppointmentFrm
        btnPayment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở giao diện danh sách ca chờ thanh toán
                PendingAppointmentFrm pendingFrm = new PendingAppointmentFrm();
                pendingFrm.setVisible(true);
            }
        });

        // 2. Chức năng Xem danh sách hóa đơn -> Dẫn ra BillListFrm
        btnViewBills.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở giao diện danh sách hóa đơn
                BillListFrm billListFrm = new BillListFrm();
                billListFrm.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReceptionistHomeFrm().setVisible(true);
        });
    }
}