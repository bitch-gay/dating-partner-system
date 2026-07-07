package com.dating.ui;

import com.dating.dao.UserDao;
import com.dating.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JButton loginBtn;
    private MainFrame parent;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public JButton getLoginBtn() {
        return loginBtn;
    }

    public LoginPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // 标题
        JLabel title = new JLabel("❤️ 新世代搭子恋爱匹配系统 2.0");
        title.setFont(new Font("微软雅黑", Font.BOLD, 26));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // 副标题
        row++;
        JLabel subTitle = new JLabel("⚡ 行为货币 · 性向光谱 · 双盲破冰");
        subTitle.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        subTitle.setForeground(new Color(100, 100, 100));
        gbc.gridy = row;
        add(subTitle, gbc);

        // ---------- 手机号 ----------
        row++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("📱 手机号:"), gbc);

        phoneField = new JTextField(15);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(phoneField, gbc);

        // ---------- 密码 ----------
        row++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("🔒 密码:"), gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(passwordField, gbc);

        // ---------- 状态标签 ----------
        row++;
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(200, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(statusLabel, gbc);

        // ---------- 按钮 ----------
        row++;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton loginBtn = new JButton("🚀 登录");
        JButton registerBtn = new JButton("📝 去注册");

        loginBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        registerBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        loginBtn.setBackground(new Color(66, 133, 244));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        gbc.gridy = row;
        add(btnPanel, gbc);

        // ---------- 事件绑定 ----------
        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> parent.switchTo("register"));

    }

    private void login() {
        String phone = phoneField.getText().trim();
        String pwd = new String(passwordField.getPassword());

        if (phone.isEmpty() || pwd.isEmpty()) {
            statusLabel.setText("⚠️ 请输入手机号和密码");
            return;
        }

        UserDao dao = new UserDao();
        User user = dao.login(phone, pwd);

        if (user != null) {
            parent.setCurrentUser(user.getId());
            statusLabel.setText("✅ 欢迎回来，" + user.getNickname() + "！");
            statusLabel.setForeground(new Color(50, 150, 50));
            // 延迟0.5秒跳转，让用户看到欢迎信息
            Timer timer = new Timer(500, e -> parent.switchTo("home"));
            timer.setRepeats(false);
            timer.start();
        } else {
            statusLabel.setText("❌ 账号或密码错误，请重试");
            statusLabel.setForeground(new Color(200, 50, 50));
            passwordField.setText("");
        }
    }
}