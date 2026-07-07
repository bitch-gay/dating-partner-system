package com.dating.service;

import com.dating.model.User;
import java.util.List;
import com.dating.util.SimilarityUtil;


/**
 * 匹配策略接口 (策略模式)
 * 开闭原则：对扩展开放(新增性向策略)，对修改关闭(不修改已有代码)
 */
public interface MatchStrategy {

    /**
     * 根据当前用户，从候选人列表中筛选出匹配对象
     * @param currentUser 当前登录用户
     * @param candidates 所有候选人列表
     * @return 筛选后的匹配列表 (按匹配分数降序)
     */
    List<User> filterCandidates(User currentUser, List<User> candidates);

    /**
     * 计算两个用户之间的匹配分数 (0~1)
     * 子类可重写以加入性向特定规则
     */
    default double calculateScore(User me, User other) {
        // 基础分：兴趣Jaccard相似度(40%) + 年龄差(30%) + 同城(30%)
        double tagSim = SimilarityUtil.jaccard(me.getMbti(), other.getMbti()); // 简化用MBTI代替标签
        double ageScore = Math.max(0, 1 - Math.abs(me.getAge() - other.getAge()) / 20.0);
        double cityScore = (me.getCity() != null && me.getCity().equals(other.getCity())) ? 1.0 : 0.3;
        return 0.4 * tagSim + 0.3 * ageScore + 0.3 * cityScore;
    }
}