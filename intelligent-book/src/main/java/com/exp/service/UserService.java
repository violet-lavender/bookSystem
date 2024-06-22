package com.exp.service;

import com.exp.pojo.*;
import com.exp.pojo.Class;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    ListResult bookListByTime();

    ListResult bookListByUp();

    ListResult bookListRecommend();

    PageBean pageBook(Integer userId, Integer page, Integer pageSize, String name, String author, String press, String language,
                      Double lowerPrice, Double upperPrice, LocalDate beginPubDate, LocalDate endPubDate);

    Book getBook(Integer id);


    List<Class> classList();

    List<Book> bookListByClass(Integer id);

    PageBean pageLend(Integer page, Integer pageSize, Integer id);

    User getUser(Integer id);

    Result lendBook(Lend lend);

    Result updateUser(User user);

    PageBean pageNotification(Integer page, Integer pageSize, Integer id);

    PageBean pageIsLike(Integer page, Integer pageSize, Integer id);

    void blacklistUser(Integer userId);

    void removeUserFromBlacklist(Integer userId);

    void setIsRead(List<Integer> ids);

    void setIsVisual(List<Integer> ids);

    Result backBook(Integer id, Integer grade, String assess);

    void delayBook(Integer id);

    Result updateStar(Integer userId, Integer bookId, Integer isLike);


    void removeIsLike(Integer userId, Integer bookId);


}
