package com.exp.service;

import com.exp.pojo.*;
import com.exp.pojo.Class;

import java.util.List;

public interface AdminService {

    ListResult bookListByTime();

    ListResult bookListByUp();

    PageBean pageBook(Integer page, Integer pageSize, String name, String author, String press, String language);

    Book getBook(Integer id);

    List<Class> classList();

    List<Book> bookListByClass(Integer id);

    PageBean pageUser(Integer page, Integer pageSize);

    User getUser(Integer id);

    PageBean pageLend(Integer page, Integer pageSize);

    List<Admin> adminList();

    List<Analyst> analystList();

    void deleteBooks(List<Integer> ids);

    void deleteUsers(List<Integer> ids);

    Result insertBook(Book book);

    Result updateBook(Book book);

    Result insertClass(Class clas);

    Result deleteClass(Integer id);

    Result updateClass(Integer id, String name);

    void updateUserStatus(Integer id, Integer isEnabled);

    PageBean pageOperateLog(Integer page, Integer pageSize);

    OperateLog getOperateLog(Integer id);

    Result insertAnalyst(Analyst analyst);

    void deleteAnalyst(Integer id);

}
