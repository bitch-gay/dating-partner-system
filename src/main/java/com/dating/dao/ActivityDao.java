package com.dating.dao;

import com.dating.model.Activity;
import com.dating.util.DBUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityDao {

    // 1. 发布搭子活动
    public int publish(Activity activity) {
        String sql = "INSERT INTO activities (publisher_id, title, type, gender_preference, " +
                "location_desc, activity_time, max_people, current_people, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        return DBUtil.executeUpdate(sql,
                activity.getPublisherId(),
                activity.getTitle(),
                activity.getType(),
                activity.getGenderPreference(),
                activity.getLocationDesc(),
                activity.getActivityTime(),
                activity.getMaxPeople(),
                activity.getCurrentPeople(),
                activity.getStatus()
        );
    }

    // 2. 查询所有 "进行中" 且 "未满员" 的活动 (首页展示)
    public List<Activity> findAllAvailable() {
        String sql = "SELECT * FROM activities WHERE status = 0 AND current_people < max_people " +
                "ORDER BY created_at DESC";
        return DBUtil.executeQuery(sql, rs -> {
            List<Activity> list = new ArrayList<>();
            try {
                while (rs.next()) {
                    list.add(mapRowToActivity(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        });
    }

    // 3. 根据发布者ID查询 (我的活动)
    public List<Activity> findByPublisherId(int publisherId) {
        String sql = "SELECT * FROM activities WHERE publisher_id = ? ORDER BY created_at DESC";
        return DBUtil.executeQuery(sql, rs -> {
            List<Activity> list = new ArrayList<>();
            try {
                while (rs.next()) {
                    list.add(mapRowToActivity(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }, publisherId);
    }

    // 4. 根据活动ID查询详情
    public Activity findById(int id) {
        String sql = "SELECT * FROM activities WHERE id = ?";
        return DBUtil.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    return mapRowToActivity(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, id);
    }

    // 5. 修改活动信息 (标题、时间、地点、人数等)
    public int updateActivity(Activity activity) {
        String sql = "UPDATE activities SET title = ?, type = ?, gender_preference = ?, " +
                "location_desc = ?, activity_time = ?, max_people = ? WHERE id = ?";
        return DBUtil.executeUpdate(sql,
                activity.getTitle(),
                activity.getType(),
                activity.getGenderPreference(),
                activity.getLocationDesc(),
                activity.getActivityTime(),
                activity.getMaxPeople(),
                activity.getId()
        );
    }

    // 6. 报名人数+1 (用于报名功能，防止超卖)
    public int incrementCurrentPeople(int activityId) {
        String sql = "UPDATE activities SET current_people = current_people + 1 " +
                "WHERE id = ? AND current_people < max_people";
        return DBUtil.executeUpdate(sql, activityId);
    }

    // 私有方法：ResultSet 转 Activity
    private Activity mapRowToActivity(ResultSet rs) throws SQLException {
        Activity activity = new Activity();
        activity.setId(rs.getInt("id"));
        activity.setPublisherId(rs.getInt("publisher_id"));
        activity.setTitle(rs.getString("title"));
        activity.setType(rs.getString("type"));
        activity.setGenderPreference(rs.getString("gender_preference"));
        activity.setLocationDesc(rs.getString("location_desc"));
        activity.setActivityTime(rs.getTimestamp("activity_time").toLocalDateTime());
        activity.setMaxPeople(rs.getInt("max_people"));
        activity.setCurrentPeople(rs.getInt("current_people"));
        activity.setStatus(rs.getInt("status"));
        activity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return activity;
    }
}