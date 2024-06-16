package com.exp.controller;

import com.exp.anno.Log;
import com.exp.pojo.*;
import com.exp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/books")     // 分页条件查询书籍信息
    public Result pageBook(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           String name, String author, String press, String language,
                           double lowerPrice, double upperPrice,
                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginPubDate,
                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endPubDate) {
        PageBean pageBean = userService.pageBook(page, pageSize, name, author, press, language,
                lowerPrice, upperPrice, beginPubDate, endPubDate);
        return Result.success(pageBean);
    }

    @GetMapping("/book/{id}")     // 书籍详情
    public Result getBook(@PathVariable Integer id){
        log.info("书籍详情, id: {}", id);
        Book book = userService.getBook(id);
        return Result.success(book);
    }

    @GetMapping("/lends/{id}")    // 个人借阅信息
    public Result pageLend(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize, @PathVariable Integer id) {
        PageBean pageBean = userService.pageLend(page, pageSize, id);
        return Result.success(pageBean);
    }

    @GetMapping("/{id}")    // 个人详情
    public Result getUser(@PathVariable Integer id){
        User user = userService.getUser(id);
        return Result.success(user);
    }

    @GetMapping("/notifications/{id}")   // 通知信息
    public Result getNotification(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize, @PathVariable Integer id) {
        PageBean pageBean = userService.getNotification(page, pageSize, id);
        return Result.success(pageBean);
    }

    @PutMapping("/notifications/setIsRead")   // 设置已读
    public Result setIsRead(@RequestBody List<Integer> ids){
        userService.setIsRead(ids);
        return Result.success();
    }

    @PutMapping("/notifications/setIsVisual")   // “删除”, 设置为不显示
    public Result setIsVisual(@RequestBody List<Integer> ids){
        userService.setIsVisual(ids);
        return Result.success();
    }

    @Log
    @PostMapping("/lend/saveLend")   // 借阅行为, 插入借阅记录
    public Result lendBook(@RequestBody Lend lend){
        log.info("新增借阅记录, lend: {}", lend);
        return userService.lendBook(lend);
    }

    @Log
    @PutMapping("/lend/back")   // 归还书籍
    public Result backBook(@RequestParam Integer id){
        log.info("书籍归还记录, id: {}", id);
        userService.backBook(id);
        return Result.success();
    }

    @Log
    @PutMapping("/lend/delay")   // 延长借阅
    public Result delayBook(@RequestBody Integer id){
        log.info("书籍归还记录, lend: {}", id);
        userService.delayBook(id);
        return Result.success();
    }

    @Log
    @PutMapping("/user/updateUser")    // 更新用户信息
    public Result updateUser(@RequestBody User user){
        log.info("更新用户信息: {}", user);
        return userService.updateUser(user);
    }



}
