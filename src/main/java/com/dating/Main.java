package com.dating;

import com.dating.dao.*;
import com.dating.model.Activity;
import com.dating.model.MatchRecord;
import com.dating.model.User;
import com.dating.service.BlindQuestionService;
import com.dating.service.HeteroMatchStrategy;
import com.dating.service.MatchStrategy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("========== 搭子恋爱匹配系统 - 完整链路测试 ==========\n");

        UserDao userDao = new UserDao();
        ActivityDao activityDao = new ActivityDao();
        MatchDao matchDao = new MatchDao();
        BlindQuestionService bqService = new BlindQuestionService();
        ActivityParticipantDao participantDao = new ActivityParticipantDao();

        // ===== 1. 注册两个测试用户 =====
        User userA = new User("13800000001", "Pass1234", "小欢", 0, "异性", 22, "广州");
        User userB = new User("13800000002", "Pass1234", "小阳", 1, "异性", 24, "广州");
        userDao.register(userA);
        userDao.register(userB);
        System.out.println("✅ 注册成功：小欢(女) 和 小阳(男)");

        // 拿到数据库生成的真实ID (假设注册后ID分别为1和2，如果已有数据请改成实际的)
        User realA = userDao.findByPhone("13800000001");
        User realB = userDao.findByPhone("13800000002");
        System.out.println("📌 用户ID: " + realA.getId() + " (小欢) , " + realB.getId() + " (小阳)");

        // ===== 2. 模拟异性匹配策略 (计算匹配分) =====
        MatchStrategy strategy = new HeteroMatchStrategy();
        List<User> candidates = Arrays.asList(realA, realB);
        List<User> matched = strategy.filterCandidates(realA, candidates);
        if (!matched.isEmpty()) {
            double score = strategy.calculateScore(realA, matched.get(0));
            System.out.printf("✅ 匹配成功！匹配分数: %.2f%%\n", score * 100);

            // ===== 3. 创建匹配记录 (进入双盲阶段) =====
            MatchRecord record = new MatchRecord(realA.getId(), realB.getId(), score);
            int matchId = matchDao.createMatch(record);
            System.out.println("✅ 匹配记录创建成功！match_id = " + matchId);

            // ===== 4. ★核心测试：双盲问答流程 =====
            System.out.println("\n===== 开始双盲问答 (3题互答) =====");

            // 生成3道题
            List<String> questions = bqService.generateQuestions(matchId);

            // 小欢 (userA) 回答3题
            System.out.println("\n📝 小欢正在回答...");
            bqService.answerQuestion(matchId, realA.getId(), questions.get(0), "A. 户外爬山");
            bqService.answerQuestion(matchId, realA.getId(), questions.get(1), "A. 精神共鸣");
            bqService.answerQuestion(matchId, realA.getId(), questions.get(2), "A. 直接沟通");
            System.out.println("   小欢答完3题 ✅");

            // 小阳 (userB) 回答3题
            System.out.println("\n📝 小阳正在回答...");
            bqService.answerQuestion(matchId, realB.getId(), questions.get(0), "B. 宅家看电影");
            bqService.answerQuestion(matchId, realB.getId(), questions.get(1), "B. 生活陪伴");
            bqService.answerQuestion(matchId, realB.getId(), questions.get(2), "B. 冷静几天再说");
            System.out.println("   小阳答完3题 ✅");

            // ===== 5. 检查是否满足解锁条件 =====
            System.out.println("\n===== 检查解锁条件 =====");
            int answerCount = new BlindQuestionDao().countByMatchId(matchId);
            System.out.println("当前总回答数: " + answerCount + " / 6");

            if (bqService.isReadyToUnlock(matchId)) {
                System.out.println("✅ 条件满足！正在解锁真实资料...");
                boolean unlocked = bqService.unlockMatch(matchId);
                if (unlocked) {
                    System.out.println("🎉🎉🎉 解锁成功！双方可以查看对方真实头像和资料了！");
                }
            } else {
                System.out.println("⏳ 还需继续作答...");
            }

            // ===== 6. 查看最终匹配状态 =====
            MatchRecord finalRecord = matchDao.findById(matchId);
            System.out.println("\n📋 匹配最终状态: phase = " + finalRecord.getPhase() + " (0=双盲中, 1=已解锁)");

        } else {
            System.out.println("❌ 没有匹配到合适的人");
        }

        System.out.println("\n========== 测试结束 ==========");
    }
}