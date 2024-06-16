package com.exp.controller;

import com.exp.dto.UserStatusUpdateRequest;
import com.exp.pojo.Book;
import com.exp.pojo.PageBean;
import com.exp.pojo.Result;
import com.exp.pojo.User;
import com.exp.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j  // 日志记录注解 内部定义了log对象
@RequestMapping("/admins")   // 抽取公共请求路径
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/books")     // 分页条件查询书籍信息
    public Result pageBook(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, String author, String press, String language) {
        PageBean pageBean = adminService.pageBook(page, pageSize, name, author, press, language);
        return Result.success(pageBean);
    }

    @GetMapping("/book/{id}")     //书籍详情
    public Result getBook(@PathVariable Integer id){
        log.info("书籍详情, id: {}", id);
        Book book = adminService.getBook(id);
        return Result.success(book);
    }

    @GetMapping("/users")   // 查询用户信息
    public Result pageUser(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = adminService.pageUser(page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/user/{id}")     //用户详情
    public Result getUser(@PathVariable Integer id){
        log.info("用户详情, id: {}", id);
        User user = adminService.getUser(id);
        return Result.success(user);
    }

    @GetMapping("/lends")   // 查询借阅信息
    public Result pageLend(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = adminService.pageLend(page, pageSize);
        return Result.success(pageBean);
    }

    @DeleteMapping("/books/{ids}")    // 书籍删除和批量删除
    public Result deleteBooks(@PathVariable List<Integer> ids){
        log.info("(批量)删除书籍, ids: {}", ids);
        adminService.deleteBooks(ids);
        return Result.success();
    }

    @PostMapping("/book/saveBook")    // 新增书籍
    public Result insertBook(@RequestBody Book book){
        log.info("新增书籍, book: {}", book);
        return adminService.insertBook(book);
    }

    @PutMapping("/book/updateBook")     // 更新书籍信息 —— 根据id更新
    public Result updateBook(@RequestBody Book book){
        log.info("更新书籍信息: {}", book);
        return adminService.updateBook(book);
    }

    @DeleteMapping("/users/{ids}")    // 用户删除和批量删除
    public Result deleteUsers(@PathVariable List<Integer> ids){
        log.info("(批量)删除用户, ids: {}", ids);
        adminService.deleteUsers(ids);
        return Result.success();
    }

    @PutMapping("/user/setIsEnabled1")   //启用用户, 解除黑名单
    public Result userIsEnabled(@RequestBody UserStatusUpdateRequest request){
        adminService.updateUserStatus(request.getId(), request.getIsEnabled());
        return Result.success();
    }
}
