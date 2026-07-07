package com.dating.service;

import com.dating.model.User;
import java.util.List;
import java.util.stream.Collectors;

public class PanMatchStrategy implements MatchStrategy {
    @Override
    public List<User> filterCandidates(User currentUser, List<User> candidates) {
        // 泛性策略：不限制性别，只看匹配分数
        return candidates.stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .sorted((u1, u2) -> Double.compare(
                        calculateScore(currentUser, u2),
                        calculateScore(currentUser, u1)
                ))
                .collect(Collectors.toList());
    }
}