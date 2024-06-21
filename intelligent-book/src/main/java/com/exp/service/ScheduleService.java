package com.exp.service;

import com.exp.anno.Log;
import com.exp.config.AppConfig;
import com.exp.mapper.UserMapper;
import com.exp.pojo.Lend;
import com.exp.pojo.Notification;
import com.exp.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    // 检查逾期记录
    @Log
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 每天午夜执行一次
    public void checkOverdueLends() {
        LocalDate today = LocalDate.now();
        List<Lend> overdueLends = userMapper.findOverdueLends(today);

        for (Lend lend : overdueLends) {
            // 更新用户的 disFrequency
            userMapper.updateDisFrequency(lend.getUserId());

            // 检查用户的逾期次数
            int disFrequency = userMapper.getDisFrequency(lend.getUserId());

            if (disFrequency >= AppConfig.DEFAULT_REMINDER_LIMIT && disFrequency < AppConfig.DEFAULT_BLACKLIST_OVERDUE_LIMIT) {
                // 创建即将加入黑名单的警告通知
                Notification notification = new Notification();
                notification.setUserId(lend.getUserId());
                notification.setMessage("You are at risk of being added to the blacklist due to multiple overdue returns. Please return your books promptly.");
                notification.setIsRead(0);
                notification.setCreateTime(LocalDateTime.now());

                userMapper.insertNotification(notification);
            } else if (disFrequency >= AppConfig.DEFAULT_BLACKLIST_OVERDUE_LIMIT) {
                userService.blacklistUser(lend.getUserId());
            } else {
                // 创建逾期通知
                Notification notification = new Notification();
                notification.setUserId(lend.getUserId());
                notification.setMessage("You have overdue books. Please return them as soon as possible.");
                notification.setIsRead(0);
                notification.setCreateTime(LocalDateTime.now());

                userMapper.insertNotification(notification);
            }
        }
    }

    // 检查黑名单
    @Log
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 每天午夜执行一次
    public void checkAndRemoveFromBlacklist() {
        LocalDateTime now = LocalDateTime.now();
        List<User> blacklistedUsers = userMapper.findUsersInBlacklist();

        for (User user : blacklistedUsers) {
            LocalDateTime blacklistUntil = user.getBlacklistUntil();
            if (blacklistUntil != null && now.isAfter(blacklistUntil)) {
                // 已经过了解除黑名单时间，将用户从黑名单中移除
                userService.removeUserFromBlacklist(user.getId());
            }
        }
    }

    // 应用启动时执行检查任务
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(){
        checkOverdueLends();
        checkAndRemoveFromBlacklist();
    }

}
