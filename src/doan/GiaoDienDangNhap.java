// File: GiaoDienDangNhap.java
package doan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;

public class GiaoDienDangNhap extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JPanel loginPanel;
    private JPanel forgotPasswordPanel;
    private JPanel resetPasswordPanel;
    private JPanel newPasswordPanel;
    private JPanel registerPanel;
    private CardLayout cardLayout;
    private String currentEmail;
    private JTextField emailField;
    private JTextField otpField;
    private JPasswordField newPassField;
    private JPasswordField confirmPassField;

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

        // Panel chính bên phải
        JPanel rightPanel = new JPanel();
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);
        rightPanel.setBackground(new Color(240, 240, 240));

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

        // Thêm link quên mật khẩu
        JLabel forgotPassLabel = new JLabel("Quên mật khẩu?");
        forgotPassLabel.setBounds(80, 210, 100, 20);
        forgotPassLabel.setForeground(new Color(0, 102, 204));
        forgotPassLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearAllFields();
                cardLayout.show(rightPanel, "FORGOT_PASSWORD");
            }
        });
        loginPanel.add(forgotPassLabel);

        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setBounds(80, 240, 250, 35);
        loginButton.setBackground(new Color(20, 22, 58));
        loginButton.setForeground(Color.WHITE);
        loginPanel.add(loginButton);

        JButton registerButton = new JButton("Đăng ký");
        registerButton.setBounds(80, 285, 250, 35);
        registerButton.setBackground(new Color(20, 22, 58));
        registerButton.setForeground(Color.WHITE);
        loginPanel.add(registerButton);

        // Forgot Password Panel
        forgotPasswordPanel = new JPanel(null);
        forgotPasswordPanel.setBackground(new Color(240, 240, 240));

        JLabel forgotTitleLabel = new JLabel("QUÊN MẬT KHẨU");
        forgotTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        forgotTitleLabel.setBounds(120, 30, 200, 30);
        forgotPasswordPanel.add(forgotTitleLabel);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(80, 80, 100, 25);
        forgotPasswordPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(80, 105, 250, 30);
        emailField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        forgotPasswordPanel.add(emailField);

        JButton continueButton = new JButton("Tiếp tục");
        continueButton.setBounds(80, 150, 120, 35);
        continueButton.setBackground(new Color(20, 22, 58));
        continueButton.setForeground(Color.WHITE);
        forgotPasswordPanel.add(continueButton);

        JButton backButton = new JButton("Quay lại");
        backButton.setBounds(210, 150, 120, 35);
        backButton.setBackground(new Color(20, 22, 58));
        backButton.setForeground(Color.WHITE);
        forgotPasswordPanel.add(backButton);

        // Reset Password Panel
        resetPasswordPanel = new JPanel(null);
        resetPasswordPanel.setBackground(new Color(240, 240, 240));

        JLabel resetTitleLabel = new JLabel("ĐẶT LẠI MẬT KHẨU");
        resetTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        resetTitleLabel.setBounds(100, 30, 200, 30);
        resetPasswordPanel.add(resetTitleLabel);

        JLabel otpLabel = new JLabel("Nhập mã OTP");
        otpLabel.setBounds(80, 80, 100, 25);
        resetPasswordPanel.add(otpLabel);

        otpField = new JTextField();
        otpField.setBounds(80, 105, 250, 30);
        otpField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        resetPasswordPanel.add(otpField);

        JButton verifyButton = new JButton("Xác nhận");
        verifyButton.setBounds(80, 150, 120, 35);
        verifyButton.setBackground(new Color(20, 22, 58));
        verifyButton.setForeground(Color.WHITE);
        resetPasswordPanel.add(verifyButton);

        JButton backToLoginButton = new JButton("Quay lại");
        backToLoginButton.setBounds(210, 150, 120, 35);
        backToLoginButton.setBackground(new Color(20, 22, 58));
        backToLoginButton.setForeground(Color.WHITE);
        resetPasswordPanel.add(backToLoginButton);

        // New Password Panel
        newPasswordPanel = new JPanel(null);
        newPasswordPanel.setBackground(new Color(240, 240, 240));

        JLabel newPassTitleLabel = new JLabel("ĐẶT MẬT KHẨU MỚI");
        newPassTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        newPassTitleLabel.setBounds(100, 30, 200, 30);
        newPasswordPanel.add(newPassTitleLabel);

        JLabel newPassLabel = new JLabel("Mật khẩu mới:");
        newPassLabel.setBounds(80, 80, 100, 25);
        newPasswordPanel.add(newPassLabel);

        newPassField = new JPasswordField();
        newPassField.setBounds(80, 105, 250, 30);
        newPassField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        newPasswordPanel.add(newPassField);

        JLabel confirmPassLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPassLabel.setBounds(80, 145, 150, 25);
        newPasswordPanel.add(confirmPassLabel);

        confirmPassField = new JPasswordField();
        confirmPassField.setBounds(80, 170, 250, 30);
        confirmPassField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        newPasswordPanel.add(confirmPassField);

        JButton saveButton = new JButton("Lưu");
        saveButton.setBounds(80, 220, 120, 35);
        saveButton.setBackground(new Color(20, 22, 58));
        saveButton.setForeground(Color.WHITE);
        newPasswordPanel.add(saveButton);

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setBounds(210, 220, 120, 35);
        cancelButton.setBackground(new Color(20, 22, 58));
        cancelButton.setForeground(Color.WHITE);
        newPasswordPanel.add(cancelButton);

        // Register Panel
        registerPanel = new JPanel(null);
        registerPanel.setBackground(new Color(240, 240, 240));

        JLabel registerTitleLabel = new JLabel("ĐĂNG KÝ TÀI KHOẢN");
        registerTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        registerTitleLabel.setBounds(120, 30, 250, 30);
        registerPanel.add(registerTitleLabel);

        // Họ tên
        JLabel hoTenLabel = new JLabel("Họ tên:");
        hoTenLabel.setBounds(80, 80, 100, 25);
        registerPanel.add(hoTenLabel);

        JTextField hoTenField = new JTextField();
        hoTenField.setBounds(80, 105, 250, 30);
        hoTenField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        registerPanel.add(hoTenField);

        // Địa chỉ
        JLabel diaChiLabel = new JLabel("Địa chỉ:");
        diaChiLabel.setBounds(80, 145, 100, 25);
        registerPanel.add(diaChiLabel);

        JTextField diaChiField = new JTextField();
        diaChiField.setBounds(80, 170, 250, 30);
        diaChiField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        registerPanel.add(diaChiField);

        // Số điện thoại
        JLabel sdtLabel = new JLabel("Số điện thoại:");
        sdtLabel.setBounds(80, 210, 100, 25);
        registerPanel.add(sdtLabel);

        JTextField sdtField = new JTextField();
        sdtField.setBounds(80, 235, 250, 30);
        sdtField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        registerPanel.add(sdtField);

        // Email
        JLabel emailRegLabel = new JLabel("Email:");
        emailRegLabel.setBounds(80, 275, 100, 25);
        registerPanel.add(emailRegLabel);

        JTextField emailRegField = new JTextField();
        emailRegField.setBounds(80, 300, 250, 30);
        emailRegField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        registerPanel.add(emailRegField);

        // Loại chủ thải
        JLabel loaiChuThaiLabel = new JLabel("Loại chủ thải:");
        loaiChuThaiLabel.setBounds(80, 340, 100, 25);
        registerPanel.add(loaiChuThaiLabel);

        String[] loaiChuThai = {"Hộ gia đình", "Doanh nghiệp", "Cơ quan"};
        JComboBox<String> loaiChuThaiCombo = new JComboBox<>(loaiChuThai);
        loaiChuThaiCombo.setBounds(80, 365, 250, 30);
        registerPanel.add(loaiChuThaiCombo);

        // Username
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setBounds(80, 405, 100, 25);
        registerPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(80, 430, 250, 30);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        registerPanel.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setBounds(80, 470, 100, 25);
        registerPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(80, 495, 250, 30);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        registerPanel.add(passwordField);

        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPasswordLabel.setBounds(80, 535, 150, 25);
        registerPanel.add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(80, 560, 250, 30);
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        registerPanel.add(confirmPasswordField);

        // Buttons
        JButton registerSubmitButton = new JButton("Đăng ký");
        registerSubmitButton.setBounds(80, 610, 120, 35);
        registerSubmitButton.setBackground(new Color(20, 22, 58));
        registerSubmitButton.setForeground(Color.WHITE);
        registerPanel.add(registerSubmitButton);

        JButton registerBackButton = new JButton("Quay lại");
        registerBackButton.setBounds(210, 610, 120, 35);
        registerBackButton.setBackground(new Color(20, 22, 58));
        registerBackButton.setForeground(Color.WHITE);
        registerPanel.add(registerBackButton);

        // Add action listeners
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
                                    Object maTruongNhomObj = rs.getObject("MaTruongNhom");
                                    if (maTruongNhomObj == null) {
                                        // Trưởng nhóm
                                        new TruongNhomUI(maNvtg, tenNvtg).setVisible(true);
                                    } else {
                                        // Nhân viên thường
                                        new NvtgUI(maNvtg, tenNvtg).setVisible(true);
                                    }
                                    break;
                                case "NhanVienDieuPhoi":
                                    String maNvdp = rs.getString("MaNvdp");
                                    String tenNvdp = rs.getString("TenNvdp");
                                    new QuanLyRacThaiUI(maNvdp, tenNvdp).setVisible(true);
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

        continueButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "SELECT * FROM ChuThai WHERE Email = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    currentEmail = email;
                    cardLayout.show(rightPanel, "RESET_PASSWORD");
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Email chưa được đăng ký!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            clearAllFields();
            cardLayout.show(rightPanel, "LOGIN");
        });

        verifyButton.addActionListener(e -> {
            String otp = otpField.getText().trim();
            if (otp.equals("123456")) {
                cardLayout.show(rightPanel, "NEW_PASSWORD");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Mã OTP không đúng!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        backToLoginButton.addActionListener(e -> {
            clearAllFields();
            cardLayout.show(rightPanel, "LOGIN");
        });

        saveButton.addActionListener(e -> {
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng điền đầy đủ thông tin!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, 
                    "Mật khẩu mới không khớp!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "UPDATE ChuThai SET Password = ? WHERE Email = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, newPass);
                pstmt.setString(2, currentEmail);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, 
                    "Đặt lại mật khẩu thành công!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(rightPanel, "LOGIN");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi cập nhật mật khẩu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> {
            clearAllFields();
            cardLayout.show(rightPanel, "LOGIN");
        });

        registerButton.addActionListener(e -> {
            setSize(700, 700);
            cardLayout.show(rightPanel, "REGISTER");
        });

        registerBackButton.addActionListener(e -> {
            setSize(700, 400);
            clearRegisterFields(hoTenField, diaChiField, sdtField, emailRegField, 
                              usernameField, passwordField, confirmPasswordField);
            cardLayout.show(rightPanel, "LOGIN");
        });

        registerSubmitButton.addActionListener(e -> {
            String hoTen = hoTenField.getText().trim();
            String diaChi = diaChiField.getText().trim();
            String sdt = sdtField.getText().trim();
            String email = emailRegField.getText().trim();
            String loaiChuThaiSelected = (String) loaiChuThaiCombo.getSelectedItem();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Validate empty fields
            if (hoTen.isEmpty() || diaChi.isEmpty() || sdt.isEmpty() || 
                email.isEmpty() || username.isEmpty() || password.isEmpty() || 
                confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ thông tin!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate password match
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "Mật khẩu không khớp!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = ConnectionJDBC.getConnection();
                
                // Check for duplicate email
                String checkEmailSql = "SELECT COUNT(*) FROM ChuThai WHERE Email = ?";
                PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql);
                checkEmailStmt.setString(1, email);
                ResultSet emailRs = checkEmailStmt.executeQuery();
                emailRs.next();
                if (emailRs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Email đã được sử dụng!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check for duplicate phone
                String checkPhoneSql = "SELECT COUNT(*) FROM ChuThai WHERE Sdt = ?";
                PreparedStatement checkPhoneStmt = conn.prepareStatement(checkPhoneSql);
                checkPhoneStmt.setString(1, sdt);
                ResultSet phoneRs = checkPhoneStmt.executeQuery();
                phoneRs.next();
                if (phoneRs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Số điện thoại đã được sử dụng!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check for duplicate username
                String checkUsernameSql = "SELECT COUNT(*) FROM ChuThai WHERE Username = ?";
                PreparedStatement checkUsernameStmt = conn.prepareStatement(checkUsernameSql);
                checkUsernameStmt.setString(1, username);
                ResultSet usernameRs = checkUsernameStmt.executeQuery();
                usernameRs.next();
                if (usernameRs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Tên đăng nhập đã tồn tại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get next MaChuThai
                String getNextIdSql = "SELECT MAX(MaChuThai) + 1 FROM ChuThai";
                PreparedStatement getNextIdStmt = conn.prepareStatement(getNextIdSql);
                ResultSet nextIdRs = getNextIdStmt.executeQuery();
                nextIdRs.next();
                int nextId = nextIdRs.getInt(1);

                // Insert new user
                String insertSql = "INSERT INTO ChuThai (MaChuThai, HoTen, DiaChi, Sdt, Email, LoaiChuThai, Username, Password) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, nextId);
                insertStmt.setString(2, hoTen);
                insertStmt.setString(3, diaChi);
                insertStmt.setString(4, sdt);
                insertStmt.setString(5, email);
                insertStmt.setString(6, loaiChuThaiSelected);
                insertStmt.setString(7, username);
                insertStmt.setString(8, password);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this,
                    "Đăng ký thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);

                setSize(700, 400);
                clearRegisterFields(hoTenField, diaChiField, sdtField, emailRegField, 
                                  usernameField, passwordField, confirmPasswordField);
                cardLayout.show(rightPanel, "LOGIN");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi đăng ký: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Add panels to card layout
        rightPanel.add(loginPanel, "LOGIN");
        rightPanel.add(forgotPasswordPanel, "FORGOT_PASSWORD");
        rightPanel.add(resetPasswordPanel, "RESET_PASSWORD");
        rightPanel.add(newPasswordPanel, "NEW_PASSWORD");
        rightPanel.add(registerPanel, "REGISTER");

        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void clearAllFields() {
        emailField.setText("");
        otpField.setText("");
        newPassField.setText("");
        confirmPassField.setText("");
    }

    private void clearRegisterFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
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
