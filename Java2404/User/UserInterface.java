package Java2404.User;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import Java2404.LoginandSignup.LoginForm;

public class UserInterface extends JFrame {
    private JPanel mainPanel;
    private JPanel categoryPanel;
    private JPanel foodPanel;
    private JPanel cartPanel;
    private Map<String, Map<String, Object>> cartItems;
    private double totalPrice;
    private JLabel totalLabel;

    public UserInterface() {
        setTitle("NT Restaurant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header panel to hold title and categories
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        // Title panel with logo and restaurant name
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);

        // Add logo
        JLabel logoLabel = new JLabel();
        try {
            File logoFile = new File("Java2404/image/logo.jpg");
            if (logoFile.exists()) {
                ImageIcon logoIcon = new ImageIcon(logoFile.getPath());
                Image logoImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(logoImage));
            } else {
                logoLabel.setText("Logo Not Found");
            }
        } catch (Exception e) {
            logoLabel.setText("Error Loading Logo");
            e.printStackTrace();
        }
        titlePanel.add(logoLabel);

        // Add restaurant name
        JLabel titleLabel = new JLabel("NT Restaurant", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 51, 51));
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.NORTH);

        // Category panel
        categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.setBackground(Color.WHITE);
        String firstCategory = loadCategories();
        headerPanel.add(categoryPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Food panel with flexible GridLayout
        foodPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        foodPanel.setBackground(Color.WHITE);
        loadFoods(firstCategory);
        JScrollPane scrollPane = new JScrollPane(foodPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Right panel (cart and checkout)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(450, 0));

        JLabel cartLabel = new JLabel("Các món được thêm vào", SwingConstants.CENTER);
        cartLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rightPanel.add(cartLabel, BorderLayout.NORTH);

        cartItems = new HashMap<>();
        totalPrice = 0.0;
        cartPanel = new JPanel(new GridBagLayout());
        cartPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel imageHeader = new JLabel("Hình ảnh", SwingConstants.CENTER);
        imageHeader.setPreferredSize(new Dimension(60, 30));
        JLabel nameHeader = new JLabel("Tên món", SwingConstants.CENTER);
        nameHeader.setPreferredSize(new Dimension(100, 30));
        JLabel priceHeader = new JLabel("Giá", SwingConstants.CENTER);
        priceHeader.setPreferredSize(new Dimension(60, 30));
        JLabel quantityHeader = new JLabel("Số lượng", SwingConstants.CENTER);
        quantityHeader.setPreferredSize(new Dimension(60, 30));
        JLabel actionHeader = new JLabel("", SwingConstants.CENTER);
        actionHeader.setPreferredSize(new Dimension(60, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        cartPanel.add(imageHeader, gbc);
        gbc.gridx = 1;
        cartPanel.add(nameHeader, gbc);
        gbc.gridx = 2;
        cartPanel.add(priceHeader, gbc);
        gbc.gridx = 3;
        cartPanel.add(quantityHeader, gbc);
        gbc.gridx = 4;
        cartPanel.add(actionHeader, gbc);

        JScrollPane cartScrollPane = new JScrollPane(cartPanel);
        cartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        cartScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rightPanel.add(cartScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Tổng: 0.0 VNĐ", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(totalLabel, BorderLayout.NORTH);

        RoundedButton checkoutButton = new RoundedButton("Thanh toán", 20);
        checkoutButton.setBackground(new Color(255, 51, 51));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setPreferredSize(new Dimension(250, 40));
        checkoutButton.addActionListener(e -> {
            if (cartItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            } else {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công! Tổng: " + totalPrice + " VNĐ");
                cartItems.clear();
                totalPrice = 0.0;
                totalLabel.setText("Tổng: 0.0 VNĐ");
                updateCartPanel();
            }
        });
        bottomPanel.add(checkoutButton, BorderLayout.SOUTH);

        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        RoundedButton logoutButton = new RoundedButton("Đăng xuất", 20);
        logoutButton.setBackground(new Color(255, 51, 51));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(logoutButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        updateCartPanel();
    }

    // Lớp RoundedButton để bo góc
    class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setOpaque(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Không vẽ viền
        }
    }

    private String loadCategories() {
        String firstCategory = null;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "12345");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Categories")) {

            while (rs.next()) {
                String categoryName = rs.getString("name");
                if (firstCategory == null) {
                    firstCategory = categoryName;
                }
                RoundedButton categoryButton = new RoundedButton(categoryName, 15);
                categoryButton.setBackground(new Color(0, 102, 204));
                categoryButton.setForeground(Color.WHITE);
                categoryButton.addActionListener(e -> loadFoods(categoryName));
                categoryPanel.add(categoryButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh mục: " + e.getMessage());
        }
        return firstCategory;
    }

    private void loadFoods(String categoryName) {
        foodPanel.removeAll();
        String query = "SELECT f.*, c.name as category_name FROM Foods f JOIN Categories c ON f.category_id = c.id";
        if (categoryName != null) {
            query += " WHERE c.name = ?";
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "12345");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (categoryName != null) {
                pstmt.setString(1, categoryName);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String imageUrl = rs.getString("image_url");
                String description = rs.getString("description");

                JPanel foodItemPanel = new JPanel(new BorderLayout(0, 5));
                foodItemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                foodItemPanel.setBackground(Color.WHITE);
                foodItemPanel.setPreferredSize(new Dimension(300, 300));

                // Image panel
                JPanel imagePanel = new JPanel(new GridBagLayout());
                imagePanel.setBackground(Color.WHITE);
                JLabel imageLabel = new JLabel();
                if (imageUrl != null) {
                    try {
                        File imageFile = new File("Java2404/image/Foods/" + imageUrl);
                        if (imageFile.exists()) {
                            ImageIcon imageIcon = new ImageIcon(imageFile.getPath());
                            Image image = imageIcon.getImage().getScaledInstance(320, 150, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(image));
                        } else {
                            imageLabel.setText("Hình ảnh không tìm thấy");
                        }
                    } catch (Exception e) {
                        imageLabel.setText("Lỗi tải hình ảnh");
                        e.printStackTrace();
                    }
                } else {
                    imageLabel.setText("Không có hình ảnh");
                }
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                GridBagConstraints gbcImage = new GridBagConstraints();
                gbcImage.insets = new Insets(10, 10, 10, 10);
                imagePanel.add(imageLabel, gbcImage);
                foodItemPanel.add(imagePanel, BorderLayout.NORTH);

                JLabel infoLabel = new JLabel("<html><b><font size='5'>" + name + "</font></b><br><b>Giá: " + price + " VNĐ</b><br>Mô tả: " + description + "</html>", SwingConstants.LEFT);
                infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                foodItemPanel.add(infoLabel, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new GridBagLayout());
                buttonPanel.setBackground(Color.WHITE);
                RoundedButton addToCartButton = new RoundedButton("Thêm món ăn", 15);
                addToCartButton.setBackground(new Color(0, 153, 51));
                addToCartButton.setForeground(Color.WHITE);
                addToCartButton.addActionListener(e -> {
                    Map<String, Object> itemDetails = cartItems.getOrDefault(name, new HashMap<>());
                    int quantity = (int) itemDetails.getOrDefault("quantity", 0) + 1;
                    itemDetails.put("quantity", quantity);
                    itemDetails.put("price", price);
                    itemDetails.put("imageUrl", imageUrl);
                    cartItems.put(name, itemDetails);
                    totalPrice += price;
                    updateCartPanel();
                    JOptionPane.showMessageDialog(this, "Đã thêm " + name + " vào giỏ hàng!");
                });
                GridBagConstraints gbcButton = new GridBagConstraints();
                gbcButton.insets = new Insets(0, 10, 10, 10);
                buttonPanel.add(addToCartButton, gbcButton);
                foodItemPanel.add(buttonPanel, BorderLayout.SOUTH);

                foodPanel.add(foodItemPanel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải món ăn: " + e.getMessage());
        }
        foodPanel.revalidate();
        foodPanel.repaint();
    }

    private void updateCartPanel() {
        cartPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel imageHeader = new JLabel("Hình ảnh", SwingConstants.CENTER);
        imageHeader.setPreferredSize(new Dimension(60, 30));
        JLabel nameHeader = new JLabel("Tên món", SwingConstants.CENTER);
        nameHeader.setPreferredSize(new Dimension(100, 30));
        JLabel priceHeader = new JLabel("Giá", SwingConstants.CENTER);
        priceHeader.setPreferredSize(new Dimension(60, 30));
        JLabel quantityHeader = new JLabel("Số lượng", SwingConstants.CENTER);
        quantityHeader.setPreferredSize(new Dimension(60, 30));
        JLabel actionHeader = new JLabel("", SwingConstants.CENTER);
        actionHeader.setPreferredSize(new Dimension(60, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        cartPanel.add(imageHeader, gbc);
        gbc.gridx = 1;
        cartPanel.add(nameHeader, gbc);
        gbc.gridx = 2;
        cartPanel.add(priceHeader, gbc);
        gbc.gridx = 3;
        cartPanel.add(quantityHeader, gbc);
        gbc.gridx = 4;
        cartPanel.add(actionHeader, gbc);

        int row = 1;
        for (Map.Entry<String, Map<String, Object>> entry : cartItems.entrySet()) {
            String name = entry.getKey();
            Map<String, Object> details = entry.getValue();
            double price = (double) details.get("price");
            String imageUrl = (String) details.get("imageUrl");
            int quantity = (int) details.get("quantity");

            JLabel imageLabel = new JLabel();
            if (imageUrl != null) {
                try {
                    File imageFile = new File("Java2404/image/Foods/" + imageUrl);
                    if (imageFile.exists()) {
                        ImageIcon imageIcon = new ImageIcon(imageFile.getPath());
                        Image image = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(image));
                    } else {
                        imageLabel.setText("No Image");
                    }
                } catch (Exception e) {
                    imageLabel.setText("Error");
                }
            }
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(60, 50));
            gbc.gridx = 0;
            gbc.gridy = row;
            cartPanel.add(imageLabel, gbc);

            JLabel nameLabel = new JLabel("<html>" + name + "</html>", SwingConstants.CENTER);
            nameLabel.setPreferredSize(new Dimension(100, 50));
            gbc.gridx = 1;
            cartPanel.add(nameLabel, gbc);

            JLabel priceLabel = new JLabel(price + " VNĐ", SwingConstants.CENTER);
            priceLabel.setPreferredSize(new Dimension(60, 50));
            gbc.gridx = 2;
            cartPanel.add(priceLabel, gbc);

            JLabel quantityLabel = new JLabel(String.valueOf(quantity), SwingConstants.CENTER);
            quantityLabel.setPreferredSize(new Dimension(60, 50));
            gbc.gridx = 3;
            cartPanel.add(quantityLabel, gbc);

            JButton removeButton = new JButton();
            removeButton.setPreferredSize(new Dimension(60, 50));
            try {
                File trashFile = new File("Java2404/image/trash.jpg");
                if (trashFile.exists()) {
                    ImageIcon trashIcon = new ImageIcon(trashFile.getPath());
                    Image trashImage = trashIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    removeButton.setIcon(new ImageIcon(trashImage));
                } else {
                    removeButton.setText("X");
                }
            } catch (Exception e) {
                removeButton.setText("X");
                e.printStackTrace();
            }
            removeButton.setBorder(null);
            removeButton.setFocusPainted(false);
            removeButton.setContentAreaFilled(false);
            removeButton.setHorizontalAlignment(SwingConstants.CENTER);
            removeButton.addActionListener(e -> {
                totalPrice -= price * quantity;
                cartItems.remove(name);
                updateCartPanel();
            });
            gbc.gridx = 4;
            cartPanel.add(removeButton, gbc);

            row++;
        }

        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 5;
        cartPanel.add(new JLabel(), gbc);

        totalLabel.setText("Tổng: " + totalPrice + " VNĐ");

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserInterface().setVisible(true));
    }
}