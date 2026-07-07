package com.dating.ui;

import com.dating.dao.UserDao;
import com.dating.model.User;
import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private MainFrame parent;
    private RadarChartPanel radarChart;
    private JLabel infoLabel;

    public ProfilePanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10,10));
        JLabel title = new JLabel("👤 个人动态行为资产", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        radarChart = new RadarChartPanel(new double[]{0.8, 0.6, 0.9, 0.4, 0.7});
        add(radarChart, BorderLayout.CENTER);

        infoLabel = new JLabel(" ", SwingConstants.CENTER);
        add(infoLabel, BorderLayout.SOUTH);

        JButton refreshBtn = new JButton("刷新雷达图");
        refreshBtn.addActionListener(e -> refreshData());
        add(refreshBtn, BorderLayout.EAST);
    }

    public void refreshData() {
        if (parent.getCurrentUserId() == null) return;
        UserDao dao = new UserDao();
        User user = dao.findById(parent.getCurrentUserId());
        if (user != null) {
            infoLabel.setText("昵称: " + user.getNickname() + " | 性向: " + user.getSexualOrientation() + " | 城市: " + user.getCity());
            // 模拟动态数据 (实际从behavior表查)
            double[] data = {
                    0.5 + Math.random() * 0.5,
                    0.5 + Math.random() * 0.5,
                    0.5 + Math.random() * 0.5,
                    0.5 + Math.random() * 0.5,
                    0.5 + Math.random() * 0.5
            };
            radarChart.updateData(data);
        }
    }
}