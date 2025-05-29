package Java2404.Admin;

import javax.swing.*;
import java.awt.*;
import Java2404.LoginandSignup.LoginForm;

public class AdminInterface extends JFrame {
    public AdminInterface() {
        setTitle("Giao diện Quản trị viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Tạo panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Tiêu đề
        JLabel titleLabel = new JLabel("Chào mừng đến với Giao diện Quản trị viên", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 51, 51));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Nội dung
        JLabel contentLabel = new JLabel("Đây là khu vực	không gian làm việc của quản trị viên.", SwingConstants.CENTER);
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        mainPanel.add(contentLabel, BorderLayout.CENTER);

        // Nút Đăng xuất
        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setBackground(new Color(255, 51, 51));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(logoutButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
