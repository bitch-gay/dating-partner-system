package com.dating.service;

import com.dating.model.User;
import java.util.List;
import java.util.stream.Collectors;

public class HeteroMatchStrategy implements MatchStrategy {
    @Override
    public List<User> filterCandidates(User currentUser, List<User> candidates) {
        // 异性策略：性别必须不同（男找女，女找男）
        return candidates.stream()
                .filter(u -> !u.getId().equals(currentUser.getId())) // 排除自己
                .filter(u -> u.getGender() != null && currentUser.getGender() != null)
                .filter(u -> !u.getGender().equals(currentUser.getGender())) // 性别不同
                .sorted((u1, u2) -> Double.compare(
                        calculateScore(currentUser, u2),
                        calculateScore(currentUser, u1)
                )) // 降序排列
                .collect(Collectors.toList());
    }
}