package com.dating.ui;

import com.dating.dao.BlindQuestionDao;
import com.dating.dao.MatchDao;
import com.dating.model.MatchRecord;
import com.dating.service.BlindQuestionService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BlindDateDialog extends JDialog {
    private int matchId;
    private BlindQuestionService bqService = new BlindQuestionService();
    private BlindQuestionDao bqDao = new BlindQuestionDao();
    private MatchDao matchDao = new MatchDao();
    private int currentQuestionIndex = 0;
    private List<String> questions;
    private JTextArea questionArea;
    private JTextField answerField;
    private JLabel progressLabel;

    public BlindDateDialog(MainFrame parent, int userIdA, int userIdB) {
        setTitle("🔒 双盲破冰阶段 (完成3题互答解锁)");
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setModal(true);
        setLayout(new BorderLayout(10, 10));

        // 1. 创建匹配记录
        MatchRecord record = new MatchRecord(userIdA, userIdB, 0.75); // 模拟分
        matchDao.createMatch(record);
        // 获取刚生成的matchId (实际应返回主键，演示简化：查询最新)
        List<MatchRecord> records = matchDao.findByUserId(userIdA);
        this.matchId = records.isEmpty() ? 1 : records.get(0).getId();

        // 2. 生成3道题
        questions = bqService.generateQuestions(matchId);

        // 3. UI组件
        JPanel topPanel = new JPanel();
        progressLabel = new JLabel("进度: 0 / 6 次回答");
        topPanel.add(progressLabel);
        add(topPanel, BorderLayout.NORTH);

        questionArea = new JTextArea(5, 30);
        questionArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        add(new JScrollPane(questionArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        answerField = new JTextField();
        JButton submitBtn = new JButton("提交回答");
        bottomPanel.add(answerField, BorderLayout.CENTER);
        bottomPanel.add(submitBtn, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // 显示第一题
        showNextQuestion();

        submitBtn.addActionListener(e -> submitAnswer(userIdA));
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            questionArea.setText("第 " + (currentQuestionIndex + 1) + " 题:\n" + questions.get(currentQuestionIndex));
        } else {
            questionArea.setText("✅ 你已完成所有题目！等待对方回答后自动解锁。");
            answerField.setEnabled(false);
        }
        // 更新进度
        int count = bqDao.countByMatchId(matchId);
        progressLabel.setText("进度: " + count + " / 6 次回答");
    }

    private void submitAnswer(int userId) {
        if (currentQuestionIndex >= questions.size()) {
            JOptionPane.showMessageDialog(this, "你已完成全部题目");
            return;
        }
        String answer = answerField.getText().trim();
        if (answer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入回答");
            return;
        }
        // 保存回答
        String question = questions.get(currentQuestionIndex);
        boolean success = bqService.answerQuestion(matchId, userId, question, answer);
        if (success) {
            currentQuestionIndex++;
            answerField.setText("");
            // 检查是否满足解锁条件
            if (bqService.isReadyToUnlock(matchId)) {
                bqService.unlockMatch(matchId);
                JOptionPane.showMessageDialog(this, "🎉 双方已完成3题互答！真实资料已解锁！");
                dispose();
                return;
            }
            showNextQuestion();
        } else {
            JOptionPane.showMessageDialog(this, "提交失败，请重试");
        }
    }
}