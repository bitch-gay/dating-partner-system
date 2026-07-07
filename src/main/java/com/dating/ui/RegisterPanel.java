package com.dating.ui;

import com.dating.dao.UserDao;
import com.dating.model.User;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private MainFrame parent;
    private JTextField phoneField, nicknameField, ageField, cityField;
    private JPasswordField passwordField;
    private JComboBox<String> genderCombo, orientationCombo;

    public RegisterPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // 标题
        JLabel title = new JLabel("📝 注册新账号 (支持性向光谱)");
        title.setFont(new Font("微软雅黑", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // ---------- 第1行：手机号 ----------
        row++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("📱 手机号:"), gbc);

        phoneField = new JTextField(15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(phoneField, gbc);

        // ---------- 第2行：密码 ----------
        row++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("🔒 密码:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(passwordField, gbc);

        // ---------- 第3行：昵称 ----------
        row++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("👤 昵称:"), gbc);

        nicknameField = new JTextField(15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(nicknameField, gbc);

        // ---------- 第4行：年龄 ----------
        row++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("🎂 年龄:"), gbc);

        ageField = new JTextField(15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(ageField, gbc);

        // ---------- 第5行：城市 ----------
        row++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("🏙️ 城市:"), gbc);

        cityField = new JTextField(15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(cityField, gbc);

        // ---------- 第6行：性别 (支持非二元) ----------
        row++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("⚧ 性别:"), gbc);

        genderCombo = new JComboBox<>(new String[]{"女", "男", "非二元"});
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(genderCombo, gbc);

        // ---------- 第7行：性取向 (核心亮点) ----------
        row++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("❤️ 性取向:"), gbc);

        orientationCombo = new JComboBox<>(new String[]{"异性", "同性", "双性", "泛性"});
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(orientationCombo, gbc);

        // ---------- 第8行：按钮 ----------
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton registerBtn = new JButton("✅ 注册");
        JButton backBtn = new JButton("🔙 返回登录");

        // 让按钮好看一点
        registerBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        backBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        registerBtn.setBackground(new Color(66, 133, 244));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);

        btnPanel.add(registerBtn);
        btnPanel.add(backBtn);
        add(btnPanel, gbc);

        // ---------- 事件绑定 ----------
        registerBtn.addActionListener(e -> register());
        backBtn.addActionListener(e -> parent.switchTo("login"));
    }

    private void register() {
        String phone = phoneField.getText().trim();
        String pwd = new String(passwordField.getPassword());
        String nickname = nicknameField.getText().trim();
        String ageText = ageField.getText().trim();
        String city = cityField.getText().trim();

        if (phone.isEmpty() || pwd.isEmpty() || nickname.isEmpty() || ageText.isEmpty() || city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ 请填写所有必填字段");
            return;
        }

        if (phone.length() != 11) {
            JOptionPane.showMessageDialog(this, "⚠️ 手机号必须为11位");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age < 16 || age > 100) {
                JOptionPane.showMessageDialog(this, "⚠️ 年龄请填写16-100之间");
                return;
            }

            int genderIdx = genderCombo.getSelectedIndex(); // 0女 1男 2非二元
            String orientation = (String) orientationCombo.getSelectedItem();

            User user = new User(phone, pwd, nickname, genderIdx, orientation, age, city);
            UserDao dao = new UserDao();
            int result = dao.register(user);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "🎉 注册成功！请返回登录");
                parent.switchTo("login");
            } else {
                JOptionPane.showMessageDialog(this, "❌ 注册失败，手机号可能已存在");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ 年龄请输入数字");
        }
    }
}