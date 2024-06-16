package com.exp.service.impl;

import com.exp.config.AppConfig;
import com.exp.mapper.UserMapper;
import com.exp.pojo.*;
import com.exp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageBean pageBook(Integer page, Integer pageSize, String name, String author, String press, String language, double lowerPrice, double upperPrice, LocalDate beginPubDate, LocalDate endPubDate) {
        return null;
    }

    @Override
    public Book getBook(Integer id) {
        Book book = userMapper.getBookById(id);
        return book;
    }

    @Override
    public PageBean pageLend(Integer page, Integer pageSize, Integer id) {
        // 获取总记录数
        Long count = userMapper.countLendByUserId(id);
        // 获取分页查询结果列表, 起始索引 = (页码 - 1) * 记录数, 注意索引从0开始, 而页码是从1开始的
        Integer start = (page - 1) * pageSize;
        List<Lend> lendList = userMapper.lendListByUserId(start, pageSize, id);
        // 封装PageBean对象
        PageBean pageBean = new PageBean(count, lendList);
        return pageBean;
    }

    @Override
    public User getUser(Integer id) {
        User user = userMapper.getUserById(id);
        return user;
    }

    @Transactional
    @Override
    public Result lendBook(Lend lend) {
        // 是否可借阅
        Book book = userMapper.getBookById(lend.getBookId());
        if (book.getNumber() == 0)
            return Result.error("The book is not available for lending.");

        lend.setCreateTime(LocalDateTime.now());
        lend.setUpdateTime(LocalDateTime.now());

        // 插入借阅信息
        userMapper.insertLend(lend);
        // 更新书籍信息
        userMapper.updateBook(lend.getBookId());
        // 更新用户借阅信息
        userMapper.updateFrequency(lend.getUserId());

        return Result.success();
    }

    @Transactional
    @Override
    public Result updateUser(User user) {
        try {
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());

            userMapper.updateUser(user);
            return Result.success();
        } catch (DataIntegrityViolationException e) {
            // 捕获唯一约束违规异常
            return Result.error("Username already exists.");
        } catch (Exception e) {
            // 捕获其他异常
            return Result.error(e.getMessage());
        }
    }

    @Override
    public PageBean getNotification(Integer page, Integer pageSize, Integer id) {
        // 获取总记录数
        Long count = userMapper.countNotificationByUserId(id);
        // 获取分页查询结果列表, 起始索引 = (页码 - 1) * 记录数, 注意索引从0开始, 而页码是从1开始的
        Integer start = (page - 1) * pageSize;
        List<Notification> notificationList = userMapper.notificationListByUserId(start, pageSize, id);
        // 封装PageBean对象
        PageBean pageBean = new PageBean(count, notificationList);
        return pageBean;
    }

    // 添加黑名单
    @Transactional
    @Override
    public void blacklistUser(Integer userId) {
        LocalDateTime blacklistUntil = LocalDateTime.now().plus(AppConfig.DEFAULT_BLACKLIST_DURATION_DAYS, ChronoUnit.DAYS);
        userMapper.blacklistUser(userId, blacklistUntil);

        // 创建黑名单通知
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage("You have been added to the blacklist due to multiple overdue returns. You will be blacklisted until " + blacklistUntil + ".");
        notification.setIsRead(0);
        notification.setCreateTime(LocalDateTime.now());

        userMapper.insertNotification(notification);
    }

    // 移除黑名单
    @Transactional
    @Override
    public void removeUserFromBlacklist(Integer userId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage("You have been removed from the blacklist.");
        notification.setIsRead(0);
        notification.setCreateTime(LocalDateTime.now());

        userMapper.removeUserFromBlacklist(userId);
    }

    @Override
    public void setIsRead(List<Integer> ids) {
        userMapper.setIsRead(ids);
    }

    @Override
    public void setIsVisual(List<Integer> ids) {
        userMapper.setIsVisual(ids);
    }

    @Override
    public void backBook(Integer id) {
        userMapper.backBook(id);
    }

    @Override
    public void delayBook(Integer id) {
        userMapper.delayBook(id, AppConfig.DEFAULT_RENEWAL_DURATION_DAYS);
    }


}


