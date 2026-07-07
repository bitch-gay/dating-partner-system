package com.dating.dao;

import com.dating.model.MatchRecord;
import com.dating.util.DBUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchDao {

    // 1. 创建匹配记录 (两人配对时调用)
    public int createMatch(MatchRecord record) {
        String sql = "INSERT INTO match_records (user_a_id, user_b_id, match_score, phase, created_at) " +
                "VALUES (?, ?, ?, ?, NOW())";
        return DBUtil.executeUpdate(sql,
                record.getUserAId(),
                record.getUserBId(),
                record.getMatchScore(),
                record.getPhase()
        );
    }

    // 2. 根据用户ID查询所有匹配记录 (用于展示我的匹配列表)
    public List<MatchRecord> findByUserId(int userId) {
        String sql = "SELECT * FROM match_records WHERE user_a_id = ? OR user_b_id = ? ORDER BY created_at DESC";
        return DBUtil.executeQuery(sql, rs -> {
            List<MatchRecord> list = new ArrayList<>();
            try {
                while (rs.next()) {
                    list.add(mapRowToMatchRecord(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }, userId, userId);
    }

    // 3. 根据ID查询单个匹配记录 (用于双盲页面)
    public MatchRecord findById(int matchId) {
        String sql = "SELECT * FROM match_records WHERE id = ?";
        return DBUtil.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    return mapRowToMatchRecord(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, matchId);
    }

    // 4. 更新匹配阶段 (0->1 解锁)
    public int updatePhase(int matchId, int newPhase) {
        String sql = "UPDATE match_records SET phase = ? WHERE id = ?";
        return DBUtil.executeUpdate(sql, newPhase, matchId);
    }

    // 私有方法：ResultSet转对象
    private MatchRecord mapRowToMatchRecord(ResultSet rs) throws SQLException {
        MatchRecord record = new MatchRecord();
        record.setId(rs.getInt("id"));
        record.setUserAId(rs.getInt("user_a_id"));
        record.setUserBId(rs.getInt("user_b_id"));
        record.setMatchScore(rs.getDouble("match_score"));
        record.setPhase(rs.getInt("phase"));
        record.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return record;
    }
}