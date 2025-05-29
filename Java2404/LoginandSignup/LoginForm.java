package Java2404.LoginandSignup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Java2404.User.ForgotPassword;
import Java2404.User.UserInterface;
import Java2404.Admin.AdminInterface;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton togglePasswordButton;
    private JPanel successPanel;
    private JLabel successIconLabel;
    private JLabel successTextLabel;

    public LoginForm() {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setMinimumSize(new Dimension(800, 400));
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

        // Tạo panel chứa thông báo thành công (ban đầu ẩn)
        successPanel = new JPanel();
        successPanel.setBackground(new Color(255, 255, 255, 255));
        successPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        successPanel.setVisible(false);
        successPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Label cho hình ảnh dấu tích
        successIconLabel = new JLabel();
        try {
            ImageIcon checkIcon = new ImageIcon("Java2404/image/success.png");
            Image img = checkIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            successIconLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            successIconLabel.setText("✔");
        }

        // Label cho văn bản
        successTextLabel = new JLabel("Đăng nhập thành công!");
        successTextLabel.setFont(new Font("Arial", Font.BOLD, 18));
        successTextLabel.setForeground(Color.BLACK);

        successPanel.add(successIconLabel);
        successPanel.add(successTextLabel);

        mainPanel.add(successPanel, BorderLayout.NORTH);

        // Panel chia đôi
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setOpaque(false);

        // Panel bên trái cho form đăng nhập
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề "Đăng nhập"
        JLabel titleLabel = new JLabel("Đăng nhập", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 51, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
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

        // Văn bản "hoặc đăng nhập bằng:"
        JLabel orLabel = new JLabel("hoặc đăng nhập bằng:", SwingConstants.CENTER);
        orLabel.setForeground(Color.GRAY);
        gbc.gridy = 2;
        formPanel.add(orLabel, gbc);

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
        gbc.gridy = 3;
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
        gbc.gridy = 4;
        formPanel.add(passwordContainer, gbc);

        // Hộp kiểm "Nhớ mật khẩu"
        JCheckBox rememberCheckBox = new JCheckBox("Nhớ mật khẩu");
        rememberCheckBox.setForeground(new Color(255, 51, 51));
        rememberCheckBox.setOpaque(false);
        rememberCheckBox.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridy = 5;
        formPanel.add(rememberCheckBox, gbc);

        // Nút "Đăng nhập" và "Đăng ký"
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton signInButton = new JButton("Đăng nhập");
        signInButton.setBackground(new Color(255, 51, 51));
        signInButton.setForeground(Color.WHITE);
        signInButton.setPreferredSize(new Dimension(150, 40));
        signInButton.setFocusPainted(false);
        signInButton.setBorder(BorderFactory.createLineBorder(new Color(255, 51, 51), 2, true));

        JButton signUpButton = new JButton("Đăng ký");
        signUpButton.setBackground(new Color(255, 51, 51));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setPreferredSize(new Dimension(150, 40));
        signInButton.setFocusPainted(false);
        signUpButton.setBorder(BorderFactory.createLineBorder(new Color(255, 51, 51), 2, true));
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterForm();
            }
        });

        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);
        gbc.gridy = 6;
        formPanel.add(buttonPanel, gbc);

        // Trong class LoginForm, tìm đoạn code của forgotPasswordLabel và sửa lại
        JLabel forgotPasswordLabel = new JLabel("Quên mật khẩu?", SwingConstants.CENTER);
        forgotPasswordLabel.setForeground(Color.GRAY);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ẩn form đăng nhập hiện tại
                LoginForm.this.setVisible(false);
                // Mở form ForgotPassword
                ForgotPassword forgotPasswordForm = new ForgotPassword(LoginForm.this);
                forgotPasswordForm.setVisible(true);
            }
        });
        gbc.gridy = 7;
        formPanel.add(forgotPasswordLabel, gbc);

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

        // ActionListener cho nút Đăng nhập
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Kiểm tra các trường không rỗng
                if (email.isEmpty() || email.equals("Email")) {
                    JOptionPane.showMessageDialog(LoginForm.this, "Vui lòng nhập email!");
                    return;
                }
                if (password.isEmpty() || password.equals("Mật khẩu")) {
                    JOptionPane.showMessageDialog(LoginForm.this, "Vui lòng nhập mật khẩu!");
                    return;
                }

                // Sử dụng SwingWorker để xử lý đăng nhập
                SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        return validateLogin(email, password);
                    }

                    @Override
                    protected void done() {
                        try {
                            String role = get();
                            if (role != null) {
                                // Hiển thị thông báo thành công
                                successPanel.setVisible(true);
                                successPanel.revalidate();
                                successPanel.repaint();

                                // Tạo Timer để chuyển hướng sau 1 giây
                                Timer timer = new Timer(1000, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        // Chuyển hướng dựa trên role
                                        if (role.equals("user")) {
                                            new UserInterface().setVisible(true);
                                        } else if (role.equals("admin")) {
                                            new AdminInterface().setVisible(true);
                                        }
                                        LoginForm.this.dispose(); // Đóng form đăng nhập
                                    }
                                });
                                timer.setRepeats(false); // Chỉ chạy một lần
                                timer.start();
                            } else {
                                JOptionPane.showMessageDialog(LoginForm.this, "Đăng nhập thất bại! Email hoặc mật khẩu không đúng.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(LoginForm.this, "Lỗi trong quá trình đăng nhập: " + ex.getMessage());
                        }
                    }
                };
                worker.execute();
            }
        });
    }

    private String validateLogin(String email, String password) {
        try (Connection conn = TestConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Không thể kết nối đến cơ sở dữ liệu!");
            }
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi đăng nhập: " + e.getMessage());
        }
    }

    public void showRegisterForm() {
        new RegisterForm(this).setVisible(true);
        this.setVisible(false);
    }
}