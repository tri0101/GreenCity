package doan;

import javax.swing.*;
import java.awt.*;

public class TruongNhomUI extends JFrame {
    public TruongNhomUI(String maNvtg, String tenNvtg) {
        setTitle("Giao diện Trưởng Nhóm");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Xin chào Trưởng nhóm: " + tenNvtg + " (Mã: " + maNvtg + ")", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        add(label, BorderLayout.CENTER);
    }
}