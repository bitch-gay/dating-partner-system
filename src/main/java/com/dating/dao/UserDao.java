package com.dating.dao;

import com.dating.model.User;
import com.dating.util.DBUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserDao {

    // 1. 注册：插入新用户 (返回影响行数)
    public int register(User user) {
        String sql = "INSERT INTO users (phone, password, nickname, gender, sexual_orientation, age, city, mbti, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        return DBUtil.executeUpdate(sql,
                user.getPhone(),
                user.getPassword(),
                user.getNickname(),
                user.getGender(),
                user.getSexualOrientation(),
                user.getAge(),
                user.getCity(),
                user.getMbti()
        );
    }

    // 2. 根据手机号查询用户 (用于登录校验 + 查重)
    public User findByPhone(String phone) {
        String sql = "SELECT * FROM users WHERE phone = ?";
        return DBUtil.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, phone);
    }

    // 3. 根据ID查询用户 (获取详情)
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return DBUtil.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, id);
    }

    // 4. 登录验证 (返回完整User对象，若密码不对返回null)
    public User login(String phone, String password) {
        User user = findByPhone(phone);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // 5. 更新用户信息 (除密码外)
    public int updateUser(User user) {
        String sql = "UPDATE users SET nickname = ?, gender = ?, sexual_orientation = ?, " +
                "age = ?, city = ?, mbti = ? WHERE id = ?";
        return DBUtil.executeUpdate(sql,
                user.getNickname(),
                user.getGender(),
                user.getSexualOrientation(),
                user.getAge(),
                user.getCity(),
                user.getMbti(),
                user.getId()
        );
    }

    // 6. 单独修改密码
    public int updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        return DBUtil.executeUpdate(sql, newPassword, userId);
    }

    // 私有方法：ResultSet 转 User 对象 (消除重复代码)
    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setPhone(rs.getString("phone"));
        user.setPassword(rs.getString("password"));
        user.setNickname(rs.getString("nickname"));
        user.setGender(rs.getInt("gender"));
        user.setSexualOrientation(rs.getString("sexual_orientation"));
        user.setShowGender(rs.getInt("show_gender"));
        user.setAge(rs.getInt("age"));
        user.setCity(rs.getString("city"));
        user.setMbti(rs.getString("mbti"));
        user.setLatitude(rs.getDouble("latitude"));
        user.setLongitude(rs.getDouble("longitude"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
}