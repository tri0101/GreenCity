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
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public NvtgUI(String maNvtg, String tenNvtg) {
        this.maNvtg = maNvtg;
        this.tenNvtg = tenNvtg;
        setTitle("Nhân viên thu gom");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 202, 50));
        headerPanel.setPreferredSize(new Dimension(1000, 40));

        JLabel titleLabel = new JLabel("Nhân Viên thu gom", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new javax.swing.border.EmptyBorder(0, 20, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        JLabel staffInfoLabel = new JLabel(tenNvtg + " - Mã: " + maNvtg);
        staffInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        staffInfoLabel.setForeground(Color.WHITE);
        staffInfoLabel.setBorder(new javax.swing.border.EmptyBorder(5, 0, 0, 0));
        JButton logoutButton = new JButton("Đăng xuất");
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
        rightPanel.add(staffInfoLabel);
        rightPanel.add(logoutButton);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Sidebar menu
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(25, 42, 86));
        menuPanel.setPreferredSize(new Dimension(200, 0));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new javax.swing.border.EmptyBorder(20, 5, 20, 10));

        JButton lichButton = createMenuButton("Xem lịch thu gom");
        JButton settingsButton = createMenuButton("Cài đặt");

        menuPanel.add(lichButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(settingsButton);
        menuPanel.add(Box.createVerticalGlue());

        // Content panels
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createLichThuGomPanel(), "Xem lịch thu gom");
        contentPanel.add(createSettingsPanel(), "Cài đặt");

        // Add action for menu buttons
        lichButton.addActionListener(e -> cardLayout.show(contentPanel, "Xem lịch thu gom"));
        settingsButton.addActionListener(e -> cardLayout.show(contentPanel, "Cài đặt"));

        // Layout
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        cardLayout.show(contentPanel, "Xem lịch thu gom");
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(46, 64, 83));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(40, 42, 78));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(46, 64, 83));
            }
        });
        return button;
    }

    // Panel xem lịch thu gom (có thể thay bằng bảng dữ liệu thực tế)
    private JPanel createLichThuGomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Xem lịch thu gom", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(new javax.swing.border.EmptyBorder(0, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        // Thay phần này bằng bảng dữ liệu thực tế nếu muốn
        JLabel content = new JLabel("Nội dung bảng lịch thu gom sẽ hiển thị ở đây.", SwingConstants.CENTER);
        content.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    // Panel cài đặt (có thể thay bằng form đổi mật khẩu/thông tin cá nhân)
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Cài đặt", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(new javax.swing.border.EmptyBorder(0, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        // Staff information panel
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin tài khoản"));

        JPanel infoContentPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        infoContentPanel.setBackground(Color.WHITE);

        JLabel idLabel = new JLabel("Mã nhân viên:");
        JTextField idField = new JTextField(maNvtg);
        idField.setEditable(false);
        JLabel unitLabel = new JLabel("Mã đơn vị:");
        JTextField unitField = new JTextField(""); // Placeholder for unit code
        unitField.setEditable(false);
        JLabel nameLabel = new JLabel("Tên nhân viên:");
        JTextField nameField = new JTextField(tenNvtg);
        nameField.setEditable(false);
        JLabel genderLabel = new JLabel("Giới tính:");
        JTextField genderField = new JTextField(""); // Placeholder for gender
        genderField.setEditable(false);
        JLabel phoneLabel = new JLabel("Số điện thoại:");
        JTextField phoneField = new JTextField(""); // Placeholder for phone number
        phoneField.setEditable(false);
        JLabel leaderLabel = new JLabel("Mã trưởng nhóm:");
        JTextField leaderField = new JTextField(""); // Placeholder for leader code
        leaderField.setEditable(false);

        infoContentPanel.add(idLabel);
        infoContentPanel.add(idField);
        infoContentPanel.add(unitLabel);
        infoContentPanel.add(unitField);
        infoContentPanel.add(nameLabel);
        infoContentPanel.add(nameField);
        infoContentPanel.add(genderLabel);
        infoContentPanel.add(genderField);
        infoContentPanel.add(phoneLabel);
        infoContentPanel.add(phoneField);
        infoContentPanel.add(leaderLabel);
        infoContentPanel.add(leaderField);

        infoPanel.add(infoContentPanel, BorderLayout.CENTER);

        // Password change panel
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 10));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Đổi mật khẩu"));

        JPanel passwordContentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        passwordContentPanel.setBackground(Color.WHITE);

        JLabel oldPassLabel = new JLabel("Mật khẩu cũ:");
        JPasswordField oldPassField = new JPasswordField();
        JLabel newPassLabel = new JLabel("Mật khẩu mới:");
        JPasswordField newPassField = new JPasswordField();
        JLabel confirmPassLabel = new JLabel("Xác nhận mật khẩu mới:");
        JPasswordField confirmPassField = new JPasswordField();

        passwordContentPanel.add(oldPassLabel);
        passwordContentPanel.add(oldPassField);
        passwordContentPanel.add(newPassLabel);
        passwordContentPanel.add(newPassField);
        passwordContentPanel.add(confirmPassLabel);
        passwordContentPanel.add(confirmPassField);

        JButton changePassButton = new JButton("Đổi mật khẩu");
        changePassButton.addActionListener(e -> {
            // Logic to change password
            String oldPass = new String(oldPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());
            if (newPass.equals(confirmPass)) {
                // Update password logic here
                JOptionPane.showMessageDialog(this, "Mật khẩu đã được thay đổi.");
            } else {
                JOptionPane.showMessageDialog(this, "Mật khẩu mới không khớp.");
            }
        });

        passwordContentPanel.add(changePassButton);

        passwordPanel.add(passwordContentPanel, BorderLayout.CENTER);

        // Add both panels to the main panel
        JPanel combinedPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        combinedPanel.setBackground(Color.WHITE);
        combinedPanel.add(infoPanel);
        combinedPanel.add(passwordPanel);

        panel.add(combinedPanel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NvtgUI nvtgUI = new NvtgUI("NV001", "Nguyễn Văn A");
            nvtgUI.setVisible(true);
        });
    }
}
