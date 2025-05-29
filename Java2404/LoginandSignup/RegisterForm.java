package Java2404.LoginandSignup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterForm extends JFrame {
    private LoginForm loginForm;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton togglePasswordButton;
    private JCheckBox agreeCheckBox;

    public RegisterForm(LoginForm loginForm) {
        this.loginForm = loginForm;
        setTitle("Đăng ký");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);

        // Tạo panel chính với background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image backgroundImage = new ImageIcon("Java2404/image/background.png").getImage();
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.RED);
                    g.drawString("Hình ảnh không tải được", 10, 20);
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Panel chia đôi
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setOpaque(false);

        // Panel bên trái cho form đăng ký
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);

        // Khởi tạo và cấu hình GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL; // Điền ngang

        // Tiêu đề "Đăng ký"
        JLabel titleLabel = new JLabel("Đăng ký", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 51, 51));
        gbc.gridx = 0; // Cột bắt đầu
        gbc.gridy = 0; // Hàng bắt đầu
        gbc.gridwidth = 2; // Chiếm 2 cột
        formPanel.add(titleLabel, gbc);

        // Nút mạng xã hội
        JPanel socialPanel = new JPanel();
        socialPanel.setOpaque(false);
        JButton googleButton = new JButton("G");
        JButton facebookButton = new JButton("f");
        JButton twitterButton = new JButton("T");
        JButton linkedinButton = new JButton("in");
        JButton[] socialButtons = {googleButton, facebookButton, twitterButton, linkedinButton};
        for (JButton btn : socialButtons) {
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(Color.BLACK);
            btn.setPreferredSize(new Dimension(40, 40));
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    AbstractButton b = (AbstractButton) c;
                    g.setColor(b.getBackground());
                    g.fillOval(0, 0, b.getWidth(), b.getHeight());
                    super.paint(g, c);
                }
            });
            socialPanel.add(btn);
        }
        gbc.gridy = 1;
        formPanel.add(socialPanel, gbc);

        // Văn bản "hoặc sử dụng email để đăng ký:"
        JLabel orLabel = new JLabel("hoặc sử dụng email để đăng ký:", SwingConstants.CENTER);
        orLabel.setForeground(Color.GRAY);
        gbc.gridy = 2;
        formPanel.add(orLabel, gbc);

        // Trường Tên với placeholder
        nameField = new JTextField("Họ và tên");
        nameField.setForeground(Color.GRAY);
        nameField.setPreferredSize(new Dimension(300, 40));
        nameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Họ và tên")) {
                    nameField.setText("");
                    nameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("Họ và tên");
                    nameField.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 3;
        formPanel.add(nameField, gbc);

        // Trường Email với placeholder
        emailField = new JTextField("Email");
        emailField.setForeground(Color.GRAY);
        emailField.setPreferredSize(new Dimension(300, 40));
        emailField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        emailField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Email")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Email");
                    emailField.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 4;
        formPanel.add(emailField, gbc);

        // Trường Mật khẩu với placeholder và nút con mắt
        JPanel passwordContainer = new JPanel(new BorderLayout(0, 0));
        passwordContainer.setOpaque(false);
        passwordContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        passwordContainer.setPreferredSize(new Dimension(300, 40));

        passwordField = new JPasswordField("Mật khẩu");
        passwordField.setEchoChar((char) 0);
        passwordField.setForeground(Color.GRAY);
        passwordField.setBorder(null);
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals("Mật khẩu")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Mật khẩu");
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        togglePasswordButton = new JButton("👁️");
        togglePasswordButton.setPreferredSize(new Dimension(40, 40));
        togglePasswordButton.setFocusPainted(false);
        togglePasswordButton.setOpaque(true);
        togglePasswordButton.setBackground(Color.WHITE);
        togglePasswordButton.setBorderPainted(false);
        togglePasswordButton.setBorder(null);
        togglePasswordButton.setContentAreaFilled(true);
        togglePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordField.getEchoChar() != 0) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('•');
                }
            }
        });

        passwordContainer.add(passwordField, BorderLayout.CENTER);
        passwordContainer.add(togglePasswordButton, BorderLayout.EAST);
        gbc.gridy = 5;
        formPanel.add(passwordContainer, gbc);

        // Trường Xác nhận mật khẩu với placeholder
        confirmPasswordField = new JPasswordField("Nhập lại mật khẩu");
        confirmPasswordField.setEchoChar((char) 0);
        confirmPasswordField.setForeground(Color.GRAY);
        confirmPasswordField.setPreferredSize(new Dimension(300, 40));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        confirmPasswordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(confirmPasswordField.getPassword()).equals("Nhập lại mật khẩu")) {
                    confirmPasswordField.setText("");
                    confirmPasswordField.setEchoChar('•');
                    confirmPasswordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(confirmPasswordField.getPassword()).isEmpty()) {
                    confirmPasswordField.setText("Nhập lại mật khẩu");
                    confirmPasswordField.setEchoChar((char) 0);
                    confirmPasswordField.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 6;
        formPanel.add(confirmPasswordField, gbc);

        // Hộp kiểm "Tôi đồng ý với điều khoản và chính sách bảo mật"
        agreeCheckBox = new JCheckBox("Tôi đồng ý với điều khoản và chính sách bảo mật");
        agreeCheckBox.setForeground(new Color(255, 51, 51));
        agreeCheckBox.setOpaque(false);
        agreeCheckBox.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridy = 7;
        formPanel.add(agreeCheckBox, gbc);

        // Nút "Đăng ký" và "Đăng nhập"
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton signUpButton = new JButton("Đăng ký");
        signUpButton.setBackground(new Color(255, 51, 51));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setPreferredSize(new Dimension(150, 40));
        signUpButton.setFocusPainted(false);
        signUpButton.setBorder(BorderFactory.createLineBorder(new Color(255, 51, 51), 2, true));
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Kiểm tra mật khẩu có khớp không
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterForm.this, "Mật khẩu không khớp!");
                    return;
                }

                // Kiểm tra điều khoản
                if (!agreeCheckBox.isSelected()) {
                    JOptionPane.showMessageDialog(RegisterForm.this, "Vui lòng đồng ý với điều khoản!");
                    return;
                }

                // Kiểm tra độ dài dữ liệu
                if (name.length() > 255) {
                    JOptionPane.showMessageDialog(RegisterForm.this, "Tên quá dài (tối đa 255 ký tự)!");
                    return;
                }
                if (email.length() > 255) {
                    JOptionPane.showMessageDialog(RegisterForm.this, "Email quá dài (tối đa 255 ký tự)!");
                    return;
                }
                if (password.length() > 255) {
                    JOptionPane.showMessageDialog(RegisterForm.this, "Mật khẩu quá dài (tối đa 255 ký tự)!");
                    return;
                }

                // Kiểm tra xem các trường có rỗng không
                if (name.isEmpty() || name.equals("Họ và tên")) {
                    JOptionPane.showMessageDialog(RegisterForm.this, "Vui lòng nhập họ và tên!");
                    return;
                }
                if (email.isEmpty() || email.equals("Email")) {
                    JOptionPane.showMessageDialog(RegisterForm.this, "Vui lòng nhập email!");
                    return;
                }
                if (password.isEmpty() || password.equals("Mật khẩu")) {
                    JOptionPane.showMessageDialog(RegisterForm.this, "Vui lòng nhập mật khẩu!");
                    return;
                }

                registerUser(name, email, password);
            }
        });

        JButton signInButton = new JButton("Đăng nhập");
        signInButton.setBackground(new Color(255, 51, 51));
        signInButton.setForeground(Color.WHITE);
        signInButton.setPreferredSize(new Dimension(150, 40));
        signInButton.setFocusPainted(false);
        signInButton.setBorder(BorderFactory.createLineBorder(new Color(255, 51, 51), 2, true));
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginForm.setVisible(true);
                RegisterForm.this.dispose();
            }
        });

        buttonPanel.add(signUpButton);
        buttonPanel.add(signInButton);
        gbc.gridy = 8;
        formPanel.add(buttonPanel, gbc);

        // Panel bên phải cho hình ảnh món ăn
        JPanel imagePanel = new JPanel();
        imagePanel.setOpaque(false);
        JLabel imageLabel = new JLabel();
        try {
            ImageIcon imageIcon = new ImageIcon("Java2404/image/logo-removebg-preview.png");
            Image image = imageIcon.getImage().getScaledInstance(550, 550, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            imageLabel.setText("Hình ảnh không tải được");
            imageLabel.setForeground(Color.RED);
        }
        imagePanel.add(imageLabel);

        // Thêm các panel vào contentPanel
        contentPanel.add(formPanel);
        contentPanel.add(imagePanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void registerUser(String name, String email, String password) {
        try (Connection conn = TestConnection.getConnection()) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name); // Sử dụng name làm username
            stmt.setString(2, password);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                loginForm.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đăng ký thất bại! Không có dữ liệu nào được thêm.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đăng ký thất bại! Chi tiết lỗi: " + e.getMessage());
        }
    }
}