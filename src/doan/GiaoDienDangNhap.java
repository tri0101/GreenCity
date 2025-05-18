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
    private JPanel loginPanel;

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

        // Login panel
        loginPanel = new JPanel(null);
        loginPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("ĐĂNG NHẬP");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBounds(150, 30, 200, 30);
        loginPanel.add(titleLabel);

        JLabel userLabel = new JLabel("Tài khoản");
        userLabel.setBounds(80, 80, 100, 25);
        loginPanel.add(userLabel);

        userField = new JTextField();
        userField.setBounds(80, 105, 250, 30);
        userField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        loginPanel.add(userField);

        JLabel passLabel = new JLabel("Mật khẩu");
        passLabel.setBounds(80, 145, 100, 25);
        loginPanel.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(80, 170, 250, 30);
        loginPanel.add(passField);

        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setBounds(80, 220, 250, 35);
        loginButton.setBackground(new Color(20, 22, 58));
        loginButton.setForeground(Color.WHITE);
        loginPanel.add(loginButton);

        // Add action listener for login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(GiaoDienDangNhap.this,
                        "Vui lòng nhập đầy đủ thông tin!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    Connection conn = ConnectionJDBC.getConnection();
                    String sql;
                    String userType;
                    
                    if (username.toLowerCase().startsWith("ct_")) {
                        sql = "SELECT * FROM ChuThai WHERE Username = ? AND Password = ?";
                        userType = "ChuThai";
                    } else if (username.toLowerCase().startsWith("nvtg_")) {
                        sql = "SELECT * FROM NhanVienThuGom WHERE Username = ? AND Password = ?";
                        userType = "NhanVienThuGom";
                    } else if (username.toLowerCase().startsWith("nvdp_")) {
                        sql = "SELECT * FROM NhanVienDieuPhoi WHERE Username = ? AND Password = ?";
                        userType = "NhanVienDieuPhoi";
                    } else {
                        JOptionPane.showMessageDialog(GiaoDienDangNhap.this,
                            "Tên đăng nhập hoặc mật khẩu không đúng",
                            "Lỗi đăng nhập",
                            JOptionPane.ERROR_MESSAGE);
                        passField.setText("");
                        return;
                    }
                    
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, password);
                        ResultSet rs = pstmt.executeQuery();
                        
                        if (rs.next()) {
                            dispose();
                            switch (userType) {
                                case "ChuThai":
                                    String maChuThai = rs.getString("MaChuThai");
                                    String hoTen = rs.getString("HoTen");
                                    new ChuThaiUI(hoTen, maChuThai).setVisible(true);
                                    break;
                                case "NhanVienThuGom":
                                    String maNvtg = rs.getString("MaNvtg");
                                    String tenNvtg = rs.getString("TenNvtg");
                                    new NvtgUI(maNvtg, tenNvtg).setVisible(true);
                                    break;
                                case "NhanVienDieuPhoi":
                                    String maNvdp = rs.getString("MaNvdp");
                                    String tenNvdp = rs.getString("TenNvdp");
                                    new QuanLyRacThaiUI().setVisible(true);
                                    break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(GiaoDienDangNhap.this,
                                "Tên đăng nhập hoặc mật khẩu không đúng",
                                "Lỗi đăng nhập",
                                JOptionPane.ERROR_MESSAGE);
                            passField.setText("");
                            if (userField.getText().trim().isEmpty()) {
                                userField.requestFocus();
                            } else {
                                passField.requestFocus();
                            }
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

        mainPanel.add(loginPanel, BorderLayout.CENTER);
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
