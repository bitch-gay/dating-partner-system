package com.dating.dao;

import java.sql.SQLException;
import com.dating.util.DBUtil;

public class ActivityParticipantDao {

    // 报名搭子活动 (插入报名记录，同时更新activities表的current_people)
    public int joinActivity(int activityId, int userId) {
        // 第一步：插入报名表
        String sql1 = "INSERT INTO activity_participants (activity_id, user_id, joined_at) VALUES (?, ?, NOW())";
        int result1 = DBUtil.executeUpdate(sql1, activityId, userId);
        if (result1 <= 0) return -1;

        // 第二步：更新活动当前人数 (+1) 并检查是否满员
        String sql2 = "UPDATE activities SET current_people = current_people + 1, " +
                "status = CASE WHEN current_people + 1 >= max_people THEN 1 ELSE 0 END " +
                "WHERE id = ?";
        return DBUtil.executeUpdate(sql2, activityId);
    }

    // 检查用户是否已经报名了该活动 (防止重复报名)
    public boolean isAlreadyJoined(int activityId, int userId) {
        String sql = "SELECT COUNT(*) FROM activity_participants WHERE activity_id = ? AND user_id = ?";
        return DBUtil.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }, activityId, userId);
    }
}