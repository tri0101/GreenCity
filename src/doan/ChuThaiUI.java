package doan;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.util.Date;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Component;
import javax.swing.JSplitPane;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Calendar;
import javax.swing.SpinnerDateModel;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ChuThaiUI extends JFrame {
    private JPanel sideBar, mainPanel;
    private CardLayout cardLayout;
    private String tenKhachHang;
    private String maKhachHang;

    public ChuThaiUI(String tenKhachHang, String maKhachHang) {
        this.tenKhachHang = tenKhachHang;
        this.maKhachHang = maKhachHang;
        
        setTitle("Hệ thống quản lý rác thải - Giao diện chủ thải");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo sidebar
        sideBar = new JPanel();
        sideBar.setLayout(new GridLayout(9, 1, 0, 10));
        sideBar.setPreferredSize(new Dimension(200, 0));
        sideBar.setBackground(new Color(25, 42, 86));
        sideBar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Panel thông tin người dùng
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new GridLayout(2, 1));
        userInfoPanel.setBackground(new Color(25, 42, 86));
        JLabel nameLabel = new JLabel("Xin chào, " + tenKhachHang);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel idLabel = new JLabel("Mã KH: " + maKhachHang);
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userInfoPanel.add(nameLabel);
        userInfoPanel.add(idLabel);
        sideBar.add(userInfoPanel);

        // Các nút menu
        String[] menuItems = {
            "Đặt lịch thu gom",
            "Lịch sử đặt lịch",
            "Gửi phản ánh",
            "Xem hóa đơn",
            "Danh sách hợp đồng",
            "Hướng dẫn phân loại",
            "Cài đặt",
            "Đăng xuất"
        };

        for (String item : menuItems) {
            JButton button = createMenuButton(item);
            sideBar.add(button);
        }

        // Main content
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Thêm các panel chức năng
        mainPanel.add(createDatLichPanel(), "Đặt lịch thu gom");
        mainPanel.add(createLichSuPanel(), "Lịch sử đặt lịch");
        mainPanel.add(createPhanAnhPanel(), "Gửi phản ánh");
        mainPanel.add(createHoaDonPanel(), "Xem hóa đơn");
        mainPanel.add(createHopDongPanel(), "Danh sách hợp đồng");
        mainPanel.add(createHuongDanPanel(), "Hướng dẫn phân loại");
        mainPanel.add(createSettingsPanel(), "Cài đặt");
        mainPanel.add(createDangXuatPanel(), "Đăng xuất");

        add(sideBar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(46, 64, 83));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.addActionListener(e -> cardLayout.show(mainPanel, text));
        return button;
    }

    private JPanel createDatLichPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Đặt lịch thu gom rác", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form đặt lịch
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        formPanel.add(new JLabel("Ngày bắt đầu thu gom:"));
        
        // Tạo JSpinner cho ngày tháng năm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();
        
        // Thêm 1 ngày vào ngày hiện tại để đảm bảo ngày chọn phải lớn hơn ngày hiện tại
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date minDate = calendar.getTime();
        
        Calendar maxCal = Calendar.getInstance();
        maxCal.set(2030, Calendar.DECEMBER, 31, 23, 59, 59);
        Date maxDate = maxCal.getTime();
        
        SpinnerDateModel dateModel = new SpinnerDateModel(minDate, minDate, maxDate, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        
        JFormattedTextField ftf = ((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField();
        ftf.setEditable(true);
        ftf.setHorizontalAlignment(JTextField.LEFT);
        ftf.setFont(new Font("Arial", Font.PLAIN, 14));
        
        dateSpinner.addChangeListener(e -> {
            Date selectedDate = (Date) dateSpinner.getValue();
            if (selectedDate.before(minDate)) {
                dateSpinner.setValue(minDate);
            } else if (selectedDate.after(maxDate)) {
                dateSpinner.setValue(maxDate);
            }
        });
        
        formPanel.add(dateSpinner);

        formPanel.add(new JLabel("Giờ thu gom:"));
        String[] times = {"7:00", "8:00", "9:00", "10:00", "14:00", "15:00", "16:00"};
        JComboBox<String> timeBox = new JComboBox<>(times);
        formPanel.add(timeBox);

        // Thêm ComboBox chọn loại rác
        formPanel.add(new JLabel("Chọn loại rác:"));
        String[] wasteTypes = {"Rác sinh hoạt", "Rác công nghiệp", "Rác tái chế"};
        JComboBox<String> wasteTypeBox = new JComboBox<>(wasteTypes);
        formPanel.add(wasteTypeBox);

        // Panel chứa hai ComboBox kế bên nhau
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // ComboBox 1 (sẽ thay đổi giá trị dựa trên ComboBox 2)
        JComboBox<String> comboBox1 = new JComboBox<>();
        
        // ComboBox 2 (chọn tháng hoặc năm)
        String[] options = {"Tháng", "Năm"};
        JComboBox<String> comboBox2 = new JComboBox<>(options);
        
        // Thêm sự kiện cho ComboBox 2
        comboBox2.addActionListener(e -> {
            String selected = (String) comboBox2.getSelectedItem();
            comboBox1.removeAllItems();
            
            if ("Tháng".equals(selected)) {
                for (int i = 1; i <= 12; i++) {
                    comboBox1.addItem(String.valueOf(i));
                }
            } else if ("Năm".equals(selected)) {
                for (int i = 1; i <= 5; i++) {
                    comboBox1.addItem(String.valueOf(i));
                }
            }
        });
        
        // Trigger sự kiện lần đầu để set giá trị mặc định
        comboBox2.setSelectedItem("Tháng");
        
        comboPanel.add(comboBox1);
        comboPanel.add(comboBox2);
        
        formPanel.add(new JLabel("Chọn thời hạn"));
        formPanel.add(comboPanel);

        formPanel.add(new JLabel("Địa chỉ thu gom:"));
        JTextField addressField = new JTextField();
        formPanel.add(addressField);

        formPanel.add(new JLabel("Khối lượng ước tính (kg):"));
        JTextField weightField = new JTextField();
        formPanel.add(weightField);

        formPanel.add(new JLabel("Ghi chú:"));
        JTextField noteField = new JTextField();
        formPanel.add(noteField);

        // Nút đặt lịch
        JButton submitButton = new JButton("Xác nhận đặt lịch");
        submitButton.setBackground(new Color(46, 204, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đã gửi yêu cầu đặt lịch thành công!"));
        formPanel.add(submitButton);

        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLichSuPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Lịch sử thu gom", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Tạo bảng lịch sử
        String[] columnNames = {"Ngày", "Giờ", "Loại rác", "Khối lượng", "Trạng thái"};
        Object[][] data = {};

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPhanAnhPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel chứa tiêu đề và nút gửi phản ánh
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Danh sách phản ánh", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton addButton = new JButton("Gửi phản ánh");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Card layout để chuyển đổi giữa danh sách và form gửi
        CardLayout cardLayout = new CardLayout();
        JPanel mainContent = new JPanel(cardLayout);

        // Panel danh sách phản ánh
        JPanel listPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã PA", "Nội dung", "Thời gian gửi", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Load dữ liệu từ database
        loadPhanAnhData(model);

        // Panel form gửi phản ánh
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JLabel contentLabel = new JLabel("Nội dung phản ánh:");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(contentLabel, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea();
        contentArea.setRows(10);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane formScrollPane = new JScrollPane(contentArea);
        formPanel.add(formScrollPane, BorderLayout.CENTER);

        JButton submitButton = new JButton("Gửi phản ánh");
        submitButton.setBackground(new Color(46, 204, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton backButton = new JButton("Quay lại");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        formButtonPanel.add(submitButton);
        formButtonPanel.add(backButton);
        formPanel.add(formButtonPanel, BorderLayout.SOUTH);

        // Thêm các panel vào card layout
        mainContent.add(listPanel, "list");
        mainContent.add(formPanel, "form");
        panel.add(mainContent, BorderLayout.CENTER);

        // Xử lý sự kiện nút
        addButton.addActionListener(e -> {
            cardLayout.show(mainContent, "form");
            titleLabel.setText("Gửi phản ánh");
            addButton.setVisible(false);
        });

        backButton.addActionListener(e -> {
            cardLayout.show(mainContent, "list");
            titleLabel.setText("Danh sách phản ánh");
            addButton.setVisible(true);
        });

        submitButton.addActionListener(e -> {
            String noiDung = contentArea.getText().trim();
            if (noiDung.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nội dung không được để trống!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Connection conn = ConnectionJDBC.getConnection();
                String sql = "INSERT INTO PhanAnh (MaChuThai, NoiDung, ThoiGianGui, TrangThai) VALUES (?, ?, SYSDATE, 'dang xu ly')";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, maKhachHang);
                    pstmt.setString(2, noiDung);
                    
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Gửi phản ánh thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                        contentArea.setText(""); // Clear the text area
                        loadPhanAnhData(model); // Refresh the table
                        cardLayout.show(mainContent, "list"); // Switch back to list
                        titleLabel.setText("Danh sách phản ánh");
                        addButton.setVisible(true);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi gửi phản ánh: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        return panel;
    }

    private void loadPhanAnhData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MaPA, NoiDung, ThoiGianGui, TrangThai FROM PhanAnh WHERE MaChuThai = ? ORDER BY ThoiGianGui DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maKhachHang);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MaPA"),
                        rs.getString("NoiDung"),
                        rs.getTimestamp("ThoiGianGui"),
                        rs.getString("TrangThai")
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách phản ánh: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createHoaDonPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Xem hóa đơn", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Mã hóa đơn", "Ngày", "Dịch vụ", "Số tiền", "Trạng thái"};
        Object[][] data = {};

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHopDongPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Danh sách hợp đồng", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Tạo JSplitPane để chia màn hình thành 2 phần theo chiều dọc
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);

        // Panel cho bảng hợp đồng
        JPanel hopDongPanel = new JPanel(new BorderLayout());
        JLabel hopDongLabel = new JLabel("Danh sách hợp đồng", JLabel.CENTER);
        hopDongLabel.setFont(new Font("Arial", Font.BOLD, 16));
        hopDongPanel.add(hopDongLabel, BorderLayout.NORTH);

        // Tạo bảng hợp đồng
        String[] hopDongColumns = {
            "Mã hợp đồng", 
            "Mã chủ thải", 
            "Loại hợp đồng",
            "Ngày bắt đầu",
            "Ngày kết thúc",
            "Giá trị",
            "Mô tả",
            "Trạng thái"
        };

        DefaultTableModel hopDongModel = new DefaultTableModel(hopDongColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable hopDongTable = new JTable(hopDongModel);
        JScrollPane hopDongScrollPane = new JScrollPane(hopDongTable);
        hopDongPanel.add(hopDongScrollPane, BorderLayout.CENTER);

        // Panel cho bảng chi tiết hợp đồng
        JPanel chiTietPanel = new JPanel(new BorderLayout());
        JLabel chiTietLabel = new JLabel("Chi tiết hợp đồng", JLabel.CENTER);
        chiTietLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chiTietPanel.add(chiTietLabel, BorderLayout.NORTH);

        // Tạo bảng chi tiết hợp đồng
        String[] chiTietColumns = {
            "Địa chỉ thu gom",
            "Tên dịch vụ",
            "Đơn vị tính",
            "Đơn giá",
            "Số lượng",
            "Thành tiền",
            "Ghi chú"
        };

        DefaultTableModel chiTietModel = new DefaultTableModel(chiTietColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable chiTietTable = new JTable(chiTietModel);
        JScrollPane chiTietScrollPane = new JScrollPane(chiTietTable);
        chiTietPanel.add(chiTietScrollPane, BorderLayout.CENTER);

        // Thêm các panel vào split pane
        splitPane.setTopComponent(hopDongPanel);
        splitPane.setBottomComponent(chiTietPanel);
        panel.add(splitPane, BorderLayout.CENTER);

        // Load dữ liệu hợp đồng từ database
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM HopDong WHERE MaChuThai = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maKhachHang);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MaHopDong"),
                        rs.getString("MaChuThai"),
                        rs.getString("LoaiHopDong"),
                        rs.getDate("NgBatDau"),
                        rs.getDate("NgKetThuc"),
                        String.format("%,.2f", rs.getDouble("GiaTri")),
                        rs.getString("MoTa"),
                        rs.getString("TrangThai")
                    };
                    hopDongModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách hợp đồng: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        // Thêm sự kiện click vào bảng hợp đồng
        hopDongTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = hopDongTable.getSelectedRow();
                if (selectedRow != -1) {
                    String maHopDong = hopDongTable.getValueAt(selectedRow, 0).toString();
                    loadChiTietHopDong(maHopDong, chiTietModel);
                }
            }
        });

        // Điều chỉnh độ rộng các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < hopDongTable.getColumnCount(); i++) {
            hopDongTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        for (int i = 0; i < chiTietTable.getColumnCount(); i++) {
            chiTietTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return panel;
    }

    private void loadChiTietHopDong(String maHopDong, DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT * FROM ChiTietHopDong WHERE MaHopDong = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maHopDong);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("DiaChiThuGom"),
                        rs.getString("TenDichVu"),
                        rs.getString("DonViTinh"),
                        String.format("%,.2f", rs.getDouble("DonGia")),
                        rs.getInt("SoLuong"),
                        String.format("%,.2f", rs.getDouble("ThanhTien")),
                        rs.getString("GhiChu")
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải chi tiết hợp đồng: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createHuongDanPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Hướng dẫn phân loại rác", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Panel chứa danh sách loại rác
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel hiển thị chi tiết
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tạo scroll pane cho danh sách
        JScrollPane listScrollPane = new JScrollPane(listPanel);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        listScrollPane.setPreferredSize(new Dimension(200, 0));

        // Tạo scroll pane cho panel chi tiết với chỉ thanh cuộn dọc
        JScrollPane detailScrollPane = new JScrollPane(detailPanel);
        detailScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        detailScrollPane.setPreferredSize(new Dimension(750, 0));

        // Label mặc định cho panel chi tiết
        JLabel defaultLabel = new JLabel("Chọn một loại rác để xem chi tiết", JLabel.CENTER);
        defaultLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        detailPanel.add(defaultLabel);

        // Danh sách các loại rác cố định với màu sắc
        String[][] loaiRac = {
            {"1", "Rác Hữu Cơ", "#4CAF50"},     // Green
            {"2", "Rác Vô Cơ", "#2196F3"},     // Blue
            {"3", "Rác Tái Chế", "#FF9800"},    // Orange
            {"4", "Rác Nguy Hại", "#F44336"},   // Red
            {"5", "Các loại rác khác", "#9C27B0"} // Purple
        };

        for (String[] loai : loaiRac) {
            JPanel buttonPanel = new JPanel(new BorderLayout());
            buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            buttonPanel.setOpaque(false);
            
            JButton itemButton = new JButton(loai[1]);
            itemButton.setHorizontalAlignment(SwingConstants.LEFT);
            itemButton.setFont(new Font("Arial", Font.BOLD, 14));
            itemButton.setBorderPainted(false);
            itemButton.setContentAreaFilled(true);
            itemButton.setFocusPainted(false);
            itemButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            itemButton.setBackground(Color.decode(loai[2]));
            itemButton.setForeground(Color.WHITE);
            
            // Add hover effect
            itemButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    itemButton.setBackground(itemButton.getBackground().brighter());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    itemButton.setBackground(Color.decode(loai[2]));
                }
            });
            
            final String maLoai = loai[0];
            
            // Add click event to show details
            itemButton.addActionListener(e -> {
                showWasteDetails(detailPanel, Integer.parseInt(maLoai));
            });
            
            buttonPanel.add(itemButton);
            listPanel.add(buttonPanel);
            listPanel.add(Box.createVerticalStrut(5));
        }

        // Tạo JSplitPane để chia màn hình thành 2 phần
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                            listScrollPane, detailScrollPane);
        splitPane.setDividerLocation(200);
        splitPane.setEnabled(false);
        splitPane.setOneTouchExpandable(false);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private void showWasteDetails(JPanel detailPanel, int maLoai) {
        detailPanel.removeAll();
        
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT MoTa, HdPhanLoai FROM TtPhanLoaiRac WHERE MaLoaiRac = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, maLoai);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    // Panel chứa tất cả nội dung
                    JPanel contentPanel = new JPanel();
                    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
                    contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                    
                    // Tạo panel cho tiêu đề
                    String tenLoai = "";
                    switch(maLoai) {
                        case 1: tenLoai = "Rác Hữu Cơ"; break;
                        case 2: tenLoai = "Rác Vô Cơ"; break;
                        case 3: tenLoai = "Rác Tái Chế"; break;
                        case 4: tenLoai = "Rác Nguy Hại"; break;
                        case 5: tenLoai = "Các loại rác khác"; break;
                    }
                    
                    JLabel titleLabel = new JLabel(tenLoai);
                    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
                    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentPanel.add(titleLabel);
                    contentPanel.add(Box.createVerticalStrut(20));
                    
                    // Hiển thị hình ảnh từ package Image
                    String imageName = "";
                    switch(maLoai) {
                        case 1: imageName = "RacHuuCo.jpg"; break;
                        case 2: imageName = "RacVoCo.jpg"; break;
                        case 3: imageName = "RacTaiChe.jpg"; break;
                        case 4: imageName = "RacNguyHai.jpg"; break;
                        case 5: imageName = "RacKhac.jpg"; break;
                    }
                    
                    try {
                        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Image/" + imageName));
                        Image originalImage = originalIcon.getImage();
                        
                        // Tạo BufferedImage mới với chất lượng cao
                        BufferedImage resizedImage = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2d = resizedImage.createGraphics();
                        
                        // Thiết lập các thuộc tính để render hình ảnh chất lượng cao
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        // Vẽ hình ảnh với chất lượng cao
                        g2d.drawImage(originalImage, 0, 0, 400, 300, null);
                        g2d.dispose();
                        
                        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
                        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        contentPanel.add(imageLabel);
                        contentPanel.add(Box.createVerticalStrut(20));
                    } catch (Exception ex) {
                        System.err.println("Không thể tải hình ảnh: " + imageName);
                    }
                    
                    // Hiển thị mô tả
                    JLabel descLabel = new JLabel("Mô tả:");
                    descLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentPanel.add(descLabel);
                    
                    JTextArea descArea = new JTextArea(rs.getString("MoTa"));
                    descArea.setWrapStyleWord(true);
                    descArea.setLineWrap(true);
                    descArea.setOpaque(false);
                    descArea.setEditable(false);
                    descArea.setFont(new Font("Arial", Font.PLAIN, 14));
                    descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentPanel.add(descArea);
                    contentPanel.add(Box.createVerticalStrut(20));
                    
                    // Hiển thị hướng dẫn phân loại
                    JLabel guideLabel = new JLabel("Hướng dẫn phân loại:");
                    guideLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    guideLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentPanel.add(guideLabel);
                    
                    JTextArea guideArea = new JTextArea(rs.getString("HdPhanLoai"));
                    guideArea.setWrapStyleWord(true);
                    guideArea.setLineWrap(true);
                    guideArea.setOpaque(false);
                    guideArea.setEditable(false);
                    guideArea.setFont(new Font("Arial", Font.PLAIN, 14));
                    guideArea.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentPanel.add(guideArea);
                    
                    // Thêm contentPanel vào detailPanel
                    detailPanel.add(contentPanel);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tải thông tin phân loại rác: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Cài đặt", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Panel chứa form đổi mật khẩu
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        // Mật khẩu cũ
        JLabel oldPassLabel = new JLabel("Mật khẩu cũ:");
        JPasswordField oldPassField = new JPasswordField();
        formPanel.add(oldPassLabel);
        formPanel.add(oldPassField);

        // Mật khẩu mới
        JLabel newPassLabel = new JLabel("Mật khẩu mới:");
        JPasswordField newPassField = new JPasswordField();
        formPanel.add(newPassLabel);
        formPanel.add(newPassField);

        // Xác nhận mật khẩu mới
        JLabel confirmPassLabel = new JLabel("Xác nhận mật khẩu mới:");
        JPasswordField confirmPassField = new JPasswordField();
        formPanel.add(confirmPassLabel);
        formPanel.add(confirmPassField);

        // Nút đổi mật khẩu
        JButton changePassButton = new JButton("Đổi mật khẩu");
        changePassButton.setBackground(new Color(46, 204, 113));
        changePassButton.setForeground(Color.WHITE);
        
        // Thêm sự kiện cho nút đổi mật khẩu
        changePassButton.addActionListener(e -> {
            String oldPass = new String(oldPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            // Kiểm tra các trường không được để trống
            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ thông tin!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kiểm tra mật khẩu mới và xác nhận mật khẩu
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this,
                    "Mật khẩu mới và xác nhận mật khẩu không khớp!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = ConnectionJDBC.getConnection();
                // Kiểm tra mật khẩu cũ
                String checkSql = "SELECT * FROM TaiKhoan WHERE MaTaiKhoan = ? AND MatKhau = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, maKhachHang);
                    checkStmt.setString(2, oldPass);
                    ResultSet rs = checkStmt.executeQuery();

                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this,
                            "Mật khẩu cũ không đúng!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Cập nhật mật khẩu mới
                    String updateSql = "UPDATE TaiKhoan SET MatKhau = ? WHERE MaTaiKhoan = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, newPass);
                        updateStmt.setString(2, maKhachHang);
                        int result = updateStmt.executeUpdate();

                        if (result > 0) {
                            JOptionPane.showMessageDialog(this,
                                "Đổi mật khẩu thành công!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                            // Clear các trường
                            oldPassField.setText("");
                            newPassField.setText("");
                            confirmPassField.setText("");
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi đổi mật khẩu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Panel cho nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(changePassButton);
        formPanel.add(new JLabel()); // Placeholder
        formPanel.add(buttonPanel);

        // Thêm form vào panel chính
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDangXuatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Bạn có chắc chắn muốn đăng xuất?", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton logoutButton = new JButton("Xác nhận đăng xuất");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            dispose();
            new GiaoDienDangNhap().setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(logoutButton);

        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChuThaiUI("", "").setVisible(true);
        });
    }
} 