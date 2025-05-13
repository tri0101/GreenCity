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
import java.util.Locale;

public class QuanLyRacThaiUI extends JFrame {
    private JPanel sideBar, mainPanel, headerPanel;
    private CardLayout cardLayout;
    private Color primaryColor = new Color(25, 42, 86);    // Xanh đậm
    private Color secondaryColor = new Color(46, 64, 83);  // Xanh nhạt
    private Color accentColor = new Color(46, 204, 113);   // Xanh lá
    private Color textColor = Color.WHITE;
    private Font titleFont = new Font("Arial", Font.BOLD, 24);
    private Font normalFont = new Font("Arial", Font.PLAIN, 14);

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
        JButton addButton = new JButton("Thêm tuyến thu gom");
        JButton refreshButton = new JButton("Làm mới");
        styleButton(addButton);
        styleButton(refreshButton);
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
                return false; // Không cho phép edit trực tiếp trên bảng
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadTuyenThuGomData(model);
        
        // Thêm action listener cho nút làm mới
        refreshButton.addActionListener(e -> loadTuyenThuGomData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
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
                        rs.getString("NgThu"),  // Lấy NgThu dưới dạng String
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
        JButton addButton = new JButton("Thêm lịch");
        JButton refreshButton = new JButton("Làm mới");
        styleButton(addButton);
        styleButton(refreshButton);
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã lịch",
            "Mã nhân viên điều phối",
            "Thứ thu",
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
        
        // Thêm action listener cho nút làm mới
        refreshButton.addActionListener(e -> loadLichThuGomData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
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
        JButton addButton = new JButton("Thêm phân công");
        JButton refreshButton = new JButton("Làm mới");
        styleButton(addButton);
        styleButton(refreshButton);
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
                return false; // Không cho phép edit trực tiếp trên bảng
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadPhanCongData(model);
        
        // Thêm action listener cho nút làm mới
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

        JLabel titleLabel = new JLabel("Báo cáo thống kê");
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel reportPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        reportPanel.setBackground(Color.WHITE);

        // Thêm các card báo cáo
        addReportCard(reportPanel, "Thống kê lượng rác", "Tổng khối lượng: 0 kg");
        addReportCard(reportPanel, "Doanh thu", "Tổng doanh thu: 0đ");
        addReportCard(reportPanel, "Hiệu suất nhân viên", "Đạt: 0%");
        addReportCard(reportPanel, "Mức độ hài lòng", "Đánh giá: 0/5");

        panel.add(reportPanel, BorderLayout.CENTER);
        return panel;
    }

    private void addReportCard(JPanel parent, String title, String content) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(new Color(240, 240, 240));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel contentLabel = new JLabel(content);
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(contentLabel, BorderLayout.CENTER);
        parent.add(card);
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
        JButton refreshButton = new JButton("Làm mới");
        styleButton(refreshButton);
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
        
        // Thêm action listener cho nút làm mới
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
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("MaPA"),
                        rs.getInt("MaChuThai"),
                        rs.getString("NoiDung"),
                        rs.getDate("ThoiGianGui"),
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
        JButton refreshButton = new JButton("Làm mới");
        styleButton(refreshButton);
        buttonPanel.add(refreshButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {
            "Mã yêu cầu",
            "Mã chủ thải",
            "Mã lịch",
            "Thời gian yêu cầu",
            "Ghi chú",
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
        
        // Thêm action listener cho nút làm mới
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
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("MaYc"),
                        rs.getInt("MaChuThai"),
                        rs.getInt("MaLich"),
                        rs.getDate("ThoiGianYc"),
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
        JButton addButton = new JButton("Thêm đơn vị thu gom");
        JButton refreshButton = new JButton("Làm mới");
        styleButton(addButton);
        styleButton(refreshButton);
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
                return false; // Không cho phép edit trực tiếp trên bảng
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadDvtgData(model);
        
        // Thêm action listener cho nút làm mới
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
        JButton addButton = new JButton("Thêm nhân viên thu gom");
        JButton refreshButton = new JButton("Làm mới");
        styleButton(addButton);
        styleButton(refreshButton);
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
            "Tên trưởng nhóm" // Thêm cột này để hiển thị tên trưởng nhóm
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép edit trực tiếp trên bảng
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadNvtgData(model);
        
        // Thêm action listener cho nút làm mới
        refreshButton.addActionListener(e -> loadNvtgData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
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
        JButton addButton = new JButton("Thêm hợp đồng");
        JButton refreshButton = new JButton("Làm mới");
        styleButton(addButton);
        styleButton(refreshButton);
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
                return false; // Không cho phép edit trực tiếp trên bảng
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadHopDongData(model);
        
        // Thêm action listener cho nút làm mới
        refreshButton.addActionListener(e -> loadHopDongData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
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
        JButton addButton = new JButton("Tạo hóa đơn");
        JButton refreshButton = new JButton("Làm mới");
        styleButton(addButton);
        styleButton(refreshButton);
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
                return false; // Không cho phép edit trực tiếp trên bảng
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadHoaDonData(model);
        
        // Thêm action listener cho nút làm mới
        refreshButton.addActionListener(e -> loadHoaDonData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
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
        JButton addButton = new JButton("Thêm chấm công");
        JButton refreshButton = new JButton("Làm mới");
        styleButton(addButton);
        styleButton(refreshButton);
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
                return false; // Không cho phép edit trực tiếp trên bảng
            }
        };
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load dữ liệu từ database
        loadChamCongData(model);
        
        // Thêm action listener cho nút làm mới
        refreshButton.addActionListener(e -> loadChamCongData(model));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadChamCongData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaCc, MaNvdp, MaNvtg, NgayCong, GhiChu, TrangThai FROM ChamCong ORDER BY MaCc";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    // Format ngày công
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String ngayCong = rs.getDate("NgayCong") != null ? dateFormat.format(rs.getDate("NgayCong")) : "";
                    
                    model.addRow(new Object[]{
                        rs.getInt("MaCc"),
                        rs.getInt("MaNvdp"),
                        rs.getInt("MaNvtg"),
                        ngayCong,
                        rs.getString("GhiChu"),
                        rs.getString("TrangThai")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải dữ liệu chấm công: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLyRacThaiUI().setVisible(true);
        });
    }
}
