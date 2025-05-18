/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NvtgUI extends JFrame {
    private String maNvtg;
    private String tenNvtg;

    public NvtgUI() {
        setTitle("Hệ thống quản lý thu gom rác - Nhân viên thu gom");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 202, 50));
        headerPanel.setPreferredSize(new Dimension(1000, 60));

        JLabel titleLabel = new JLabel("NHÂN VIÊN THU GOM RÁC", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Create menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(20, 22, 58));
        menuPanel.setPreferredSize(new Dimension(200, 540));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Add menu buttons
        addMenuButton(menuPanel, "Lịch Thu Gom");
        addMenuButton(menuPanel, "Báo Cáo Thu Gom");
        addMenuButton(menuPanel, "Thông Tin Cá Nhân");
        
        // Add logout button at bottom of menu
        JButton logoutButton = new JButton("Đăng Xuất");
        logoutButton.setMaximumSize(new Dimension(180, 40));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new GiaoDienDangNhap().setVisible(true);
            }
        });
        menuPanel.add(Box.createVerticalGlue()); // Push logout button to bottom
        menuPanel.add(logoutButton);
        menuPanel.add(Box.createVerticalStrut(20)); // Add some padding at bottom

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);

        // Add welcome message
        JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống quản lý thu gom rác", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        contentPanel.add(welcomeLabel);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    private void addMenuButton(JPanel menuPanel, String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(20, 22, 58));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(40, 42, 78));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(20, 22, 58));
            }
        });

        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(button);
    }

    // Constructor with staff information
    public NvtgUI(String maNvtg, String tenNvtg) {
        this();
        this.maNvtg = maNvtg;
        this.tenNvtg = tenNvtg;
    }
}
