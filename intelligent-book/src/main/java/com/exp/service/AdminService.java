package com.exp.service;

import com.exp.pojo.*;

import java.util.List;

public interface AdminService {

    PageBean pageBook(Integer page, Integer pageSize, String name, String author, String press, String language);
    Book getBook(Integer id);
    PageBean pageUser(Integer page, Integer pageSize);
    User getUser(Integer id);
    PageBean pageLend(Integer page, Integer pageSize);
    void deleteBooks(List<Integer> ids);

    void deleteUsers(List<Integer> ids);

    Result insertBook(Book book);
    Result updateBook(Book book);

    void updateUserStatus(Integer id, Integer isEnabled);

    PageBean pageOperateLog(Integer page, Integer pageSize);

    OperateLog getOperateLog(Integer id);
}
