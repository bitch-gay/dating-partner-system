package com.dating.util;

public class SimilarityUtil {

    // 基于MBTI简化的Jaccard相似度 (如果有兴趣标签表可替换)
    public static double jaccard(String mbtiA, String mbtiB) {
        if (mbtiA == null || mbtiB == null) return 0.5;
        // 简单模拟：统计相同字母个数 (E/I, S/N, T/F, J/P)
        int same = 0;
        for (int i = 0; i < Math.min(mbtiA.length(), 4); i++) {
            if (i < mbtiB.length() && mbtiA.charAt(i) == mbtiB.charAt(i)) {
                same++;
            }
        }
        return same / 4.0;
    }
}