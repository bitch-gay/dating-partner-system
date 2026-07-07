package com.dating.ui;

import com.dating.util.DBUtil;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Integer currentUserId;

    // 各页面引用
    private HomePanel homePanel;
    private ProfilePanel profilePanel;
    private MatchPanel matchPanel;
    private PublishPanel publishPanel;

    public MainFrame() {
        initLookAndFeel();
        setTitle("新世代搭子恋爱匹配系统 2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 720);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 1. 登录页
        LoginPanel loginPanel = new LoginPanel(this);
        mainPanel.add(loginPanel, "login");
        this.getRootPane().setDefaultButton(loginPanel.getLoginBtn());
        // 2. 注册页
        mainPanel.add(new RegisterPanel(this), "register");
        // 3. 首页
        homePanel = new HomePanel(this);
        mainPanel.add(homePanel, "home");
        // 4. 个人主页
        profilePanel = new ProfilePanel(this);
        mainPanel.add(profilePanel, "profile");
        // 5. 匹配页
        matchPanel = new MatchPanel(this);
        mainPanel.add(matchPanel, "match");
        // 6. 发布页
        publishPanel = new PublishPanel(this);
        mainPanel.add(publishPanel, "publish");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchTo(String pageName) {
        cardLayout.show(mainPanel, pageName);
        if ("home".equals(pageName) && homePanel != null) homePanel.refreshList();
        if ("profile".equals(pageName) && profilePanel != null) profilePanel.refreshData();
        if ("match".equals(pageName) && matchPanel != null) matchPanel.refreshCandidates();
    }

    public void setCurrentUser(int id) { this.currentUserId = id; }
    public Integer getCurrentUserId() { return currentUserId; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 容错：如果数据库连不上，打印错误但依然弹出界面（方便展示UI）
            try {
                DBUtil.getConnection();
                System.out.println("✅ 数据库连接成功");
            } catch (Exception e) {
                System.err.println("⚠️ 数据库连接失败，将以模拟模式展示界面 (请检查db.properties)");
                // 不退出，让界面能展示，方便截图
            }
            new MainFrame();
        });
    }
}