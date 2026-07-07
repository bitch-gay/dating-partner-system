package com.dating.service;

import com.dating.dao.BlindQuestionDao;
import com.dating.dao.MatchDao;
import com.dating.model.BlindQuestion;

import java.util.ArrayList;
import java.util.List;

public class BlindQuestionService {

    private BlindQuestionDao blindQuestionDao = new BlindQuestionDao();
    private MatchDao matchDao = new MatchDao();

    // 系统题库（模拟AI出题）
    private static final String[] QUESTION_BANK = {
            "周末你更喜欢：A. 户外爬山  B. 宅家看电影",
            "对于一段关系，你更看重：A. 精神共鸣  B. 生活陪伴",
            "遇到分歧时，你通常会：A. 直接沟通  B. 冷静几天再说",
            "你更倾向的约会方式是：A. 一起做饭  B. 餐厅打卡",
            "你的理想型是：A. 阳光开朗  B. 温柔内敛"
    };

    // 1. 为用户生成3道随机题目 (返回题目列表)
    public List<String> generateQuestions(int matchId) {
        List<String> selected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int idx = (matchId + i * 7) % QUESTION_BANK.length;
            selected.add(QUESTION_BANK[idx]);
        }
        return selected;
    }

    // 2. ★核心方法：用户回答一道题 (保存到数据库)
    public boolean answerQuestion(int matchId, int fromUserId, String question, String answer) {
        // 防止空答案
        if (answer == null || answer.trim().isEmpty()) {
            return false;
        }
        BlindQuestion bq = new BlindQuestion(matchId, fromUserId, question, answer.trim());
        int result = blindQuestionDao.saveAnswer(bq);
        return result > 0;
    }

    // 3. ★核心方法：检查是否可以解锁真实资料 (完成3轮互答 = 6条记录)
    public boolean isReadyToUnlock(int matchId) {
        int count = blindQuestionDao.countByMatchId(matchId);
        // 双方各答3题 = 总共6条记录
        return count >= 6;
    }

    // 4. 执行解锁操作 (将match_records的phase从0改为1)
    public boolean unlockMatch(int matchId) {
        if (!isReadyToUnlock(matchId)) {
            return false;
        }
        int result = matchDao.updatePhase(matchId, 1);
        return result > 0;
    }

    // 5. 获取某个匹配下的所有问答记录 (用于展示)
    public List<BlindQuestion> getQuestionsByMatch(int matchId) {
        return blindQuestionDao.findByMatchId(matchId);
    }
}