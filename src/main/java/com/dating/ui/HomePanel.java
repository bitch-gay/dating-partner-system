package com.dating.ui;

import com.dating.dao.ActivityDao;
import com.dating.model.Activity;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomePanel extends JPanel {
    private MainFrame parent;
    private DefaultListModel<String> listModel;
    private JList<String> activityList;

    public HomePanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));

        // 顶部：标题 + 用户信息
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("🏠 搭子广场 (世界线)", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 20));
        topPanel.add(title, BorderLayout.CENTER);

        JLabel userLabel = new JLabel("👤 已登录");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        userLabel.setForeground(new Color(100, 100, 100));
        topPanel.add(userLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 中间：活动列表
        listModel = new DefaultListModel<>();
        activityList = new JList<>(listModel);
        activityList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        add(new JScrollPane(activityList), BorderLayout.CENTER);

        // 底部：操作按钮
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton refreshBtn = new JButton("🔄 刷新列表");
        JButton publishBtn = new JButton("📢 发布搭子");
        JButton matchBtn = new JButton("🤝 匹配推荐");

        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        publishBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        matchBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));

        btnPanel.add(refreshBtn);
        btnPanel.add(publishBtn);
        btnPanel.add(matchBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // 事件
        refreshBtn.addActionListener(e -> refreshList());
        publishBtn.addActionListener(e -> parent.switchTo("publish"));
        matchBtn.addActionListener(e -> parent.switchTo("match"));

        refreshList();
    }

    public void refreshList() {
        listModel.clear();
        ActivityDao dao = new ActivityDao();
        List<Activity> list = dao.findAllAvailable();

        if (list.isEmpty()) {
            listModel.addElement("📭 当前没有进行中的搭子活动");
            listModel.addElement("💡 点击下方「发布搭子」发起一个吧！");
            return;
        }

        for (Activity a : list) {
            String status = a.getCurrentPeople() >= a.getMaxPeople() ? "🔥已满" : "✅可报名";
            listModel.addElement(String.format("%s | %s | %d/%d人 %s",
                    a.getTitle(), a.getLocationDesc(),
                    a.getCurrentPeople(), a.getMaxPeople(), status));
        }
    }
}