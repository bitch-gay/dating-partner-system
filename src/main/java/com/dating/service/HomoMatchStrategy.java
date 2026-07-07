package com.dating.service;

import com.dating.model.User;
import java.util.List;
import java.util.stream.Collectors;

public class HomoMatchStrategy implements MatchStrategy {
    @Override
    public List<User> filterCandidates(User currentUser, List<User> candidates) {
        // 同性策略：性别必须相同
        return candidates.stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .filter(u -> u.getGender() != null && currentUser.getGender() != null)
                .filter(u -> u.getGender().equals(currentUser.getGender())) // 性别相同
                .sorted((u1, u2) -> Double.compare(
                        calculateScore(currentUser, u2),
                        calculateScore(currentUser, u1)
                ))
                .collect(Collectors.toList());
    }
}