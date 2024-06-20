package com.exp.controller;

import com.exp.anno.Log;
import com.exp.anno.RequiresRole;
import com.exp.dto.UserStatusUpdateRequest;
import com.exp.pojo.*;
import com.exp.pojo.Class;
import com.exp.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j  // 日志记录注解 内部定义了log对象
@RequestMapping("/admins")   // 抽取公共请求路径
@RestController
@RequiresRole({Role.ADMIN}) // 权限管理
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/books")     // 分页条件查询书籍信息
    public Result pageBook(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, String author, String press, String language) {
        log.info("查询书籍信息");
        PageBean pageBean = adminService.pageBook(page, pageSize, name, author, press, language);
        return Result.success(pageBean);
    }

    @GetMapping("/book/{id}")     // 书籍详情
    public Result getBook(@PathVariable Integer id){
        log.info("书籍详情, id: {}", id);
        Book book = adminService.getBook(id);
        return Result.success(book);
    }

    @GetMapping("/classes")     // 类别信息
    public Result classList(){
        log.info("查询类别信息");
        List<Class> classList  = adminService.classList();
        return Result.success(classList);
    }

    @GetMapping("/class/books/{id}")     // 根据类别查询
    public Result bookListByClass(@PathVariable Integer id){
        log.info("根据类别{}查询",id);
        List<Book> bookList  = adminService.bookListByClass(id);
        return Result.success(bookList);
    }

    @GetMapping("/users")   // 查询用户信息
    public Result pageUser(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询用户信息");
        PageBean pageBean = adminService.pageUser(page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/user/{id}")     // 用户详情
    public Result getUser(@PathVariable Integer id){
        log.info("用户详情, id: {}", id);
        User user = adminService.getUser(id);
        return Result.success(user);
    }

    @GetMapping("/admins")  // 查询管理员信息
    public Result adminList(){
        log.info("查询管理员信息");
        List<Admin> adminList = adminService.adminList();
        return Result.success(adminList);
    }

    @GetMapping("/analysts")  // 查询分析师信息
    public Result analystList(){
        log.info("查询分析师信息");
        List<Analyst> analystList = adminService.analystList();
        return Result.success(analystList);
    }

    @GetMapping("/lends")   // 查询借阅信息
    public Result pageLend(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询借阅信息");
        PageBean pageBean = adminService.pageLend(page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/OperateLogs")   // 查询系统日志
    public Result pageLog(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询系统日志信息");
        PageBean pageBean = adminService.pageOperateLog(page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/OperateLog/{id}")     //日志详情
    public Result getLog(@PathVariable Integer id){
        log.info("日志详情, id: {}", id);
        OperateLog operateLog = adminService.getOperateLog(id);
        return Result.success(operateLog);
    }

    @Log
    @DeleteMapping("/books/{ids}")    // 书籍删除和批量删除
    public Result deleteBooks(@PathVariable List<Integer> ids){
        log.info("(批量)删除书籍, ids: {}", ids);
        adminService.deleteBooks(ids);
        return Result.success();
    }

    @Log
    @PostMapping("/book/saveBook")    // 新增书籍
    public Result insertBook(@RequestBody Book book){
        log.info("新增书籍, book: {}", book);
        return adminService.insertBook(book);
    }

    @Log
    @PutMapping("/book/updateBook")     // 更新书籍信息 —— 根据id更新
    public Result updateBook(@RequestBody Book book){
        log.info("更新书籍信息: {}", book);
        return adminService.updateBook(book);
    }

    @Log
    @PostMapping("/class/saveClass")    // 新增类别
    public Result insertClass(@RequestBody Class clas){
        log.info("新增类别, clas: {}", clas);
        return adminService.insertClass(clas);
    }

    @Log
    @DeleteMapping("/class/{id}")    // 删除类别, 必须保证该类别下没有书籍才可以删除
    public Result deleteClass(@PathVariable Integer id){
        log.info("(批量)删除类别, id: {}", id);
        return adminService.deleteClass(id);
    }

    @Log
    @PutMapping("/class/updateClass")    // 更新类别信息
    public Result updateClass(@RequestBody Class clas){
        log.info("更新类别信息: {}", clas);
        return adminService.updateClass(clas.getId(), clas.getName());
    }

    @Log
    @DeleteMapping("/users/{ids}")    // 用户删除和批量删除
    public Result deleteUsers(@PathVariable List<Integer> ids){
        log.info("(批量)删除用户, ids: {}", ids);
        adminService.deleteUsers(ids);
        return Result.success();
    }

    @Log
    @PutMapping("/user/setIsEnabled")   //启用/禁用用户, 解除/加入黑名单
    public Result userIsEnabled(@RequestBody UserStatusUpdateRequest request){
        log.info("启用/禁用用户:{}", request);
        adminService.updateUserStatus(request.getId(), request.getIsEnabled());
        return Result.success();
    }

    @Log
    @PostMapping("/analyst/saveAnalyst")    // 新增分析师
    public Result insertClass(@RequestBody Analyst analyst){
        log.info("新增分析师, analyst: {}", analyst);
        return adminService.insertAnalyst(analyst);
    }

    @Log
    @DeleteMapping("/analyst/{id}")    // 删除分析师
    public Result deleteAnalyst(@PathVariable Integer id){
        log.info("(批量)删除类别, analyst: {}", id);
        adminService.deleteAnalyst(id);
        return Result.success();
    }

}
