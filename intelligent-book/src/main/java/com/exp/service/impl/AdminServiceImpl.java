package com.exp.service.impl;

import com.exp.mapper.AdminMapper;
import com.exp.mapper.OperateLogMapper;
import com.exp.pojo.*;
import com.exp.service.AdminService;
import com.exp.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageBean pageBook(Integer page, Integer pageSize, String name, String author, String press, String language) {
        // 设置分页参数 —— 页码, 记录数
        PageHelper.startPage(page, pageSize);
        // 执行查询
        List<Book> bookList = adminMapper.bookList(name, author, press, language);
        Page<Book> p = (Page<Book>) bookList;
        // 封装PageBean对象
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Override
    public Book getBook(Integer id) {
        return adminMapper.getBookById(id);
    }

    @Override
    public PageBean pageUser(Integer page, Integer pageSize) {
        // 获取总记录数
        Long count = adminMapper.countUser();
        // 获取分页查询结果列表, 起始索引 = (页码 - 1) * 记录数, 注意索引从0开始, 而页码是从1开始的
        Integer start = (page - 1) * pageSize;
        List<User> userList = adminMapper.userList(start, pageSize);
        // 封装PageBean对象
        return new PageBean(count, userList);
    }

    @Override
    public User getUser(Integer id) {
        return adminMapper.getUserById(id);
    }

    @Override
    public PageBean pageLend(Integer page, Integer pageSize) {
        // 获取总记录数
        Long count = adminMapper.countLend();
        // 获取分页查询结果列表, 起始索引 = (页码 - 1) * 记录数, 注意索引从0开始, 而页码是从1开始的
        Integer start = (page - 1) * pageSize;
        List<Lend> lendList = adminMapper.lendList(start, pageSize);
        // 封装PageBean对象
        return new PageBean(count, lendList);
    }

    @Override
    public void deleteBooks(List<Integer> ids) {
        adminMapper.deleteBooks(ids);
    }

    @Override
    public void deleteUsers(List<Integer> ids) {
        adminMapper.deleteUsers(ids);
    }

    @Transactional
    @Override
    public Result insertBook(Book book) {
        try {
            book.setCreateTime(LocalDateTime.now());
            book.setUpdateTime(LocalDateTime.now());

            adminMapper.insertBook(book);
            return Result.success();
        } catch (DataIntegrityViolationException e) {
            // 捕获唯一约束违规异常
            return Result.error("Username already exists.");
        } catch (Exception e) {
            // 捕获其他异常
            return Result.error(e.getMessage());
        }

    }

    @Transactional
    @Override
    public Result updateBook(Book book) {
        try {
            book.setUpdateTime(LocalDateTime.now());

            adminMapper.updateBook(book);
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
    public void updateUserStatus(Integer id, Integer isEnabled) {
        if(isEnabled == 1){
            userService.removeUserFromBlacklist(id);
        }else {
            userService.blacklistUser(id);
        }
    }

    @Override
    public PageBean pageOperateLog(Integer page, Integer pageSize) {
        // 获取总记录数
        Long count = operateLogMapper.countOperateLog();
        // 获取分页查询结果列表, 起始索引 = (页码 - 1) * 记录数, 注意索引从0开始, 而页码是从1开始的
        Integer start = (page - 1) * pageSize;
        List<OperateLog> operateLogList = operateLogMapper.operateLogList(start, pageSize);
        // 封装PageBean对象
        return new PageBean(count, operateLogList);
    }

    @Override
    public OperateLog getOperateLog(Integer id) {
        return operateLogMapper.getOperateLogById(id);
    }

}