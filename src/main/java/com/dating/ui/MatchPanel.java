package com.dating.ui;

import com.dating.dao.MatchDao;
import com.dating.dao.UserDao;
import com.dating.model.MatchRecord;
import com.dating.model.User;
import com.dating.service.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MatchPanel extends JPanel {
    private MainFrame parent;
    private DefaultListModel<String> listModel;
    private JList<String> candidateList;
    private List<User> currentCandidates;

    public MatchPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("🤝 智能匹配推荐 (基于策略模式)", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        candidateList = new JList<>(listModel);
        candidateList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        add(new JScrollPane(candidateList), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton refreshBtn = new JButton("刷新推荐");
        JButton matchBtn = new JButton("发起匹配 (双盲破冰)");
        btnPanel.add(refreshBtn);
        btnPanel.add(matchBtn);
        add(btnPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshCandidates());
        matchBtn.addActionListener(e -> startMatch());
    }

    public void refreshCandidates() {
        listModel.clear();
        if (parent.getCurrentUserId() == null) return;
        UserDao userDao = new UserDao();
        User currentUser = userDao.findById(parent.getCurrentUserId());
        if (currentUser == null) return;

        // 获取除自己以外的所有真实用户
        List<User> allUsers = userDao.findAllExcept(parent.getCurrentUserId());
        if (allUsers.isEmpty()) {
            listModel.addElement("📭 暂无其他用户，快邀请朋友注册吧！");
            return;
        }

        // 根据当前用户性向选择策略
        MatchStrategy strategy;
        String ori = currentUser.getSexualOrientation();
        if ("同性".equals(ori)) {
            strategy = new HomoMatchStrategy();
        } else if ("双性".equals(ori) || "泛性".equals(ori)) {
            strategy = new PanMatchStrategy();
        } else {
            strategy = new HeteroMatchStrategy();
        }

        // 执行过滤和排序
        currentCandidates = strategy.filterCandidates(currentUser, allUsers);
        if (currentCandidates.isEmpty()) {
            listModel.addElement("😅 当前没有符合您性向偏好的对象");
            return;
        }

        for (User u : currentCandidates) {
            double score = strategy.calculateScore(currentUser, u);
            String genderStr = u.getGender() == 0 ? "女" : (u.getGender() == 1 ? "男" : "非二元");
            listModel.addElement(String.format("%s (%s) | %s | 匹配度: %.0f%%",
                    u.getNickname(), genderStr, u.getCity(), score * 100));
        }
    }

    private void startMatch() {
        int idx = candidateList.getSelectedIndex();
        if (idx < 0 || currentCandidates == null || idx >= currentCandidates.size()) {
            JOptionPane.showMessageDialog(this, "请先选择一个候选人");
            return;
        }
        User target = currentCandidates.get(idx);
        // 弹出双盲破冰对话框 (调用独立的Dialog)
        new BlindDateDialog(parent, parent.getCurrentUserId(), target.getId()).setVisible(true);
    }
}