package com.dating.dao;

import com.dating.model.BlindQuestion;
import com.dating.util.DBUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlindQuestionDao {

    // 1. 保存一条问答记录 (用户回答时调用)
    public int saveAnswer(BlindQuestion question) {
        String sql = "INSERT INTO blind_questions (match_id, from_user_id, question_content, user_answer, created_at) " +
                "VALUES (?, ?, ?, ?, NOW())";
        return DBUtil.executeUpdate(sql,
                question.getMatchId(),
                question.getFromUserId(),
                question.getQuestionContent(),
                question.getUserAnswer()
        );
    }

    // 2. 统计某个match_id下共有多少条已回答记录 (用于判断是否完成3题互答)
    public int countByMatchId(int matchId) {
        String sql = "SELECT COUNT(*) FROM blind_questions WHERE match_id = ?";
        return DBUtil.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }, matchId);
    }

    // 3. 查询某个match_id下的所有问答 (用于展示历史记录)
    public List<BlindQuestion> findByMatchId(int matchId) {
        String sql = "SELECT * FROM blind_questions WHERE match_id = ? ORDER BY created_at ASC";
        return DBUtil.executeQuery(sql, rs -> {
            List<BlindQuestion> list = new ArrayList<>();
            try {
                while (rs.next()) {
                    BlindQuestion q = new BlindQuestion();
                    q.setId(rs.getInt("id"));
                    q.setMatchId(rs.getInt("match_id"));
                    q.setFromUserId(rs.getInt("from_user_id"));
                    q.setQuestionContent(rs.getString("question_content"));
                    q.setUserAnswer(rs.getString("user_answer"));
                    q.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(q);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }, matchId);
    }
}