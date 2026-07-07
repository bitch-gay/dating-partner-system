package com.dating;

import com.dating.dao.ActivityDao;
import com.dating.dao.UserDao;
import com.dating.model.Activity;
import com.dating.model.User;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        UserDao userDao = new UserDao();
        ActivityDao activityDao = new ActivityDao();

        // ===== 1. 测试注册 =====
        User newUser = new User("18164166485", "hstc051024", "陈俊凯",
                0, "异性", 21, "潮州");
        int result = userDao.register(newUser);
        if (result > 0) {
            System.out.println("✅ 注册成功！用户: " + newUser.getNickname());
        } else {
            System.out.println("❌ 注册失败，手机号可能已存在");
        }

        // ===== 2. 测试登录 =====
        User loginUser = userDao.login("13800138000", "Password123");
        if (loginUser != null) {
            System.out.println("✅ 登录成功！欢迎回来: " + loginUser.getNickname());
        } else {
            System.out.println("❌ 登录失败，账号或密码错误");
        }

        // ===== 3. 测试发布搭子 =====
        // 假设上面注册的用户ID为1（如果你表里已有数据，改成实际的ID）
        Integer testUserId = 1;
        Activity activity = new Activity(testUserId, "周末爬白云山", "运动",
                "不限", "白云山南门",
                LocalDateTime.now().plusDays(3), 4);
        int pubResult = activityDao.publish(activity);
        if (pubResult > 0) {
            System.out.println("✅ 搭子活动发布成功！标题: " + activity.getTitle());
        } else {
            System.out.println("❌ 发布失败");
        }

        // ===== 4. 测试查询所有可用搭子 =====
        System.out.println("\n📋 当前进行中的搭子活动列表:");
        activityDao.findAllAvailable().forEach(a ->
                System.out.println("  - " + a.getTitle() + " | 地点: " + a.getLocationDesc() +
                        " | 当前人数: " + a.getCurrentPeople() + "/" + a.getMaxPeople())
        );

        System.out.println("\n===== 测试修改数据 =====");

        // 1. 修改用户信息 (假设我要把ID=1的用户改成22岁)
        User updateUser = userDao.findById(1);
        if (updateUser != null) {
            updateUser.setAge(22);
            updateUser.setCity("深圳");
            int updateResult = userDao.updateUser(updateUser);
            System.out.println(updateResult > 0 ? "✅ 用户信息修改成功！" : "❌ 修改失败");
        }

        // 2. 修改活动 (假设我要改ID=1的活动标题)
        Activity updateAct = activityDao.findById(1);
        if (updateAct != null) {
            updateAct.setTitle("周日爬白云山（改期）");
            int actResult = activityDao.updateActivity(updateAct);
            System.out.println(actResult > 0 ? "✅ 活动修改成功！" : "❌ 修改失败");
        }
    }
}