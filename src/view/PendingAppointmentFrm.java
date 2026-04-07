// Code khung cơ bản cho PendingAppointmentFrm.java
package view;
import javax.swing.*;

public class PendingAppointmentFrm extends JFrame {
    private JPanel mainPanel;

    public PendingAppointmentFrm() {
        setTitle("Tìm kiếm ca khám chờ thanh toán");
        setContentPane(mainPanel);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Tắt form này không tắt màn hình chính
    }
}