package com.exp.service.impl;

import com.exp.config.AppConfig;
import com.exp.mapper.UserMapper;
import com.exp.pojo.*;
import com.exp.pojo.Class;
import com.exp.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public PageBean pageBook(Integer userId, Integer page, Integer pageSize, String name, String author, String press, String language,
                             Double lowerPrice, Double upperPrice, LocalDate beginPubDate, LocalDate endPubDate) {
        // 设置分页参数 —— 页码, 记录数
        PageHelper.startPage(page, pageSize);
        // 执行查询
        List<Book> bookList = userMapper.bookList(name, author, press, language, lowerPrice, upperPrice, beginPubDate, endPubDate);
        // 点赞信息
        for (Book book : bookList) {
            Integer liked = userMapper.existsLike(userId, book.getId()) ? 1 : 0;
            book.setIsLike(liked);
        }
        Page<Book> p = (Page<Book>) bookList;
        // 封装PageBean对象
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Override
    public Book getBook(Integer id) {
        return userMapper.getBookById(id);
    }

    @Override
    public List<Class> classList() {
        return userMapper.classList();
    }

    @Override
    public List<Book> bookListByClass(Integer id) {
        return userMapper.bookListByClass(id);
    }

    @Override
    public PageBean pageLend(Integer page, Integer pageSize, Integer id) {
        // 获取总记录数
        Long count = userMapper.countLendByUserId(id);
        // 获取分页查询结果列表, 起始索引 = (页码 - 1) * 记录数, 注意索引从0开始, 而页码是从1开始的
        Integer start = (page - 1) * pageSize;
        List<Lend> lendList = userMapper.lendListByUserId(start, pageSize, id);
        // 封装PageBean对象
        return new PageBean(count, lendList);
    }

    @Override
    public User getUser(Integer id) {
        return userMapper.getUserById(id);
    }

    @Transactional
    @Override
    public Result lendBook(Lend lend) {
        // 是否可借阅
        Book book = userMapper.getBookById(lend.getBookId());
        if (book.getNumber() == 0)
            return Result.error("The book is not available for lending.");

        lend.setLendDate(LocalDate.now());
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
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
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
    public PageBean pageNotification(Integer page, Integer pageSize, Integer id) {
        // 获取总记录数
        Long count = userMapper.countNotificationByUserId(id);
        // 获取分页查询结果列表, 起始索引 = (页码 - 1) * 记录数, 注意索引从0开始, 而页码是从1开始的
        Integer start = (page - 1) * pageSize;
        List<Notification> notificationList = userMapper.notificationListByUserId(start, pageSize, id);
        // 封装PageBean对象
        return new PageBean(count, notificationList);
    }

    @Override
    public PageBean pageIsLike(Integer page, Integer pageSize, Integer id) {
        // 获取总记录数
        Long count = userMapper.countIsLikeByUserId(id);
        // 获取分页查询结果列表, 起始索引 = (页码 - 1) * 记录数, 注意索引从0开始, 而页码是从1开始的
        Integer start = (page - 1) * pageSize;
        List<Notification> notificationList = userMapper.isLikeListByUserId(start, pageSize, id);
        // 封装PageBean对象
        return new PageBean(count, notificationList);
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
    @Override
    public void removeUserFromBlacklist(Integer userId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage("You have been removed from the blacklist.");
        notification.setIsRead(0);
        notification.setCreateTime(LocalDateTime.now());

        userMapper.insertNotification(notification);

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

    @Transactional
    @Override
    public Result updateStar(Integer userId, Integer bookId, Integer isLike) {
        if (isLike == 1 && !userMapper.existsLike(userId, bookId)) {
            // 点赞操作
            userMapper.insertLike(userId, bookId);
            userMapper.incrementStars(bookId);
            return Result.success();
        } else if (isLike == 0 && userMapper.existsLike(userId, bookId)) {
            // 取消点赞操作
            userMapper.deleteLike(userId, bookId);
            userMapper.decrementStars(bookId);
            return Result.success();
        }
        return Result.error("You have done useless repetitive operations.");
    }

    @Transactional
    @Override
    public void removeIsLike(Integer userId, Integer bookId) {
        userMapper.deleteLike(userId, bookId);
        userMapper.decrementStars(bookId);
    }

}


