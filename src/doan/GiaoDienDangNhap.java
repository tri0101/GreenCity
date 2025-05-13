// File: GiaoDienDangNhap.java
package doan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GiaoDienDangNhap extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JTextField phoneField;
    private JPanel normalLoginPanel;
    private JPanel phoneLoginPanel;
    private CardLayout cardLayout;

    public GiaoDienDangNhap() {
        setTitle("Đăng nhập hệ thống");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(new Color(50, 202, 50));
                g.setColor(Color.YELLOW);
                addLogo(g, 25, 50);

                g.setFont(new Font("SansSerif", Font.BOLD, 20));
                g.setColor(new Color(255,215,0));
                g.drawString("GREEN CITY", 65, 280);

                g.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g.setColor(Color.WHITE);
                g.drawString("DỊCH VỤ THU GOM RÁC TẠI NHÀ", 40, 305);
            }
        };
        leftPanel.setPreferredSize(new Dimension(250, 400));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right panel with CardLayout
        JPanel rightPanel = new JPanel();
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);
        rightPanel.setBackground(new Color(240, 240, 240));

        // Normal login panel
        normalLoginPanel = new JPanel(null);
        normalLoginPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("ĐĂNG NHẬP");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBounds(150, 30, 200, 30);
        normalLoginPanel.add(titleLabel);

        JLabel userLabel = new JLabel("Tài khoản");
        userLabel.setBounds(80, 80, 100, 25);
        normalLoginPanel.add(userLabel);

        userField = new JTextField();
        userField.setBounds(80, 105, 250, 30);
        userField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        normalLoginPanel.add(userField);

        JLabel passLabel = new JLabel("Mật khẩu");
        passLabel.setBounds(80, 145, 100, 25);
        normalLoginPanel.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(80, 170, 250, 30);
        normalLoginPanel.add(passField);

        // Phone login link
        JLabel phoneLoginLink = new JLabel("Đăng nhập bằng số điện thoại");
        phoneLoginLink.setBounds(80, 205, 200, 25);
        phoneLoginLink.setForeground(new Color(0, 102, 204));
        phoneLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        phoneLoginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(rightPanel, "phone");
            }
        });
        normalLoginPanel.add(phoneLoginLink);

        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setBounds(80, 235, 250, 35);
        loginButton.setBackground(new Color(20, 22, 58));
        loginButton.setForeground(Color.WHITE);
        normalLoginPanel.add(loginButton);

        // Phone login panel
        phoneLoginPanel = new JPanel(null);
        phoneLoginPanel.setBackground(new Color(240, 240, 240));

        JLabel phoneTitleLabel = new JLabel("ĐĂNG NHẬP ");
        phoneTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        phoneTitleLabel.setBounds(150, 30, 300, 30);
        phoneLoginPanel.add(phoneTitleLabel);

        JLabel phoneLabel = new JLabel("Số điện thoại");
        phoneLabel.setBounds(80, 80, 100, 25);
        phoneLoginPanel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(80, 105, 250, 30);
        phoneField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        phoneLoginPanel.add(phoneField);

        // Back to normal login link
        JLabel backToLoginLink = new JLabel("Đăng nhập bằng tài khoản");
        backToLoginLink.setBounds(80, 140, 200, 25);
        backToLoginLink.setForeground(new Color(0, 102, 204));
        backToLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLoginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(rightPanel, "normal");
            }
        });
        phoneLoginPanel.add(backToLoginLink);

        JButton phoneLoginButton = new JButton("Đăng nhập");
        phoneLoginButton.setBounds(80, 175, 250, 35);
        phoneLoginButton.setBackground(new Color(20, 22, 58));
        phoneLoginButton.setForeground(Color.WHITE);
        phoneLoginPanel.add(phoneLoginButton);

        // Add panels to right panel with CardLayout
        rightPanel.add(normalLoginPanel, "normal");
        rightPanel.add(phoneLoginPanel, "phone");

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword()).trim();

                try {
                    Connection conn = ConnectionJDBC.getConnection();
                    String sql = "SELECT * FROM NhanVienDieuPhoi WHERE Username = ? AND Password = ?";
                    
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, password);
                        ResultSet rs = pstmt.executeQuery();
                        
                        if (rs.next()) {
                            dispose();
                            new QuanLyRacThaiUI().setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(GiaoDienDangNhap.this,
                                "Tài khoản hoặc mật khẩu không đúng",
                                "Lỗi đăng nhập",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GiaoDienDangNhap.this,
                        "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        phoneLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = phoneField.getText().trim();
                
                if (phone.isEmpty()) {
                    JOptionPane.showMessageDialog(GiaoDienDangNhap.this,
                        "Vui lòng nhập số điện thoại!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    Connection conn = ConnectionJDBC.getConnection();
                    String sql = "SELECT MaChuThai, HoTen FROM ChuThai WHERE Sdt = ?";
                    
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, phone);
                        ResultSet rs = pstmt.executeQuery();
                        
                        if (rs.next()) {
                            String maChuThai = rs.getString("MaChuThai");
                            String hoTen = rs.getString("HoTen");
                            dispose();
                            new ChuThaiUI(hoTen, maChuThai).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(GiaoDienDangNhap.this,
                                "Số điện thoại này chưa đăng ký tài khoản!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GiaoDienDangNhap.this,
                        "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    public void addLogo(Graphics g, int x, int y) {
        URL url = GiaoDienDangNhap.class.getResource("/Image/logo.png");
        if (url == null) {
            System.err.println("Logo image file not found!");
            return;
        }
        ImageIcon logoIcon = new ImageIcon(url);
        Image logoImage = logoIcon.getImage();
        g.drawImage(logoImage, x, y, 200, 200, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GiaoDienDangNhap().setVisible(true));
    }
}
