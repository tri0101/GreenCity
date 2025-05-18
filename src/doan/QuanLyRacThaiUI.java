package doan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.util.Date;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.SpinnerDateModel;
import java.util.Calendar;
import java.util.ArrayList;

public class QuanLyRacThaiUI extends JFrame {
    private JPanel sideBar, mainPanel, headerPanel;
    private CardLayout cardLayout;
    private Color primaryColor = new Color(25, 42, 86);    // Xanh đậm
    private Color secondaryColor = new Color(46, 64, 83);  // Xanh nhạt
    private Color accentColor = new Color(46, 204, 113);   // Xanh lá
    private Color textColor = Color.WHITE;
    private Font titleFont = new Font("Arial", Font.BOLD, 24);
    private Font normalFont = new Font("Arial", Font.PLAIN, 14);
    private JTable table; // Add this line

    public QuanLyRacThaiUI() {
        setTitle("Hệ thống Quản lý Thu gom Rác thải");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        createHeaderPanel();
        
        // Sidebar
        createSideBar();
        
        // Main Content
        createMainPanel();

        // Add components to frame
        add(headerPanel, BorderLayout.NORTH);
        add(sideBar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("QUẢN LÝ THU GOM RÁC THẢI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(textColor);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Admin |");
        userLabel.setForeground(textColor);
        JButton logoutBtn = new JButton("Đăng xuất");
        styleButton(logoutBtn);
        logoutBtn.addActionListener(e -> {
            dispose();
            new GiaoDienDangNhap().setVisible(true);
        });
        userPanel.add(userLabel);
        userPanel.add(logoutBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
    }

    private void createSideBar() {
        sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(secondaryColor);
        sideBar.setPreferredSize(new Dimension(250, 0));
        sideBar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Các nút menu
        String[] menuItems = {
            "Tổng quan",
            "Quản lý nhân viên điều phối",
            "Quản lý chủ thải",
            "Quản lý đơn vị thu gom",
            "Quản lý nhân viên thu gom",
            "Quản lý lịch thu gom",
            "Quản lý tuyến thu gom",
            "Quản lý hợp đồng",
            "Quản lý hóa đơn",
            "Quản lý phân công",
            "Quản lý chấm công",
            "Quản lý phản ánh",
            "Quản lý yêu cầu đặt lịch",
            "Thống kê báo cáo"
        };

        for (String item : menuItems) {
            JButton button = createMenuButton(item);
            sideBar.add(button);
            sideBar.add(Box.createVerticalStrut(5));
        }
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        // Thêm các panel chức năng
        mainPanel.add(createTongQuanPanel(), "Tổng quan");
        mainPanel.add(createNvdpPanel(), "Quản lý nhân viên điều phối");
        mainPanel.add(createChuThaiPanel(), "Quản lý chủ thải");
        mainPanel.add(createDvtgPanel(), "Quản lý đơn vị thu gom");
        mainPanel.add(createNvtgPanel(), "Quản lý nhân viên thu gom");
        mainPanel.add(createLichThuGomPanel(), "Quản lý lịch thu gom");
        mainPanel.add(createTuyenThuGomPanel(), "Quản lý tuyến thu gom");
        mainPanel.add(createHopDongPanel(), "Quản lý hợp đồng");
        mainPanel.add(createHoaDonPanel(), "Quản lý hóa đơn");
        mainPanel.add(createPhanCongPanel(), "Quản lý phân công");
        mainPanel.add(createChamCongPanel(), "Quản lý chấm công");
        mainPanel.add(createPhanAnhPanel(), "Quản lý phản ánh");
        mainPanel.add(createYeuCauDatLichPanel(), "Quản lý yêu cầu đặt lịch");
        mainPanel.add(createThongKePanel(), "Thống kê báo cáo");

        // Hiển thị panel Tổng quan mặc định
        cardLayout.show(mainPanel, "Tổng quan");
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(230, 40));
        button.setFont(normalFont);
        button.setForeground(textColor);
        button.setBackground(secondaryColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);
        button.addActionListener(e -> cardLayout.show(mainPanel, text));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(primaryColor);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(secondaryColor);
            }
        });
        return button;
    }

    private void styleButton(JButton button) {
        button.setBackground(accentColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private JPanel createTongQuanPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Tiêu đề
        JLabel titleLabel = new JLabel("Tổng quan hệ thống", JLabel.LEFT);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Panel thống kê
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBackground(Color.WHITE);

        // Thêm các card thống kê
        addStatCard(statsPanel, "Tổng số nhân viên", "0", new Color(52, 152, 219));
        addStatCard(statsPanel, "Số tuyến thu gom", "0", new Color(155, 89, 182));
        addStatCard(statsPanel, "Tổng số khách hàng", "0", new Color(52, 73, 94));
        addStatCard(statsPanel, "Lượng rác thu gom/ngày", "0 kg", new Color(46, 204, 113));
        addStatCard(statsPanel, "Số phương tiện", "0", new Color(231, 76, 60));
        addStatCard(statsPanel, "Phản ánh chưa xử lý", "0", new Color(230, 126, 34));

        panel.add(statsPanel, BorderLayout.CENTER);
        return panel;
    }

    private void addStatCard(JPanel parent, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        parent.add(card);
    }

    private JPanel createNhanVienPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý nhân viên");
        titleLabel.setFont(titleFont);
        
        JButton addButton = new JButton("Thêm nhân viên mới");
        styleButton(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);
        
        // Table
        String[] columns = {"Mã NV", "Họ tên", "Chức vụ", "SĐT", "Email", "Trạng thái"};
        Object[][] data = {};
        
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createTuyenThuGomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý tuyến thu gom");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã tuyến",
            "Mã đơn vị",
            "Tên tuyến",
            "Khu vực"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadTuyenThuGomData(model);
        
        // Thêm action listener cho các nút
        addButton.addActionListener(e -> showAddTuyenThuGomDialog(model));
        deleteButton.addActionListener(e -> showDeleteTuyenThuGomDialog(model));
        editButton.addActionListener(e -> showEditTuyenThuGomDialog(model));
        searchButton.addActionListener(e -> showSearchTuyenThuGomDialog(model));
        refreshButton.addActionListener(e -> loadTuyenThuGomData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void showAddTuyenThuGomDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Thêm tuyến thu gom mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Mã đơn vị:"));
        JTextField maDvField = new JTextField();
        formPanel.add(maDvField);

        formPanel.add(new JLabel("Tên tuyến:"));
        JTextField tenTuyenField = new JTextField();
        formPanel.add(tenTuyenField);

        formPanel.add(new JLabel("Khu vực:"));
        JTextField khuVucField = new JTextField();
        formPanel.add(khuVucField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (maDvField.getText().trim().isEmpty() || 
                    tenTuyenField.getText().trim().isEmpty() ||
                    khuVucField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }

                // Insert into database
            Connection conn = ConnectionJDBC.getConnection();
                String sql = "INSERT INTO TuyenDuongThuGom (MaDv, TenTuyen, KhuVuc) VALUES (?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maDvField.getText().trim()));
                    pstmt.setString(2, tenTuyenField.getText().trim());
                    pstmt.setString(3, khuVucField.getText().trim());
                    
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Thêm tuyến thu gom thành công!");
                    loadTuyenThuGomData(model);
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Mã đơn vị không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm tuyến thu gom: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDeleteTuyenThuGomDialog(DefaultTableModel model) {
        String maTuyen = JOptionPane.showInputDialog(this, "Nhập mã tuyến cần xóa:");
        if (maTuyen != null && !maTuyen.trim().isEmpty()) {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                // Kiểm tra xem tuyến có tồn tại không
                String checkSql = "SELECT COUNT(*) FROM TuyenDuongThuGom WHERE MaTuyen = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, Integer.parseInt(maTuyen.trim()));
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy tuyến thu gom với mã " + maTuyen);
                        return;
                    }
                }

                // Thực hiện xóa
                String deleteSql = "DELETE FROM TuyenDuongThuGom WHERE MaTuyen = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                    pstmt.setInt(1, Integer.parseInt(maTuyen.trim()));
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa tuyến thu gom thành công!");
                    loadTuyenThuGomData(model);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã tuyến không hợp lệ!");
        } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa tuyến thu gom: " + ex.getMessage());
            }
        }
    }

    private void showEditTuyenThuGomDialog(DefaultTableModel model) {
        String maTuyen = JOptionPane.showInputDialog(this, "Nhập mã tuyến cần sửa:");
        if (maTuyen == null || maTuyen.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM TuyenDuongThuGom WHERE MaTuyen = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maTuyen.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy tuyến thu gom với mã " + maTuyen);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa tuyến thu gom", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Mã đơn vị:"));
                JTextField maDvField = new JTextField(String.valueOf(rs.getInt("MaDv")));
                formPanel.add(maDvField);

                formPanel.add(new JLabel("Tên tuyến:"));
                JTextField tenTuyenField = new JTextField(rs.getString("TenTuyen"));
                formPanel.add(tenTuyenField);

                formPanel.add(new JLabel("Khu vực:"));
                JTextField khuVucField = new JTextField(rs.getString("KhuVuc"));
                formPanel.add(khuVucField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                saveButton.addActionListener(e -> {
                    try {
                        // Validate input
                        if (maDvField.getText().trim().isEmpty() || 
                            tenTuyenField.getText().trim().isEmpty() ||
                            khuVucField.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                            return;
                        }

                        // Update database
                        String updateSql = "UPDATE TuyenDuongThuGom SET MaDv = ?, TenTuyen = ?, KhuVuc = ? WHERE MaTuyen = ?";
                        
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, Integer.parseInt(maDvField.getText().trim()));
                            updateStmt.setString(2, tenTuyenField.getText().trim());
                            updateStmt.setString(3, khuVucField.getText().trim());
                            updateStmt.setInt(4, Integer.parseInt(maTuyen.trim()));
                            
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(dialog, "Cập nhật tuyến thu gom thành công!");
                            loadTuyenThuGomData(model);
                            dialog.dispose();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Mã đơn vị không hợp lệ!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật tuyến thu gom: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã tuyến không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm tuyến thu gom: " + ex.getMessage());
        }
    }

    private void loadLichThuGomData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaLich, MaNvdp, NgThu, GioThu, TrangThai FROM LichThuGom ORDER BY NgThu, GioThu";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("MaLich"),
                        rs.getInt("MaNvdp"),
                        rs.getString("NgThu"),
                        rs.getString("GioThu"),
                        rs.getString("TrangThai")
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu lịch thu gom: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createLichThuGomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý lịch thu gom");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã lịch",
            "Mã nhân viên điều phối",
            "Ngày thu",
            "Giờ thu",
            "Trạng thái"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadLichThuGomData(model);
        
        // Thêm action listener cho các nút
        addButton.addActionListener(e -> showAddLichThuGomDialog(model));
        deleteButton.addActionListener(e -> showDeleteLichThuGomDialog(model));
        editButton.addActionListener(e -> showEditLichThuGomDialog(model));
        searchButton.addActionListener(e -> showSearchLichThuGomDialog(model));
        refreshButton.addActionListener(e -> loadLichThuGomData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void showAddLichThuGomDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Thêm lịch thu gom mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Mã nhân viên điều phối:"));
        JTextField maNvdpField = new JTextField();
        formPanel.add(maNvdpField);

        formPanel.add(new JLabel("Ngày thu:"));
        JTextField ngayThuField = new JTextField();
        formPanel.add(ngayThuField);

        formPanel.add(new JLabel("Giờ thu:"));
        JTextField gioThuField = new JTextField();
        formPanel.add(gioThuField);

        formPanel.add(new JLabel("Trạng thái:"));
        JTextField trangThaiField = new JTextField();
        formPanel.add(trangThaiField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (maNvdpField.getText().trim().isEmpty() || 
                    ngayThuField.getText().trim().isEmpty() ||
                    gioThuField.getText().trim().isEmpty() ||
                    trangThaiField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }

                // Insert into database
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "INSERT INTO LichThuGom (MaNvdp, NgThu, GioThu, TrangThai) VALUES (?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maNvdpField.getText().trim()));
                    pstmt.setString(2, ngayThuField.getText().trim());
                    pstmt.setString(3, gioThuField.getText().trim());
                    pstmt.setString(4, trangThaiField.getText().trim());
                    
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Thêm lịch thu gom thành công!");
                    loadLichThuGomData(model);
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Mã nhân viên điều phối không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm lịch thu gom: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDeleteLichThuGomDialog(DefaultTableModel model) {
        String maLich = JOptionPane.showInputDialog(this, "Nhập mã lịch thu gom cần xóa:");
        if (maLich == null || maLich.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            // Kiểm tra xem lịch có tồn tại không
            String checkSql = "SELECT COUNT(*) FROM LichThuGom WHERE MaLich = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, Integer.parseInt(maLich.trim()));
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(this, "Không có mã lịch thu gom " + maLich);
                    return;
                }
            }

            // Thực hiện xóa
            String deleteSql = "DELETE FROM LichThuGom WHERE MaLich = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, Integer.parseInt(maLich.trim()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa lịch thu gom thành công!");
                loadLichThuGomData(model);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã lịch thu gom không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa lịch thu gom: " + ex.getMessage());
        }
    }

    private void showEditLichThuGomDialog(DefaultTableModel model) {
        String maLich = JOptionPane.showInputDialog(this, "Nhập mã lịch thu gom cần sửa:");
        if (maLich == null || maLich.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM LichThuGom WHERE MaLich = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maLich.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không có mã lịch thu gom " + maLich);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa thông tin lịch thu gom", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 400);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Mã nhân viên điều phối:"));
                JTextField maNvdpField = new JTextField(String.valueOf(rs.getInt("MaNvdp")));
                formPanel.add(maNvdpField);

                formPanel.add(new JLabel("Ngày thu:"));
                JTextField ngayThuField = new JTextField(rs.getString("NgThu"));
                formPanel.add(ngayThuField);

                formPanel.add(new JLabel("Giờ thu:"));
                JTextField gioThuField = new JTextField(rs.getString("GioThu"));
                formPanel.add(gioThuField);

                formPanel.add(new JLabel("Trạng thái:"));
                JTextField trangThaiField = new JTextField(rs.getString("TrangThai"));
                formPanel.add(trangThaiField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                saveButton.addActionListener(e -> {
                    try {
                        // Validate input
                        if (maNvdpField.getText().trim().isEmpty() || 
                            ngayThuField.getText().trim().isEmpty() ||
                            gioThuField.getText().trim().isEmpty() ||
                            trangThaiField.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                            return;
                        }

                        // Update database
                        String updateSql = "UPDATE LichThuGom SET MaNvdp = ?, NgThu = ?, GioThu = ?, TrangThai = ? WHERE MaLich = ?";
                        
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, Integer.parseInt(maNvdpField.getText().trim()));
                            updateStmt.setString(2, ngayThuField.getText().trim());
                            updateStmt.setString(3, gioThuField.getText().trim());
                            updateStmt.setString(4, trangThaiField.getText().trim());
                            updateStmt.setInt(5, Integer.parseInt(maLich.trim()));
                            
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(dialog, "Cập nhật lịch thu gom thành công!");
                            loadLichThuGomData(model);
                            dialog.dispose();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Mã nhân viên điều phối không hợp lệ!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật lịch thu gom: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã lịch thu gom không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm lịch thu gom: " + ex.getMessage());
        }
    }

    private void showSearchLichThuGomDialog(DefaultTableModel model) {
        String maLich = JOptionPane.showInputDialog(this, "Nhập mã lịch thu gom cần tìm:");
        if (maLich == null || maLich.trim().isEmpty()) {
            loadLichThuGomData(model);
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM LichThuGom WHERE MaLich = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maLich.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    model.setRowCount(0);
                    model.addRow(new Object[]{
                        rs.getInt("MaLich"),
                        rs.getInt("MaNvdp"),
                        rs.getString("NgThu"),
                        rs.getString("GioThu"),
                        rs.getString("TrangThai")
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Không có mã lịch thu gom " + maLich);
                    loadLichThuGomData(model);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã lịch thu gom không hợp lệ!");
            loadLichThuGomData(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm lịch thu gom: " + ex.getMessage());
            loadLichThuGomData(model);
        }
    }

    private void showAddPhanCongDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Thêm phân công mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Mã nhân viên điều phối:"));
        JTextField maNvdpField = new JTextField();
        formPanel.add(maNvdpField);

        formPanel.add(new JLabel("Mã lịch:"));
        JTextField maLichField = new JTextField();
        formPanel.add(maLichField);

        formPanel.add(new JLabel("Mã nhân viên thu gom:"));
        JTextField maNvtgField = new JTextField();
        formPanel.add(maNvtgField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (maNvdpField.getText().trim().isEmpty() || 
                    maLichField.getText().trim().isEmpty() ||
                    maNvtgField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }

                // Insert into database
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "INSERT INTO PhanCong (MaNvdp, MaLich, MaNvtg) VALUES (?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maNvdpField.getText().trim()));
                    pstmt.setInt(2, Integer.parseInt(maLichField.getText().trim()));
                    pstmt.setInt(3, Integer.parseInt(maNvtgField.getText().trim()));
                    
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Thêm phân công thành công!");
                    loadPhanCongData(model);
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Mã không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm phân công: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDeletePhanCongDialog(DefaultTableModel model) {
        String maPC = JOptionPane.showInputDialog(this, "Nhập mã phân công cần xóa:");
        if (maPC == null || maPC.trim().isEmpty()) {
            return; // Just return if user cancels or input is empty
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            // Kiểm tra xem phân công có tồn tại không
            String checkSql = "SELECT COUNT(*) FROM PhanCong WHERE MaPC = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, Integer.parseInt(maPC.trim()));
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy phân công với mã " + maPC);
                    return;
                }
            }

            // Thực hiện xóa
            String deleteSql = "DELETE FROM PhanCong WHERE MaPC = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, Integer.parseInt(maPC.trim()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa phân công thành công!");
                loadPhanCongData(model);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã phân công không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa phân công: " + ex.getMessage());
        }
    }

    private void showEditPhanCongDialog(DefaultTableModel model) {
        String maPC = JOptionPane.showInputDialog(this, "Nhập mã phân công cần sửa:");
        if (maPC == null || maPC.trim().isEmpty()) {
            return; // Just return if user cancels or input is empty
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM PhanCong WHERE MaPC = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maPC.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy phân công với mã " + maPC);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa thông tin phân công", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Mã nhân viên điều phối:"));
                JTextField maNvdpField = new JTextField(String.valueOf(rs.getInt("MaNvdp")));
                formPanel.add(maNvdpField);

                formPanel.add(new JLabel("Mã lịch:"));
                JTextField maLichField = new JTextField(String.valueOf(rs.getInt("MaLich")));
                formPanel.add(maLichField);

                formPanel.add(new JLabel("Mã nhân viên thu gom:"));
                JTextField maNvtgField = new JTextField(String.valueOf(rs.getInt("MaNvtg")));
                formPanel.add(maNvtgField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                saveButton.addActionListener(e -> {
                    try {
                        // Validate input
                        if (maNvdpField.getText().trim().isEmpty() || 
                            maLichField.getText().trim().isEmpty() ||
                            maNvtgField.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                            return;
                        }

                        // Update database
                        String updateSql = "UPDATE PhanCong SET MaNvdp = ?, MaLich = ?, MaNvtg = ? WHERE MaPC = ?";
                        
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, Integer.parseInt(maNvdpField.getText().trim()));
                            updateStmt.setInt(2, Integer.parseInt(maLichField.getText().trim()));
                            updateStmt.setInt(3, Integer.parseInt(maNvtgField.getText().trim()));
                            updateStmt.setInt(4, Integer.parseInt(maPC.trim()));
                            
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(dialog, "Cập nhật phân công thành công!");
                            loadPhanCongData(model);
                            dialog.dispose();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Mã không hợp lệ!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật phân công: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã phân công không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm phân công: " + ex.getMessage());
        }
    }

    private void showSearchPhanCongDialog(DefaultTableModel model) {
        String maPC = JOptionPane.showInputDialog(this, "Nhập mã phân công cần tìm:");
        if (maPC == null || maPC.trim().isEmpty()) {
            loadPhanCongData(model); // Just reload data if user cancels or input is empty
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM PhanCong WHERE MaPC = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maPC.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    // Clear existing table data
                    model.setRowCount(0);
                    
                    // Add the found record to the table
                    model.addRow(new Object[]{
                        rs.getInt("MaPC"),
                        rs.getInt("MaNvdp"),
                        rs.getInt("MaLich"),
                        rs.getInt("MaNvtg")
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy phân công với mã " + maPC);
                    loadPhanCongData(model);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã phân công không hợp lệ!");
            loadPhanCongData(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm phân công: " + ex.getMessage());
            loadPhanCongData(model);
        }
    }

    private JPanel createPhanCongPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý phân công");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã phân công",
            "Mã NVĐP",
            "Mã lịch",
            "Mã NVTG"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadPhanCongData(model);
        
        // Thêm action listener cho các nút
        addButton.addActionListener(e -> showAddPhanCongDialog(model));
        deleteButton.addActionListener(e -> showDeletePhanCongDialog(model));
        editButton.addActionListener(e -> showEditPhanCongDialog(model));
        searchButton.addActionListener(e -> showSearchPhanCongDialog(model));
        refreshButton.addActionListener(e -> loadPhanCongData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadPhanCongData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaPC, MaNvdp, MaLich, MaNvtg FROM PhanCong ORDER BY MaPC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("MaPC"),
                        rs.getInt("MaNvdp"),
                        rs.getInt("MaLich"),
                        rs.getInt("MaNvtg")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu phân công: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createKhachHangPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Quản lý khách hàng");
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Customer table
        String[] columns = {"Mã KH", "Họ tên", "Địa chỉ", "SĐT", "Loại KH", "Trạng thái"};
        Object[][] data = {};
        
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createPhuongTienPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Quản lý phương tiện");
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Vehicle table
        String[] columns = {"Mã PT", "Loại xe", "Biển số", "Tải trọng", "Năm sản xuất", "Trạng thái"};
        Object[][] data = {};
        
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createBaoCaoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Báo cáo thống kê");
        titleLabel.setFont(titleFont);
        
        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] months = {"Tháng 1/2024", "Tháng 2/2024", "Tháng 3/2024"};
        JComboBox<String> monthPicker = new JComboBox<>(months);
        JButton exportButton = new JButton("Xuất báo cáo");
        styleButton(exportButton);
        controlPanel.add(new JLabel("Chọn tháng: "));
        controlPanel.add(monthPicker);
        controlPanel.add(exportButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(controlPanel, BorderLayout.EAST);
        
        // Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentPanel.setBackground(Color.WHITE);

        // Thêm các panel thống kê
        // Panel 1: Thống kê khối lượng rác
        JPanel weightPanel = new JPanel(new BorderLayout(10, 10));
        weightPanel.setBackground(new Color(240, 240, 240));
        weightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel weightTitle = new JLabel("Thống kê khối lượng rác");
        weightTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel weightContent = new JLabel("<html>Tổng khối lượng: 0 kg<br>Rác sinh hoạt: 0 kg<br>Rác tái chế: 0 kg<br>Rác nguy hại: 0 kg</html>");
        weightPanel.add(weightTitle, BorderLayout.NORTH);
        weightPanel.add(weightContent, BorderLayout.CENTER);
        
        // Panel 2: Thống kê doanh thu
        JPanel revenuePanel = new JPanel(new BorderLayout(10, 10));
        revenuePanel.setBackground(new Color(240, 240, 240));
        revenuePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel revenueTitle = new JLabel("Thống kê doanh thu");
        revenueTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel revenueContent = new JLabel("<html>Tổng doanh thu: 0đ<br>Đã thu: 0đ<br>Chưa thu: 0đ</html>");
        revenuePanel.add(revenueTitle, BorderLayout.NORTH);
        revenuePanel.add(revenueContent, BorderLayout.CENTER);
        
        // Panel 3: Thống kê nhân viên
        JPanel staffPanel = new JPanel(new BorderLayout(10, 10));
        staffPanel.setBackground(new Color(240, 240, 240));
        staffPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel staffTitle = new JLabel("Thống kê nhân viên");
        staffTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel staffContent = new JLabel("<html>Tổng số nhân viên: 0<br>Điều phối: 0<br>Thu gom: 0<br>Tài xế: 0</html>");
        staffPanel.add(staffTitle, BorderLayout.NORTH);
        staffPanel.add(staffContent, BorderLayout.CENTER);
        
        // Panel 4: Đánh giá dịch vụ
        JPanel ratingPanel = new JPanel(new BorderLayout(10, 10));
        ratingPanel.setBackground(new Color(240, 240, 240));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel ratingTitle = new JLabel("Đánh giá dịch vụ");
        ratingTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel ratingContent = new JLabel("<html>Điểm đánh giá TB: 0/5<br>Số lượt đánh giá: 0<br>Phản hồi tích cực: 0%</html>");
        ratingPanel.add(ratingTitle, BorderLayout.NORTH);
        ratingPanel.add(ratingContent, BorderLayout.CENTER);

        contentPanel.add(weightPanel);
        contentPanel.add(revenuePanel);
        contentPanel.add(staffPanel);
        contentPanel.add(ratingPanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createPhanAnhPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý phản ánh");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã phản ánh",
            "Mã chủ thải",
            "Nội dung",
            "Thời gian gửi",
            "Trạng thái"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadPhanAnhData(model);
        
        // Thêm action listener cho các nút
        deleteButton.addActionListener(e -> showDeletePhanAnhDialog(model));
        editButton.addActionListener(e -> showEditPhanAnhDialog(model));
        searchButton.addActionListener(e -> showSearchPhanAnhDialog(model));
        refreshButton.addActionListener(e -> loadPhanAnhData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadPhanAnhData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaPA, MaChuThai, NoiDung, ThoiGianGui, TrangThai FROM PhanAnh ORDER BY ThoiGianGui DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("MaPA"),
                        rs.getInt("MaChuThai"),
                        rs.getString("NoiDung"),
                        dateFormat.format(rs.getTimestamp("ThoiGianGui")),
                        rs.getString("TrangThai")
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu phản ánh: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createYeuCauDatLichPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý yêu cầu đặt lịch");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã yêu cầu",
            "Mã chủ thải",
            "Mã lịch thu gom",
            "Ngày thu gom",
            
            "Loại rác",
            "Trạng thái"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadYeuCauDatLichData(model);
        
        // Thêm action listener cho các nút
        deleteButton.addActionListener(e -> showDeleteYeuCauDatLichDialog(model));
        editButton.addActionListener(e -> showEditYeuCauDatLichDialog(model));
        searchButton.addActionListener(e -> showSearchYeuCauDatLichDialog(model));
        refreshButton.addActionListener(e -> loadYeuCauDatLichData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadYeuCauDatLichData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaYc, MaChuThai, MaLich, ThoiGianYc, GhiChu, TrangThai FROM YeuCauDatLich ORDER BY ThoiGianYc DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("MaYc"),
                        rs.getInt("MaChuThai"),
                        rs.getInt("MaLich"),
                        dateFormat.format(rs.getDate("ThoiGianYc")),
                        rs.getString("GhiChu"),
                        rs.getString("TrangThai")
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu yêu cầu đặt lịch: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createNvdpPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý nhân viên điều phối");
        titleLabel.setFont(titleFont);
        
        JButton addButton = new JButton("Thêm nhân viên điều phối");
        styleButton(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        // Table
        String[] columns = {"Mã NVĐP", "Họ tên", "Ngày sinh", "SĐT", "Email", "Địa chỉ", "Trạng thái"};
        Object[][] data = {};
        
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createChuThaiPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý chủ thải");
        titleLabel.setFont(titleFont);
        
        JButton addButton = new JButton("Thêm chủ thải");
        styleButton(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        // Table
        String[] columns = {"Mã chủ thải", "Tên chủ thải", "Loại chủ thải", "SĐT", "Email", "Địa chỉ", "Trạng thái"};
        Object[][] data = {};
        
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void showAddDvtgDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Thêm đơn vị thu gom mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Tên đơn vị:"));
        JTextField tenDvField = new JTextField();
        formPanel.add(tenDvField);

        formPanel.add(new JLabel("Khu vực phụ trách:"));
        JTextField khuVucField = new JTextField();
        formPanel.add(khuVucField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (tenDvField.getText().trim().isEmpty() || 
                    khuVucField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }

                // Insert into database
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "INSERT INTO DonViThuGom (TenDv, KhuVucPhuTrach) VALUES (?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, tenDvField.getText().trim());
                    pstmt.setString(2, khuVucField.getText().trim());
                    
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Thêm đơn vị thu gom thành công!");
                    loadDvtgData(model);
                    dialog.dispose();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm đơn vị thu gom: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDeleteDvtgDialog(DefaultTableModel model) {
        String maDv = JOptionPane.showInputDialog(this, "Nhập mã đơn vị thu gom cần xóa:");
        if (maDv == null || maDv.trim().isEmpty()) {
            return; // Just return if user cancels or input is empty
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            // Kiểm tra xem đơn vị có tồn tại không
            String checkSql = "SELECT COUNT(*) FROM DonViThuGom WHERE MaDv = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, Integer.parseInt(maDv.trim()));
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy đơn vị thu gom với mã " + maDv);
                    return;
                }
            }

            // Thực hiện xóa
            String deleteSql = "DELETE FROM DonViThuGom WHERE MaDv = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, Integer.parseInt(maDv.trim()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa đơn vị thu gom thành công!");
                loadDvtgData(model);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã đơn vị thu gom không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa đơn vị thu gom: " + ex.getMessage());
        }
    }

    private void showEditDvtgDialog(DefaultTableModel model) {
        String maDv = JOptionPane.showInputDialog(this, "Nhập mã đơn vị thu gom cần sửa:");
        if (maDv == null || maDv.trim().isEmpty()) {
            return; // Just return if user cancels or input is empty
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM DonViThuGom WHERE MaDv = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maDv.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy đơn vị thu gom với mã " + maDv);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa thông tin đơn vị thu gom", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Tên đơn vị:"));
                JTextField tenDvField = new JTextField(rs.getString("TenDv"));
                formPanel.add(tenDvField);

                formPanel.add(new JLabel("Khu vực phụ trách:"));
                JTextField khuVucField = new JTextField(rs.getString("KhuVucPhuTrach"));
                formPanel.add(khuVucField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                saveButton.addActionListener(e -> {
                    try {
                        // Validate input
                        if (tenDvField.getText().trim().isEmpty() || 
                            khuVucField.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                            return;
                        }

                        // Update database
                        String updateSql = "UPDATE DonViThuGom SET TenDv = ?, KhuVucPhuTrach = ? WHERE MaDv = ?";
                        
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, tenDvField.getText().trim());
                            updateStmt.setString(2, khuVucField.getText().trim());
                            updateStmt.setInt(3, Integer.parseInt(maDv.trim()));
                            
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(dialog, "Cập nhật đơn vị thu gom thành công!");
                            loadDvtgData(model);
                            dialog.dispose();
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật đơn vị thu gom: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã đơn vị thu gom không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm đơn vị thu gom: " + ex.getMessage());
        }
    }

    private void showSearchDvtgDialog(DefaultTableModel model) {
        String maDv = JOptionPane.showInputDialog(this, "Nhập mã đơn vị thu gom cần tìm:");
        if (maDv == null || maDv.trim().isEmpty()) {
            loadDvtgData(model); // Just reload data if user cancels or input is empty
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM DonViThuGom WHERE MaDv = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maDv.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    // Clear existing table data
                    model.setRowCount(0);
                    
                    // Add the found record to the table
                    model.addRow(new Object[]{
                        rs.getInt("MaDv"),
                        rs.getString("TenDv"),
                        rs.getString("KhuVucPhuTrach")
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy đơn vị thu gom với mã " + maDv);
                    loadDvtgData(model);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã đơn vị thu gom không hợp lệ!");
            loadDvtgData(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm đơn vị thu gom: " + ex.getMessage());
            loadDvtgData(model);
        }
    }

    private JPanel createDvtgPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý đơn vị thu gom");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã đơn vị",
            "Tên đơn vị", 
            "Khu vực phụ trách"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadDvtgData(model);
        
        // Thêm action listener cho các nút
        addButton.addActionListener(e -> showAddDvtgDialog(model));
        deleteButton.addActionListener(e -> showDeleteDvtgDialog(model));
        editButton.addActionListener(e -> showEditDvtgDialog(model));
        searchButton.addActionListener(e -> showSearchDvtgDialog(model));
        refreshButton.addActionListener(e -> loadDvtgData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadDvtgData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaDv, TenDv, KhuVucPhuTrach FROM DonViThuGom ORDER BY MaDv";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("MaDv"),
                        rs.getString("TenDv"),
                        rs.getString("KhuVucPhuTrach")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu đơn vị thu gom: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createNvtgPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý nhân viên thu gom");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã NVTG",
            "Mã đơn vị",
            "Tên NVTG",
            "Giới tính",
            "Số điện thoại",
            "Mã trưởng nhóm",
            "Tên trưởng nhóm"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadNvtgData(model);
        
        // Thêm action listener cho các nút
        addButton.addActionListener(e -> showAddNvtgDialog(model));
        deleteButton.addActionListener(e -> showDeleteNvtgDialog(model));
        editButton.addActionListener(e -> showEditNvtgDialog(model));
        searchButton.addActionListener(e -> showSearchNvtgDialog(model));
        refreshButton.addActionListener(e -> loadNvtgData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void showDeleteNvtgDialog(DefaultTableModel model) {
        String maNvtg = JOptionPane.showInputDialog(this, "Nhập mã nhân viên thu gom cần xóa:");
        if (maNvtg == null || maNvtg.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            // Kiểm tra xem nhân viên có tồn tại không
            String checkSql = "SELECT COUNT(*) FROM NhanVienThuGom WHERE MaNvtg = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, Integer.parseInt(maNvtg.trim()));
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(this, "Không có mã nhân viên thu gom " + maNvtg);
                    return;
                }
            }

            // Thực hiện xóa
            String deleteSql = "DELETE FROM NhanVienThuGom WHERE MaNvtg = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, Integer.parseInt(maNvtg.trim()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thu gom thành công!");
                loadNvtgData(model);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên thu gom không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa nhân viên thu gom: " + ex.getMessage());
        }
    }

    private void showEditNvtgDialog(DefaultTableModel model) {
        String maNvtg = JOptionPane.showInputDialog(this, "Nhập mã nhân viên thu gom cần sửa:");
        if (maNvtg == null || maNvtg.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM NhanVienThuGom WHERE MaNvtg = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maNvtg.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không có mã nhân viên thu gom " + maNvtg);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa thông tin nhân viên thu gom", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 450);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Mã đơn vị:"));
                JTextField maDvField = new JTextField(String.valueOf(rs.getInt("MaDv")));
                formPanel.add(maDvField);

                formPanel.add(new JLabel("Tên NVTG:"));
                JTextField tenNvtgField = new JTextField(rs.getString("TenNvtg"));
                formPanel.add(tenNvtgField);

                formPanel.add(new JLabel("Giới tính:"));
                JTextField gioiTinhField = new JTextField(rs.getString("GioiTinh"));
                formPanel.add(gioiTinhField);

                formPanel.add(new JLabel("Số điện thoại:"));
                JTextField sdtField = new JTextField(rs.getString("Sdt"));
                formPanel.add(sdtField);

                formPanel.add(new JLabel("Mã trưởng nhóm:"));
                Integer maTruongNhom = rs.getObject("MaTruongNhom", Integer.class);
                JTextField maTruongNhomField = new JTextField(maTruongNhom != null ? maTruongNhom.toString() : "");
                formPanel.add(maTruongNhomField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                saveButton.addActionListener(e -> {
                    try {
                        // Validate input
                        if (maDvField.getText().trim().isEmpty() || 
                            tenNvtgField.getText().trim().isEmpty() ||
                            gioiTinhField.getText().trim().isEmpty() ||
                            sdtField.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                            return;
                        }

                        // Update database
                        String updateSql = "UPDATE NhanVienThuGom SET MaDv = ?, TenNvtg = ?, GioiTinh = ?, Sdt = ?, MaTruongNhom = ? WHERE MaNvtg = ?";
                        
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, Integer.parseInt(maDvField.getText().trim()));
                            updateStmt.setString(2, tenNvtgField.getText().trim());
                            updateStmt.setString(3, gioiTinhField.getText().trim());
                            updateStmt.setString(4, sdtField.getText().trim());
                            
                            // Handle MaTruongNhom (can be null)
                            String maTruongNhomText = maTruongNhomField.getText().trim();
                            if (maTruongNhomText.isEmpty()) {
                                updateStmt.setNull(5, java.sql.Types.INTEGER);
                            } else {
                                updateStmt.setInt(5, Integer.parseInt(maTruongNhomText));
                            }
                            
                            updateStmt.setInt(6, Integer.parseInt(maNvtg.trim()));
                            
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(dialog, "Cập nhật nhân viên thu gom thành công!");
                            loadNvtgData(model);
                            dialog.dispose();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Giá trị số không hợp lệ!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật nhân viên thu gom: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên thu gom không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm nhân viên thu gom: " + ex.getMessage());
        }
    }

    private void showSearchNvtgDialog(DefaultTableModel model) {
        String maNvtg = JOptionPane.showInputDialog(this, "Nhập mã nhân viên thu gom cần tìm:");
        if (maNvtg == null || maNvtg.trim().isEmpty()) {
            loadNvtgData(model);
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = """
                SELECT nv.MaNvtg, nv.MaDv, nv.TenNvtg, nv.GioiTinh, nv.Sdt, 
                       nv.MaTruongNhom, tn.TenNvtg as TenTruongNhom
                FROM NhanVienThuGom nv
                LEFT JOIN NhanVienThuGom tn ON nv.MaTruongNhom = tn.MaNvtg
                WHERE nv.MaNvtg = ?
                """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maNvtg.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    model.setRowCount(0);
                    
                    Integer maTruongNhom = rs.getObject("MaTruongNhom", Integer.class);
                    String displayMaTruongNhom = maTruongNhom != null ? maTruongNhom.toString() : "Không có";
                    String tenTruongNhom = rs.getString("TenTruongNhom");
                    String displayTenTruongNhom = tenTruongNhom != null ? tenTruongNhom : "Không có";
                    
                    model.addRow(new Object[]{
                        rs.getInt("MaNvtg"),
                        rs.getInt("MaDv"),
                        rs.getString("TenNvtg"),
                        rs.getString("GioiTinh"),
                        rs.getString("Sdt"),
                        displayMaTruongNhom,
                        displayTenTruongNhom
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Không có mã nhân viên thu gom " + maNvtg);
                    loadNvtgData(model);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên thu gom không hợp lệ!");
            loadNvtgData(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm nhân viên thu gom: " + ex.getMessage());
            loadNvtgData(model);
        }
    }

    private void showAddNvtgDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Thêm nhân viên thu gom mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Mã đơn vị:"));
        JTextField maDvField = new JTextField();
        formPanel.add(maDvField);

        formPanel.add(new JLabel("Tên NVTG:"));
        JTextField tenNvtgField = new JTextField();
        formPanel.add(tenNvtgField);

        formPanel.add(new JLabel("Giới tính:"));
        JTextField gioiTinhField = new JTextField();
        formPanel.add(gioiTinhField);

        formPanel.add(new JLabel("Số điện thoại:"));
        JTextField sdtField = new JTextField();
        formPanel.add(sdtField);

        formPanel.add(new JLabel("Mã trưởng nhóm:"));
        JTextField maTruongNhomField = new JTextField();
        formPanel.add(maTruongNhomField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (maDvField.getText().trim().isEmpty() || 
                    tenNvtgField.getText().trim().isEmpty() ||
                    gioiTinhField.getText().trim().isEmpty() ||
                    sdtField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }

                // Insert into database
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "INSERT INTO NhanVienThuGom (MaDv, TenNvtg, GioiTinh, Sdt, MaTruongNhom) VALUES (?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maDvField.getText().trim()));
                    pstmt.setString(2, tenNvtgField.getText().trim());
                    pstmt.setString(3, gioiTinhField.getText().trim());
                    pstmt.setString(4, sdtField.getText().trim());
                    
                    // Handle MaTruongNhom (can be null)
                    String maTruongNhomText = maTruongNhomField.getText().trim();
                    if (maTruongNhomText.isEmpty()) {
                        pstmt.setNull(5, java.sql.Types.INTEGER);
                    } else {
                        pstmt.setInt(5, Integer.parseInt(maTruongNhomText));
                    }
                    
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thu gom thành công!");
                    loadNvtgData(model);
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá trị số không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm nhân viên thu gom: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void loadNvtgData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            // Sử dụng LEFT JOIN để lấy thông tin trưởng nhóm (nếu có)
            String sql = """
                SELECT nv.MaNvtg, nv.MaDv, nv.TenNvtg, nv.GioiTinh, nv.Sdt, 
                       nv.MaTruongNhom, tn.TenNvtg as TenTruongNhom
                FROM NhanVienThuGom nv
                LEFT JOIN NhanVienThuGom tn ON nv.MaTruongNhom = tn.MaNvtg
                ORDER BY nv.MaNvtg
                """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    // Xử lý mã trưởng nhóm (hiển thị "Không có" nếu null)
                    Integer maTruongNhom = rs.getObject("MaTruongNhom", Integer.class);
                    String displayMaTruongNhom = maTruongNhom != null ? maTruongNhom.toString() : "Không có";
                    
                    // Xử lý tên trưởng nhóm
                    String tenTruongNhom = rs.getString("TenTruongNhom");
                    String displayTenTruongNhom = tenTruongNhom != null ? tenTruongNhom : "Không có";
                    
                    // Thêm dòng mới vào bảng
                    model.addRow(new Object[]{
                        rs.getInt("MaNvtg"),
                        rs.getInt("MaDv"),
                        rs.getString("TenNvtg"),
                        rs.getString("GioiTinh"),
                        rs.getString("Sdt"),
                        displayMaTruongNhom,
                        displayTenTruongNhom
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu nhân viên thu gom: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createHopDongPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý hợp đồng");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã hợp đồng", 
            "Mã chủ thải",
            "Loại hợp đồng",
            "Ngày bắt đầu",
            "Ngày kết thúc",
            "Giá trị",
            "Mô tả",
            "Trạng thái"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadHopDongData(model);
        
        // Thêm action listener cho các nút
        addButton.addActionListener(e -> showAddHopDongDialog(model));
        deleteButton.addActionListener(e -> showDeleteHopDongDialog(model));
        editButton.addActionListener(e -> showEditHopDongDialog(model));
        searchButton.addActionListener(e -> showSearchHopDongDialog(model));
        refreshButton.addActionListener(e -> loadHopDongData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void showAddHopDongDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Thêm hợp đồng mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Mã chủ thải:"));
        JTextField maChuThaiField = new JTextField();
        formPanel.add(maChuThaiField);

        formPanel.add(new JLabel("Loại hợp đồng:"));
        JTextField loaiHopDongField = new JTextField();
        formPanel.add(loaiHopDongField);

        // Tạo spinner cho ngày bắt đầu
        formPanel.add(new JLabel("Ngày bắt đầu:"));
        SpinnerDateModel startDateModel = new SpinnerDateModel();
        JSpinner ngayBatDauSpinner = new JSpinner(startDateModel);
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(ngayBatDauSpinner, "dd/MM/yyyy");
        ngayBatDauSpinner.setEditor(startDateEditor);
        formPanel.add(ngayBatDauSpinner);

        // Tạo spinner cho ngày kết thúc
        formPanel.add(new JLabel("Ngày kết thúc:"));
        SpinnerDateModel endDateModel = new SpinnerDateModel();
        JSpinner ngayKetThucSpinner = new JSpinner(endDateModel);
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(ngayKetThucSpinner, "dd/MM/yyyy");
        ngayKetThucSpinner.setEditor(endDateEditor);
        formPanel.add(ngayKetThucSpinner);

        formPanel.add(new JLabel("Giá trị:"));
        JTextField giaTriField = new JTextField();
        formPanel.add(giaTriField);

        formPanel.add(new JLabel("Mô tả:"));
        JTextField moTaField = new JTextField();
        formPanel.add(moTaField);

        formPanel.add(new JLabel("Trạng thái:"));
        JTextField trangThaiField = new JTextField();
        formPanel.add(trangThaiField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (maChuThaiField.getText().trim().isEmpty() || 
                    loaiHopDongField.getText().trim().isEmpty() ||
                    giaTriField.getText().trim().isEmpty() ||
                    trangThaiField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }

                // Get dates from spinners
                Date ngayBatDau = (Date) ngayBatDauSpinner.getValue();
                Date ngayKetThuc = (Date) ngayKetThucSpinner.getValue();

                // Parse giá trị
                double giaTri = Double.parseDouble(giaTriField.getText().trim());

                // Insert into database
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "INSERT INTO HopDong (MaChuThai, LoaiHopDong, NgBatDau, NgKetThuc, GiaTri, MoTa, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maChuThaiField.getText().trim()));
                    pstmt.setString(2, loaiHopDongField.getText().trim());
                    pstmt.setDate(3, new java.sql.Date(ngayBatDau.getTime()));
                    pstmt.setDate(4, new java.sql.Date(ngayKetThuc.getTime()));
                    pstmt.setDouble(5, giaTri);
                    pstmt.setString(6, moTaField.getText().trim());
                    pstmt.setString(7, trangThaiField.getText().trim());
                    
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Thêm hợp đồng thành công!");
                    loadHopDongData(model);
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá trị không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm hợp đồng: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDeleteHopDongDialog(DefaultTableModel model) {
        String maHD = JOptionPane.showInputDialog(this, "Nhập mã hợp đồng cần xóa:");
        if (maHD != null && !maHD.trim().isEmpty()) {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                // Kiểm tra xem hợp đồng có tồn tại không
                String checkSql = "SELECT COUNT(*) FROM HopDong WHERE MaHopDong = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, Integer.parseInt(maHD.trim()));
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy hợp đồng với mã " + maHD);
                        return;
                    }
                }

                // Thực hiện xóa
                String deleteSql = "DELETE FROM HopDong WHERE MaHopDong = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                    pstmt.setInt(1, Integer.parseInt(maHD.trim()));
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa hợp đồng thành công!");
                    loadHopDongData(model);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã hợp đồng không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa hợp đồng: " + ex.getMessage());
            }
        }
    }

    private void showEditHopDongDialog(DefaultTableModel model) {
        String maHD = JOptionPane.showInputDialog(this, "Nhập mã hợp đồng cần sửa:");
        if (maHD == null || maHD.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM HopDong WHERE MaHopDong = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maHD.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hợp đồng với mã " + maHD);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa hợp đồng", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 500);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Mã chủ thải:"));
                JTextField maChuThaiField = new JTextField(String.valueOf(rs.getInt("MaChuThai")));
                formPanel.add(maChuThaiField);

                formPanel.add(new JLabel("Loại hợp đồng:"));
                JTextField loaiHopDongField = new JTextField(rs.getString("LoaiHopDong"));
                formPanel.add(loaiHopDongField);

                // Spinner cho ngày bắt đầu
                formPanel.add(new JLabel("Ngày bắt đầu:"));
                SpinnerDateModel startDateModel = new SpinnerDateModel();
                JSpinner ngayBatDauSpinner = new JSpinner(startDateModel);
                JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(ngayBatDauSpinner, "dd/MM/yyyy");
                ngayBatDauSpinner.setEditor(startDateEditor);
                ngayBatDauSpinner.setValue(rs.getDate("NgBatDau"));
                formPanel.add(ngayBatDauSpinner);

                // Spinner cho ngày kết thúc
                formPanel.add(new JLabel("Ngày kết thúc:"));
                SpinnerDateModel endDateModel = new SpinnerDateModel();
                JSpinner ngayKetThucSpinner = new JSpinner(endDateModel);
                JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(ngayKetThucSpinner, "dd/MM/yyyy");
                ngayKetThucSpinner.setEditor(endDateEditor);
                ngayKetThucSpinner.setValue(rs.getDate("NgKetThuc"));
                formPanel.add(ngayKetThucSpinner);

                formPanel.add(new JLabel("Giá trị:"));
                JTextField giaTriField = new JTextField(String.valueOf(rs.getDouble("GiaTri")));
                formPanel.add(giaTriField);

                formPanel.add(new JLabel("Mô tả:"));
                JTextField moTaField = new JTextField(rs.getString("MoTa"));
                formPanel.add(moTaField);

                formPanel.add(new JLabel("Trạng thái:"));
                JTextField trangThaiField = new JTextField(rs.getString("TrangThai"));
                formPanel.add(trangThaiField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                saveButton.addActionListener(e -> {
                    try {
                        // Validate input
                        if (maChuThaiField.getText().trim().isEmpty() || 
                            loaiHopDongField.getText().trim().isEmpty() ||
                            giaTriField.getText().trim().isEmpty() ||
                            trangThaiField.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                            return;
                        }

                        // Get dates from spinners
                        Date ngayBatDau = (Date) ngayBatDauSpinner.getValue();
                        Date ngayKetThuc = (Date) ngayKetThucSpinner.getValue();

                        // Parse giá trị
                        double giaTri = Double.parseDouble(giaTriField.getText().trim());

                        // Update database
                        String updateSql = "UPDATE HopDong SET MaChuThai = ?, LoaiHopDong = ?, NgBatDau = ?, NgKetThuc = ?, GiaTri = ?, MoTa = ?, TrangThai = ? WHERE MaHopDong = ?";
                        
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, Integer.parseInt(maChuThaiField.getText().trim()));
                            updateStmt.setString(2, loaiHopDongField.getText().trim());
                            updateStmt.setDate(3, new java.sql.Date(ngayBatDau.getTime()));
                            updateStmt.setDate(4, new java.sql.Date(ngayKetThuc.getTime()));
                            updateStmt.setDouble(5, giaTri);
                            updateStmt.setString(6, moTaField.getText().trim());
                            updateStmt.setString(7, trangThaiField.getText().trim());
                            updateStmt.setInt(8, Integer.parseInt(maHD.trim()));
                            
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(dialog, "Cập nhật hợp đồng thành công!");
                            loadHopDongData(model);
                            dialog.dispose();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Giá trị không hợp lệ!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật hợp đồng: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã hợp đồng không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm hợp đồng: " + ex.getMessage());
        }
    }

    private void showSearchHopDongDialog(DefaultTableModel model) {
        String maHD = JOptionPane.showInputDialog(this, "Nhập mã hợp đồng cần tìm:");
        if (maHD != null && !maHD.trim().isEmpty()) {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "SELECT * FROM HopDong WHERE MaHopDong = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maHD.trim()));
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next()) {
                        // Clear existing table data
                        model.setRowCount(0);
                        
                        // Format dates
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String ngayBatDau = rs.getDate("NgBatDau") != null ? 
                            dateFormat.format(rs.getDate("NgBatDau")) : "";
                        String ngayKetThuc = rs.getDate("NgKetThuc") != null ? 
                            dateFormat.format(rs.getDate("NgKetThuc")) : "";
                        
                        // Format currency
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        String formattedGiaTri = currencyFormat.format(rs.getDouble("GiaTri"));
                        
                        // Add the found contract to the table
                        model.addRow(new Object[]{
                            rs.getInt("MaHopDong"),
                            rs.getInt("MaChuThai"),
                            rs.getString("LoaiHopDong"),
                            ngayBatDau,
                            ngayKetThuc,
                            formattedGiaTri,
                            rs.getString("MoTa"),
                            rs.getString("TrangThai")
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy hợp đồng với mã " + maHD);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã hợp đồng không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm hợp đồng: " + ex.getMessage());
            }
        }
    }

    private void loadHopDongData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaHopDong, MaChuThai, LoaiHopDong, NgBatDau, NgKetThuc, GiaTri, MoTa, TrangThai FROM HopDong ORDER BY MaHopDong";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    // Format giá trị tiền tệ
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    String formattedGiaTri = currencyFormat.format(rs.getDouble("GiaTri"));
                    
                    // Format ngày tháng
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String ngBatDau = rs.getDate("NgBatDau") != null ? dateFormat.format(rs.getDate("NgBatDau")) : "";
                    String ngKetThuc = rs.getDate("NgKetThuc") != null ? dateFormat.format(rs.getDate("NgKetThuc")) : "";
                    
                    // Thêm dòng mới vào bảng
                    model.addRow(new Object[]{
                        rs.getInt("MaHopDong"),
                        rs.getInt("MaChuThai"),
                        rs.getString("LoaiHopDong"),
                        ngBatDau,
                        ngKetThuc,
                        formattedGiaTri,
                        rs.getString("MoTa"),
                        rs.getString("TrangThai")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu hợp đồng: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createHoaDonPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý hóa đơn");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã hóa đơn",
            "Mã hợp đồng",
            "Mã NVĐP",
            "Ngày lập",
            "Số tiền",
            "Tình trạng"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadHoaDonData(model);
        
        // Thêm action listener cho các nút
        addButton.addActionListener(e -> showAddHoaDonDialog(model));
        deleteButton.addActionListener(e -> showDeleteHoaDonDialog(model));
        editButton.addActionListener(e -> showEditHoaDonDialog(model));
        searchButton.addActionListener(e -> showSearchHoaDonDialog(model));
        refreshButton.addActionListener(e -> loadHoaDonData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void showAddHoaDonDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Tạo hóa đơn mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Mã hợp đồng:"));
        JTextField maHopDongField = new JTextField();
        formPanel.add(maHopDongField);

        formPanel.add(new JLabel("Mã NVĐP:"));
        JTextField maNvdpField = new JTextField();
        formPanel.add(maNvdpField);

        // Tạo spinner cho ngày lập
        formPanel.add(new JLabel("Ngày lập:"));
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner ngayLapSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ngayLapSpinner, "dd/MM/yyyy");
        ngayLapSpinner.setEditor(dateEditor);
        formPanel.add(ngayLapSpinner);

        formPanel.add(new JLabel("Số tiền:"));
        JTextField soTienField = new JTextField();
        formPanel.add(soTienField);

        formPanel.add(new JLabel("Tình trạng:"));
        JTextField tinhTrangField = new JTextField();
        formPanel.add(tinhTrangField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (maHopDongField.getText().trim().isEmpty() || 
                    maNvdpField.getText().trim().isEmpty() ||
                    soTienField.getText().trim().isEmpty() ||
                    tinhTrangField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }

                // Get date from spinner
                Date ngayLap = (Date) ngayLapSpinner.getValue();

                // Parse số tiền
                double soTien = Double.parseDouble(soTienField.getText().trim());

                // Insert into database
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "INSERT INTO HoaDon (MaHopDong, MaNvdp, NgLap, SoTien, TinhTrang) VALUES (?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maHopDongField.getText().trim()));
                    pstmt.setInt(2, Integer.parseInt(maNvdpField.getText().trim()));
                    pstmt.setDate(3, new java.sql.Date(ngayLap.getTime()));
                    pstmt.setDouble(4, soTien);
                    pstmt.setString(5, tinhTrangField.getText().trim());
                    
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Thêm hóa đơn thành công!");
                    loadHoaDonData(model);
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá trị không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm hóa đơn: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDeleteHoaDonDialog(DefaultTableModel model) {
        String maHD = JOptionPane.showInputDialog(this, "Nhập mã hóa đơn cần xóa:");
        if (maHD != null && !maHD.trim().isEmpty()) {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                // Kiểm tra xem hóa đơn có tồn tại không
                String checkSql = "SELECT COUNT(*) FROM HoaDon WHERE MaHoaDon = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, Integer.parseInt(maHD.trim()));
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn với mã " + maHD);
                        return;
                    }
                }

                // Thực hiện xóa
                String deleteSql = "DELETE FROM HoaDon WHERE MaHoaDon = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                    pstmt.setInt(1, Integer.parseInt(maHD.trim()));
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công!");
                    loadHoaDonData(model);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã hóa đơn không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa hóa đơn: " + ex.getMessage());
            }
        }
    }

    private void showEditHoaDonDialog(DefaultTableModel model) {
        String maHD = JOptionPane.showInputDialog(this, "Nhập mã hóa đơn cần sửa:");
        if (maHD == null || maHD.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM HoaDon WHERE MaHoaDon = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maHD.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn với mã " + maHD);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa hóa đơn", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 400);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Mã hợp đồng:"));
                JTextField maHopDongField = new JTextField(String.valueOf(rs.getInt("MaHopDong")));
                formPanel.add(maHopDongField);

                formPanel.add(new JLabel("Mã NVĐP:"));
                JTextField maNvdpField = new JTextField(String.valueOf(rs.getInt("MaNvdp")));
                formPanel.add(maNvdpField);

                // Spinner cho ngày lập
                formPanel.add(new JLabel("Ngày lập:"));
                SpinnerDateModel dateModel = new SpinnerDateModel();
                JSpinner ngayLapSpinner = new JSpinner(dateModel);
                JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ngayLapSpinner, "dd/MM/yyyy");
                ngayLapSpinner.setEditor(dateEditor);
                ngayLapSpinner.setValue(rs.getDate("NgLap"));
                formPanel.add(ngayLapSpinner);

                formPanel.add(new JLabel("Số tiền:"));
                JTextField soTienField = new JTextField(String.valueOf(rs.getDouble("SoTien")));
                formPanel.add(soTienField);

                formPanel.add(new JLabel("Tình trạng:"));
                JTextField tinhTrangField = new JTextField(rs.getString("TinhTrang"));
                formPanel.add(tinhTrangField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                saveButton.addActionListener(e -> {
                    try {
                        // Validate input
                        if (maHopDongField.getText().trim().isEmpty() || 
                            maNvdpField.getText().trim().isEmpty() ||
                            soTienField.getText().trim().isEmpty() ||
                            tinhTrangField.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                            return;
                        }

                        // Get date from spinner
                        Date ngayLap = (Date) ngayLapSpinner.getValue();

                        // Parse số tiền
                        double soTien = Double.parseDouble(soTienField.getText().trim());

                        // Update database
                        String updateSql = "UPDATE HoaDon SET MaHopDong = ?, MaNvdp = ?, NgLap = ?, SoTien = ?, TinhTrang = ? WHERE MaHoaDon = ?";
                        
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, Integer.parseInt(maHopDongField.getText().trim()));
                            updateStmt.setInt(2, Integer.parseInt(maNvdpField.getText().trim()));
                            updateStmt.setDate(3, new java.sql.Date(ngayLap.getTime()));
                            updateStmt.setDouble(4, soTien);
                            updateStmt.setString(5, tinhTrangField.getText().trim());
                            updateStmt.setInt(6, Integer.parseInt(maHD.trim()));
                            
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(dialog, "Cập nhật hóa đơn thành công!");
                            loadHoaDonData(model);
                            dialog.dispose();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Giá trị không hợp lệ!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật hóa đơn: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã hóa đơn không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm hóa đơn: " + ex.getMessage());
        }
    }

    private void showSearchHoaDonDialog(DefaultTableModel model) {
        String maHD = JOptionPane.showInputDialog(this, "Nhập mã hóa đơn cần tìm:");
        if (maHD != null && !maHD.trim().isEmpty()) {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "SELECT * FROM HoaDon WHERE MaHoaDon = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maHD.trim()));
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next()) {
                        // Clear existing table data
                        model.setRowCount(0);
                        
                        // Format date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String ngayLap = rs.getDate("NgLap") != null ? 
                            dateFormat.format(rs.getDate("NgLap")) : "";
                        
                        // Format currency
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        String formattedSoTien = currencyFormat.format(rs.getDouble("SoTien"));
                        
                        // Add the found invoice to the table
                        model.addRow(new Object[]{
                            rs.getInt("MaHoaDon"),
                            rs.getInt("MaHopDong"),
                            rs.getInt("MaNvdp"),
                            ngayLap,
                            formattedSoTien,
                            rs.getString("TinhTrang")
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn với mã " + maHD);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã hóa đơn không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm hóa đơn: " + ex.getMessage());
            }
        }
    }

    private void loadHoaDonData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaHoaDon, MaHopDong, MaNvdp, NgLap, SoTien, TinhTrang FROM HoaDon ORDER BY MaHoaDon";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    // Format giá trị tiền tệ
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    String formattedSoTien = currencyFormat.format(rs.getDouble("SoTien"));
                    
                    // Format ngày tháng
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String ngLap = rs.getDate("NgLap") != null ? dateFormat.format(rs.getDate("NgLap")) : "";
                    
                    // Thêm dòng mới vào bảng
                    model.addRow(new Object[]{
                        rs.getInt("MaHoaDon"),
                        rs.getInt("MaHopDong"),
                        rs.getInt("MaNvdp"),
                        ngLap,
                        formattedSoTien,
                        rs.getString("TinhTrang")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu hóa đơn: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createChamCongPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Quản lý chấm công");
        titleLabel.setFont(titleFont);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton refreshButton = new JButton("Làm mới");
        
        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(editButton);
        styleButton(searchButton);
        styleButton(refreshButton);
        
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã chấm công",
            "Mã nhân viên điều phối",
            "Mã nhân viên thu gom",
            "Ngày công",
            "Ghi chú",
            "Trạng thái"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model); // Initialize the class field
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadChamCongData(model);
        
        // Thêm action listener cho các nút
        addButton.addActionListener(e -> showAddChamCongDialog(model));
        deleteButton.addActionListener(e -> showDeleteChamCongDialog(model));
        editButton.addActionListener(e -> showEditChamCongDialog(model));
        searchButton.addActionListener(e -> showSearchChamCongDialog(model));
        refreshButton.addActionListener(e -> loadChamCongData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void showAddChamCongDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Thêm chấm công mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Mã nhân viên điều phối:"));
        JTextField maNvdpField = new JTextField();
        formPanel.add(maNvdpField);

        formPanel.add(new JLabel("Mã nhân viên thu gom:"));
        JTextField maNvtgField = new JTextField();
        formPanel.add(maNvtgField);

        // Tạo spinner cho ngày công
        formPanel.add(new JLabel("Ngày công:"));
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner ngayCongSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ngayCongSpinner, "dd/MM/yyyy");
        ngayCongSpinner.setEditor(dateEditor);
        formPanel.add(ngayCongSpinner);

        formPanel.add(new JLabel("Ghi chú:"));
        JTextField ghiChuField = new JTextField();
        formPanel.add(ghiChuField);

        formPanel.add(new JLabel("Trạng thái:"));
        JTextField trangThaiField = new JTextField();
        formPanel.add(trangThaiField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Validate input
                if (maNvdpField.getText().trim().isEmpty() || 
                    maNvtgField.getText().trim().isEmpty() ||
                    trangThaiField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }

                // Get date from spinner
                Date ngayCong = (Date) ngayCongSpinner.getValue();

                // Insert into database
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "INSERT INTO ChamCong (MaNvdp, MaNvtg, NgayCong, GhiChu, TrangThai) VALUES (?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maNvdpField.getText().trim()));
                    pstmt.setInt(2, Integer.parseInt(maNvtgField.getText().trim()));
                    pstmt.setDate(3, new java.sql.Date(ngayCong.getTime()));
                    pstmt.setString(4, ghiChuField.getText().trim());
                    pstmt.setString(5, trangThaiField.getText().trim());
                    
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Thêm chấm công thành công!");
                    loadChamCongData(model);
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Mã nhân viên không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm chấm công: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDeleteChamCongDialog(DefaultTableModel model) {
        String maCc = JOptionPane.showInputDialog(this, "Nhập mã chấm công cần xóa:");
        if (maCc != null && !maCc.trim().isEmpty()) {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                // Kiểm tra xem chấm công có tồn tại không
                String checkSql = "SELECT COUNT(*) FROM ChamCong WHERE MaCc = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, Integer.parseInt(maCc.trim()));
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy chấm công với mã " + maCc);
                        return;
                    }
                }

                // Thực hiện xóa
                String deleteSql = "DELETE FROM ChamCong WHERE MaCc = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                    pstmt.setInt(1, Integer.parseInt(maCc.trim()));
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa chấm công thành công!");
                    loadChamCongData(model);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã chấm công không hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa chấm công: " + ex.getMessage());
            }
        }
    }

    private void showEditChamCongDialog(DefaultTableModel model) {
        String maCc = JOptionPane.showInputDialog(this, "Nhập mã chấm công cần sửa:");
        if (maCc == null || maCc.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM ChamCong WHERE MaCc = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maCc.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy chấm công với mã " + maCc);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa thông tin chấm công", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 450);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Mã nhân viên điều phối:"));
                JTextField maNvdpField = new JTextField(String.valueOf(rs.getInt("MaNvdp")));
                formPanel.add(maNvdpField);

                formPanel.add(new JLabel("Mã nhân viên thu gom:"));
                JTextField maNvtgField = new JTextField(String.valueOf(rs.getInt("MaNvtg")));
                formPanel.add(maNvtgField);

                formPanel.add(new JLabel("Ngày công:"));
                SpinnerDateModel dateModel = new SpinnerDateModel();
                JSpinner ngayCongSpinner = new JSpinner(dateModel);
                JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ngayCongSpinner, "dd/MM/yyyy");
                ngayCongSpinner.setEditor(dateEditor);
                ngayCongSpinner.setValue(rs.getDate("NgayCong"));
                formPanel.add(ngayCongSpinner);

                formPanel.add(new JLabel("Ghi chú:"));
                JTextField ghiChuField = new JTextField(rs.getString("GhiChu"));
                formPanel.add(ghiChuField);

                formPanel.add(new JLabel("Trạng thái:"));
                JTextField trangThaiField = new JTextField(rs.getString("TrangThai"));
                formPanel.add(trangThaiField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                saveButton.addActionListener(e -> {
                    try {
                        // Validate input
                        if (maNvdpField.getText().trim().isEmpty() || 
                            maNvtgField.getText().trim().isEmpty() ||
                            trangThaiField.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                            return;
                        }

                        // Get date from spinner
                        Date selectedDate = (Date) ngayCongSpinner.getValue();

                        // Update database
                        String updateSql = "UPDATE ChamCong SET MaNvdp = ?, MaNvtg = ?, NgayCong = ?, GhiChu = ?, TrangThai = ? WHERE MaCc = ?";
                        
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, Integer.parseInt(maNvdpField.getText().trim()));
                            updateStmt.setInt(2, Integer.parseInt(maNvtgField.getText().trim()));
                            updateStmt.setDate(3, new java.sql.Date(selectedDate.getTime()));
                            updateStmt.setString(4, ghiChuField.getText().trim());
                            updateStmt.setString(5, trangThaiField.getText().trim());
                            updateStmt.setInt(6, Integer.parseInt(maCc.trim()));
                            
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(dialog, "Cập nhật chấm công thành công!");
                            loadChamCongData(model);
                            dialog.dispose();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Mã nhân viên không hợp lệ!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật chấm công: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã chấm công không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm chấm công: " + ex.getMessage());
        }
    }

    private void showSearchChamCongDialog(DefaultTableModel model) {
        String maCc = JOptionPane.showInputDialog(this, "Nhập mã chấm công cần tìm:");
        if (maCc != null && !maCc.trim().isEmpty()) {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "SELECT * FROM ChamCong WHERE MaCc = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maCc.trim()));
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next()) {
                        // Clear existing table data
                        model.setRowCount(0);
                        
                        // Add the found record to the table
                        model.addRow(new Object[]{
                            rs.getInt("MaCc"),
                            rs.getInt("MaNvdp"),
                            rs.getInt("MaNvtg"),
                            rs.getDate("NgayCong"),
                            rs.getString("GhiChu"),
                            rs.getString("TrangThai")
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy chấm công với mã " + maCc);
                        loadChamCongData(model); // Load lại toàn bộ dữ liệu nếu không tìm thấy
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã chấm công không hợp lệ!");
                loadChamCongData(model); // Load lại dữ liệu nếu nhập sai định dạng
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm chấm công: " + ex.getMessage());
                loadChamCongData(model); // Load lại dữ liệu nếu có lỗi SQL
            }
        } else {
            // Nếu người dùng không nhập gì hoặc bấm Cancel, load lại toàn bộ dữ liệu
            loadChamCongData(model);
        }
    }

    private void loadChamCongData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaCc, MaNvdp, MaNvtg, NgayCong, GhiChu, TrangThai FROM ChamCong ORDER BY NgayCong DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("MaCc"),
                        rs.getInt("MaNvdp"),
                        rs.getInt("MaNvtg"),
                        dateFormat.format(rs.getDate("NgayCong")),
                        rs.getString("GhiChu"),
                        rs.getString("TrangThai")
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải dữ liệu chấm công: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createThongKePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Thống kê báo cáo");
        titleLabel.setFont(titleFont);
        
        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] months = {"Tháng 1/2024", "Tháng 2/2024", "Tháng 3/2024"};
        JComboBox<String> monthPicker = new JComboBox<>(months);
        JButton exportButton = new JButton("Xuất báo cáo");
        styleButton(exportButton);
        controlPanel.add(new JLabel("Chọn tháng: "));
        controlPanel.add(monthPicker);
        controlPanel.add(exportButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(controlPanel, BorderLayout.EAST);
        
        // Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentPanel.setBackground(Color.WHITE);

        // Thêm các panel thống kê
        // Panel 1: Thống kê khối lượng rác
        JPanel weightPanel = new JPanel(new BorderLayout(10, 10));
        weightPanel.setBackground(new Color(240, 240, 240));
        weightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel weightTitle = new JLabel("Thống kê khối lượng rác");
        weightTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel weightContent = new JLabel("<html>Tổng khối lượng: 0 kg<br>Rác sinh hoạt: 0 kg<br>Rác tái chế: 0 kg<br>Rác nguy hại: 0 kg</html>");
        weightPanel.add(weightTitle, BorderLayout.NORTH);
        weightPanel.add(weightContent, BorderLayout.CENTER);
        
        // Panel 2: Thống kê doanh thu
        JPanel revenuePanel = new JPanel(new BorderLayout(10, 10));
        revenuePanel.setBackground(new Color(240, 240, 240));
        revenuePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel revenueTitle = new JLabel("Thống kê doanh thu");
        revenueTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel revenueContent = new JLabel("<html>Tổng doanh thu: 0đ<br>Đã thu: 0đ<br>Chưa thu: 0đ</html>");
        revenuePanel.add(revenueTitle, BorderLayout.NORTH);
        revenuePanel.add(revenueContent, BorderLayout.CENTER);
        
        // Panel 3: Thống kê nhân viên
        JPanel staffPanel = new JPanel(new BorderLayout(10, 10));
        staffPanel.setBackground(new Color(240, 240, 240));
        staffPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel staffTitle = new JLabel("Thống kê nhân viên");
        staffTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel staffContent = new JLabel("<html>Tổng số nhân viên: 0<br>Điều phối: 0<br>Thu gom: 0<br>Tài xế: 0</html>");
        staffPanel.add(staffTitle, BorderLayout.NORTH);
        staffPanel.add(staffContent, BorderLayout.CENTER);
        
        // Panel 4: Đánh giá dịch vụ
        JPanel ratingPanel = new JPanel(new BorderLayout(10, 10));
        ratingPanel.setBackground(new Color(240, 240, 240));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel ratingTitle = new JLabel("Đánh giá dịch vụ");
        ratingTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel ratingContent = new JLabel("<html>Điểm đánh giá TB: 0/5<br>Số lượt đánh giá: 0<br>Phản hồi tích cực: 0%</html>");
        ratingPanel.add(ratingTitle, BorderLayout.NORTH);
        ratingPanel.add(ratingContent, BorderLayout.CENTER);

        contentPanel.add(weightPanel);
        contentPanel.add(revenuePanel);
        contentPanel.add(staffPanel);
        contentPanel.add(ratingPanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createDangXuatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Panel chứa nội dung chính
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        // Icon đăng xuất (có thể thay bằng ImageIcon thực tế)
        JLabel iconLabel = new JLabel("🚪", JLabel.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Label xác nhận
        JLabel confirmLabel = new JLabel("Bạn có chắc chắn muốn đăng xuất?", JLabel.CENTER);
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 18));
        confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Label thông tin phiên làm việc
        JLabel sessionLabel = new JLabel("Phiên làm việc sẽ kết thúc và bạn sẽ cần đăng nhập lại.", JLabel.CENTER);
        sessionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panel chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        
        // Nút Hủy
        JButton cancelButton = new JButton("Hủy");
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(secondaryColor);
        cancelButton.setBorder(BorderFactory.createLineBorder(secondaryColor));
        cancelButton.setFocusPainted(false);
        
        // Nút Đăng xuất
        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(null);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            dispose();
            new GiaoDienDangNhap().setVisible(true);
        });
        
        // Thêm các components vào panel
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(confirmLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(sessionLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(logoutButton);
        
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadTuyenThuGomData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaTuyen, MaDv, TenTuyen, KhuVuc FROM TuyenDuongThuGom ORDER BY MaTuyen";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("MaTuyen"),
                        rs.getInt("MaDv"),
                        rs.getString("TenTuyen"),
                        rs.getString("KhuVuc")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu tuyến thu gom: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showSearchTuyenThuGomDialog(DefaultTableModel model) {
        String maTuyen = JOptionPane.showInputDialog(this, "Nhập mã tuyến cần tìm:");
        if (maTuyen != null && !maTuyen.trim().isEmpty()) {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "SELECT * FROM TuyenDuongThuGom WHERE MaTuyen = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(maTuyen.trim()));
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next()) {
                        // Clear existing table data
                        model.setRowCount(0);
                        
                        // Add the found route to the table
                        model.addRow(new Object[]{
                            rs.getInt("MaTuyen"),
                            rs.getInt("MaDv"),
                            rs.getString("TenTuyen"),
                            rs.getString("KhuVuc")
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy tuyến thu gom với mã " + maTuyen);
                        loadTuyenThuGomData(model); // Load lại toàn bộ dữ liệu nếu không tìm thấy
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã tuyến không hợp lệ!");
                loadTuyenThuGomData(model); // Load lại dữ liệu nếu nhập sai định dạng
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm tuyến thu gom: " + ex.getMessage());
                loadTuyenThuGomData(model); // Load lại dữ liệu nếu có lỗi SQL
            }
        } else {
            // Nếu người dùng không nhập gì hoặc bấm Cancel, load lại toàn bộ dữ liệu
            loadTuyenThuGomData(model);
        }
    }

    private void showDeletePhanAnhDialog(DefaultTableModel model) {
        String maPA = JOptionPane.showInputDialog(this, "Nhập mã phản ánh cần xóa:");
        if (maPA == null || maPA.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM PhanAnh WHERE MaPA = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maPA.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không có mã phản ánh " + maPA);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa phản ánh này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    sql = "DELETE FROM PhanAnh WHERE MaPA = ?";
                    try (PreparedStatement deleteStmt = conn.prepareStatement(sql)) {
                        deleteStmt.setInt(1, Integer.parseInt(maPA.trim()));
                        int result = deleteStmt.executeUpdate();
                        
                        if (result > 0) {
                            JOptionPane.showMessageDialog(this, "Xóa phản ánh thành công!");
                            loadPhanAnhData(model);
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã phản ánh không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa phản ánh: " + ex.getMessage());
        }
    }

    private void showEditPhanAnhDialog(DefaultTableModel model) {
        String maPA = JOptionPane.showInputDialog(this, "Nhập mã phản ánh cần sửa:");
        if (maPA == null || maPA.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM PhanAnh WHERE MaPA = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maPA.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không có mã phản ánh " + maPA);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa thông tin phản ánh", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Mã chủ thải:"));
                JTextField maChuThaiField = new JTextField(String.valueOf(rs.getInt("MaChuThai")));
                formPanel.add(maChuThaiField);

                formPanel.add(new JLabel("Nội dung:"));
                JTextField noiDungField = new JTextField(rs.getString("NoiDung"));
                formPanel.add(noiDungField);

                formPanel.add(new JLabel("Trạng thái:"));
                JTextField trangThaiField = new JTextField(rs.getString("TrangThai"));
                formPanel.add(trangThaiField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);

                saveButton.addActionListener(e -> {
                    try {
                        String updateSql = "UPDATE PhanAnh SET MaChuThai = ?, NoiDung = ?, TrangThai = ? WHERE MaPA = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, Integer.parseInt(maChuThaiField.getText().trim()));
                            updateStmt.setString(2, noiDungField.getText().trim());
                            updateStmt.setString(3, trangThaiField.getText().trim());
                            updateStmt.setInt(4, Integer.parseInt(maPA.trim()));
                            
                            int result = updateStmt.executeUpdate();
                            if (result > 0) {
                                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                                loadPhanAnhData(model);
                                dialog.dispose();
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã phản ánh không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa phản ánh: " + ex.getMessage());
        }
    }

    private void showSearchPhanAnhDialog(DefaultTableModel model) {
        String maPA = JOptionPane.showInputDialog(this, "Nhập mã phản ánh cần tìm:");
        if (maPA == null || maPA.trim().isEmpty()) {
            loadPhanAnhData(model);
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM PhanAnh WHERE MaPA = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maPA.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    model.setRowCount(0);
                    model.addRow(new Object[]{
                        rs.getInt("MaPA"),
                        rs.getInt("MaChuThai"),
                        rs.getString("NoiDung"),
                        rs.getDate("ThoiGianGui"),
                        rs.getString("TrangThai")
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Không có mã phản ánh " + maPA);
                    loadPhanAnhData(model);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã phản ánh không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm phản ánh: " + ex.getMessage());
        }
    }

    private void showDeleteYeuCauDatLichDialog(DefaultTableModel model) {
        String maYC = JOptionPane.showInputDialog(this, "Nhập mã yêu cầu đặt lịch cần xóa:");
        if (maYC == null || maYC.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM YeuCauDatLich WHERE MaYC = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maYC.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không có mã yêu cầu đặt lịch " + maYC);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa yêu cầu đặt lịch này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    sql = "DELETE FROM YeuCauDatLich WHERE MaYC = ?";
                    try (PreparedStatement deleteStmt = conn.prepareStatement(sql)) {
                        deleteStmt.setInt(1, Integer.parseInt(maYC.trim()));
                        int result = deleteStmt.executeUpdate();
                        
                        if (result > 0) {
                            JOptionPane.showMessageDialog(this, "Xóa yêu cầu đặt lịch thành công!");
                            loadYeuCauDatLichData(model);
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã yêu cầu đặt lịch không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa yêu cầu đặt lịch: " + ex.getMessage());
        }
    }

    private void showEditYeuCauDatLichDialog(DefaultTableModel model) {
        String maYC = JOptionPane.showInputDialog(this, "Nhập mã yêu cầu đặt lịch cần sửa:");
        if (maYC == null || maYC.trim().isEmpty()) {
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM YeuCauDatLich WHERE MaYC = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maYC.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không có mã yêu cầu đặt lịch " + maYC);
                    return;
                }

                // Create edit dialog
                JDialog dialog = new JDialog(this, "Sửa thông tin yêu cầu đặt lịch", true);
                dialog.setLayout(new BorderLayout(10, 10));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(this);

                JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
                formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                formPanel.add(new JLabel("Mã chủ thải:"));
                JTextField maChuThaiField = new JTextField(String.valueOf(rs.getInt("MaChuThai")));
                formPanel.add(maChuThaiField);

                formPanel.add(new JLabel("Ngày thu gom:"));
                JTextField ngayThuGomField = new JTextField(rs.getString("NgayThuGom"));
                formPanel.add(ngayThuGomField);

                formPanel.add(new JLabel("Giờ thu gom:"));
                JTextField gioThuGomField = new JTextField(rs.getString("GioThuGom"));
                formPanel.add(gioThuGomField);

                formPanel.add(new JLabel("Loại rác:"));
                JTextField loaiRacField = new JTextField(rs.getString("LoaiRac"));
                formPanel.add(loaiRacField);

                formPanel.add(new JLabel("Trạng thái:"));
                JTextField trangThaiField = new JTextField(rs.getString("TrangThai"));
                formPanel.add(trangThaiField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Lưu");
                JButton cancelButton = new JButton("Hủy");
                
                styleButton(saveButton);
                styleButton(cancelButton);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(saveButton);

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);

                saveButton.addActionListener(e -> {
                    try {
                        String updateSql = "UPDATE YeuCauDatLich SET MaChuThai = ?, NgayThuGom = ?, GioThuGom = ?, LoaiRac = ?, TrangThai = ? WHERE MaYC = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, Integer.parseInt(maChuThaiField.getText().trim()));
                            updateStmt.setString(2, ngayThuGomField.getText().trim());
                            updateStmt.setString(3, gioThuGomField.getText().trim());
                            updateStmt.setString(4, loaiRacField.getText().trim());
                            updateStmt.setString(5, trangThaiField.getText().trim());
                            updateStmt.setInt(6, Integer.parseInt(maYC.trim()));
                            
                            int result = updateStmt.executeUpdate();
                            if (result > 0) {
                                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                                loadYeuCauDatLichData(model);
                                dialog.dispose();
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật: " + ex.getMessage());
                    }
                });

                cancelButton.addActionListener(e -> dialog.dispose());
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã yêu cầu đặt lịch không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa yêu cầu đặt lịch: " + ex.getMessage());
        }
    }

    private void showSearchYeuCauDatLichDialog(DefaultTableModel model) {
        String maYC = JOptionPane.showInputDialog(this, "Nhập mã yêu cầu đặt lịch cần tìm:");
        if (maYC == null || maYC.trim().isEmpty()) {
            loadYeuCauDatLichData(model);
            return;
        }

        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM YeuCauDatLich WHERE MaYC = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(maYC.trim()));
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    model.setRowCount(0);
                    model.addRow(new Object[]{
                        rs.getInt("MaYC"),
                        rs.getInt("MaChuThai"),
                        rs.getString("NgayThuGom"),
                        rs.getString("GioThuGom"),
                        rs.getString("LoaiRac"),
                        rs.getString("TrangThai")
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Không có mã yêu cầu đặt lịch " + maYC);
                    loadYeuCauDatLichData(model);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã yêu cầu đặt lịch không hợp lệ!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm yêu cầu đặt lịch: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLyRacThaiUI().setVisible(true);
        });
    }
}
