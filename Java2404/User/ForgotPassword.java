package Java2404.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import Java2404.LoginandSignup.LoginForm;
import Java2404.LoginandSignup.TestConnection;

// Thêm các import cần thiết cho JavaMail
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassword extends JFrame {
    private JTextField emailField;
    private JTextField codeField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton togglePasswordButton;
    private String generatedCode;
    private LoginForm loginForm;

    public ForgotPassword(LoginForm loginForm) {
        this.loginForm = loginForm;
        setTitle("Quên Mật Khẩu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);

        // Main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Quên Mật Khẩu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 51, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Email field
        emailField = new JTextField("Nhập email của bạn");
        emailField.setForeground(Color.GRAY);
        emailField.setPreferredSize(new Dimension(300, 40));
        emailField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        emailField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Nhập email của bạn")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Nhập email của bạn");
                    emailField.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        // Send code button
        JButton sendCodeButton = new JButton("Gửi mã xác nhận");
        sendCodeButton.setBackground(new Color(255, 51, 51));
        sendCodeButton.setForeground(Color.WHITE);
        sendCodeButton.setPreferredSize(new Dimension(150, 40));
        sendCodeButton.setFocusPainted(false);
        sendCodeButton.setBorder(BorderFactory.createLineBorder(new Color(255, 51, 51), 2, true));
        sendCodeButton.addActionListener(e -> sendVerificationCode());
        gbc.gridy = 2;
        formPanel.add(sendCodeButton, gbc);

        // Verification code field
        codeField = new JTextField("Nhập mã xác nhận");
        codeField.setForeground(Color.GRAY);
        codeField.setPreferredSize(new Dimension(300, 40));
        codeField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        codeField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (codeField.getText().equals("Nhập mã xác nhận")) {
                    codeField.setText("");
                    codeField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (codeField.getText().isEmpty()) {
                    codeField.setText("Nhập mã xác nhận");
                    codeField.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 3;
        formPanel.add(codeField, gbc);

        // New password field
        JPanel passwordContainer = new JPanel(new BorderLayout(0, 0));
        passwordContainer.setOpaque(false);
        passwordContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        passwordContainer.setPreferredSize(new Dimension(300, 40));

        newPasswordField = new JPasswordField("Mật khẩu mới");
        newPasswordField.setEchoChar((char) 0);
        newPasswordField.setForeground(Color.GRAY);
        newPasswordField.setBorder(null);
        newPasswordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(newPasswordField.getPassword()).equals("Mật khẩu mới")) {
                    newPasswordField.setText("");
                    newPasswordField.setEchoChar('•');
                    newPasswordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(newPasswordField.getPassword()).isEmpty()) {
                    newPasswordField.setText("Mật khẩu mới");
                    newPasswordField.setEchoChar((char) 0);
                    newPasswordField.setForeground(Color.GRAY);
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
        togglePasswordButton.addActionListener(e -> {
            if (newPasswordField.getEchoChar() != 0) {
                newPasswordField.setEchoChar((char) 0);
            } else {
                newPasswordField.setEchoChar('•');
            }
        });

        passwordContainer.add(newPasswordField, BorderLayout.CENTER);
        passwordContainer.add(togglePasswordButton, BorderLayout.EAST);
        gbc.gridy = 4;
        formPanel.add(passwordContainer, gbc);

        // Confirm new password field
        confirmPasswordField = new JPasswordField("Xác nhận mật khẩu mới");
        confirmPasswordField.setEchoChar((char) 0);
        confirmPasswordField.setForeground(Color.GRAY);
        confirmPasswordField.setPreferredSize(new Dimension(300, 40));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        confirmPasswordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(confirmPasswordField.getPassword()).equals("Xác nhận mật khẩu mới")) {
                    confirmPasswordField.setText("");
                    confirmPasswordField.setEchoChar('•');
                    confirmPasswordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(confirmPasswordField.getPassword()).isEmpty()) {
                    confirmPasswordField.setText("Xác nhận mật khẩu mới");
                    confirmPasswordField.setEchoChar((char) 0);
                    confirmPasswordField.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy = 5;
        formPanel.add(confirmPasswordField, gbc);

        // Reset password button
        JButton resetButton = new JButton("Đặt lại mật khẩu");
        resetButton.setBackground(new Color(255, 51, 51));
        resetButton.setForeground(Color.WHITE);
        resetButton.setPreferredSize(new Dimension(150, 40));
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createLineBorder(new Color(255, 51, 51), 2, true));
        resetButton.addActionListener(e -> resetPassword());
        gbc.gridy = 6;
        formPanel.add(resetButton, gbc);

        // Back to login button
        JButton backButton = new JButton("Quay lại đăng nhập");
        backButton.setBackground(new Color(255, 51, 51));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(255, 51, 51), 2, true));
        backButton.addActionListener(e -> {
            loginForm.setVisible(true);
            ForgotPassword.this.dispose();
        });
        gbc.gridy = 7;
        formPanel.add(backButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void sendVerificationCode() {
        String email = emailField.getText();
        if (email.isEmpty() || email.equals("Nhập email của bạn")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email!");
            return;
        }

        // Check if email exists in database
        try (Connection conn = TestConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Email không tồn tại!");
                return;
            }

            // Generate 6-digit code
            Random random = new Random();
            generatedCode = String.format("%06d", random.nextInt(999999));

            // Send email with the code
            try {
                sendEmail(email, generatedCode);
                JOptionPane.showMessageDialog(this, 
                    "Mã xác nhận đã được gửi đến " + email + ". Vui lòng kiểm tra email của bạn!",
                    "Mã Xác Nhận", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (MessagingException e) {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi gửi email: " + e.getMessage(),
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void sendEmail(String recipientEmail, String code) throws MessagingException {
        // Cấu hình thông tin email gửi
        final String senderEmail = "your-email@gmail.com"; // Thay bằng email của bạn
        final String senderPassword = "your-app-password"; // Thay bằng App Password (nếu dùng Gmail)

        // Cấu hình properties cho JavaMail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Tạo session với thông tin xác thực
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        // Tạo email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Mã Xác Nhận Đặt Lại Mật Khẩu");
        message.setText("Chào bạn,\n\nMã xác nhận của bạn là: " + code + "\n\nVui lòng sử dụng mã này để đặt lại mật khẩu.\n\nTrân trọng,\nỨng dụng của bạn");

        // Gửi email
        Transport.send(message);
    }

    private void resetPassword() {
        String email = emailField.getText();
        String code = codeField.getText();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validation checks
        if (email.isEmpty() || email.equals("Nhập email của bạn")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email!");
            return;
        }
        if (code.isEmpty() || code.equals("Nhập mã xác nhận")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã xác nhận!");
            return;
        }
        if (newPassword.isEmpty() || newPassword.equals("Mật khẩu mới")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!");
            return;
        }
        if (confirmPassword.isEmpty() || confirmPassword.equals("Xác nhận mật khẩu mới")) {
            JOptionPane.showMessageDialog(this, "Vui lòng xác nhận mật khẩu mới!");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không khớp!");
            return;
        }
        if (!code.equals(generatedCode)) {
            JOptionPane.showMessageDialog(this, "Mã xác nhận không đúng!");
            return;
        }

        // Update password in database
        try (Connection conn = TestConnection.getConnection()) {
            String sql = "UPDATE users SET password = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thành công!");
                loginForm.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thất bại!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
}