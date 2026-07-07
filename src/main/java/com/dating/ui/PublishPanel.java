package com.dating.ui;

import com.dating.dao.ActivityDao;
import com.dating.model.Activity;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class PublishPanel extends JPanel {
    private MainFrame parent;
    private JTextField titleField, locationField, maxField;
    private JComboBox<String> typeCombo, genderPrefCombo;

    public PublishPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        int row=0;

        JLabel title = new JLabel("📢 发布新搭子活动", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 20));
        gbc.gridx=0; gbc.gridy=row; gbc.gridwidth=2; add(title, gbc);

        row++; gbc.gridwidth=1; gbc.gridx=0; add(new JLabel("标题:"), gbc);
        titleField = new JTextField(15); gbc.gridx=1; add(titleField, gbc);

        row++; gbc.gridx=0; add(new JLabel("类型:"), gbc);
        typeCombo = new JComboBox<>(new String[]{"运动", "电影", "读书", "饭局", "桌游"});
        gbc.gridx=1; add(typeCombo, gbc);

        row++; gbc.gridx=0; add(new JLabel("性别倾向:"), gbc);
        genderPrefCombo = new JComboBox<>(new String[]{"不限", "男", "女", "LGBTQ+友好"});
        gbc.gridx=1; add(genderPrefCombo, gbc);

        row++; gbc.gridx=0; add(new JLabel("地点:"), gbc);
        locationField = new JTextField(15); gbc.gridx=1; add(locationField, gbc);

        row++; gbc.gridx=0; add(new JLabel("人数上限:"), gbc);
        maxField = new JTextField(15); gbc.gridx=1; add(maxField, gbc);

        row++; gbc.gridx=0; gbc.gridwidth=2;
        JButton publishBtn = new JButton("立即发布");
        add(publishBtn, gbc);

        publishBtn.addActionListener(e -> publish());
    }

    private void publish() {
        if (parent.getCurrentUserId() == null) {
            JOptionPane.showMessageDialog(this, "请先登录");
            return;
        }
        String title = titleField.getText().trim();
        String location = locationField.getText().trim();
        String maxText = maxField.getText().trim();
        if (title.isEmpty() || location.isEmpty() || maxText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息");
            return;
        }
        int max = Integer.parseInt(maxText);
        String type = (String) typeCombo.getSelectedItem();
        String pref = (String) genderPrefCombo.getSelectedItem();

        Activity act = new Activity(parent.getCurrentUserId(), title, type, pref, location, LocalDateTime.now().plusDays(2), max);
        ActivityDao dao = new ActivityDao();
        int result = dao.publish(act);
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "✅ 发布成功！");
            parent.switchTo("home");
        } else {
            JOptionPane.showMessageDialog(this, "❌ 发布失败");
        }
    }
}