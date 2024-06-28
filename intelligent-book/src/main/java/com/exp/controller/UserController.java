package com.exp.controller;

import com.exp.anno.Log;
import com.exp.anno.RequiresRole;
import com.exp.config.AppConfig;
import com.exp.dto.IdRequest;
import com.exp.dto.IdsRequest;
import com.exp.dto.StartUpdateRequest;
import com.exp.pojo.*;
import com.exp.pojo.Class;
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
@RequiresRole({Role.USER}) // 权限管理
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/booksByTime")  // 最新上架书籍列表
    public Result bookListByTime() {
        log.info("由时间查询书籍信息");
        ListResult listResult = userService.bookListByTime();
        return Result.success(listResult);
    }

    @GetMapping("/booksByUp")   // 评分、收藏书籍列表
    public Result bookListByUp() {
        log.info("由热度查询书籍信息");
        ListResult listResult = userService.bookListByUp();
        return Result.success(listResult);
    }

    @GetMapping("/booksRecommend")  // 推荐书籍列表
    public Result bookListRecommend(Integer id) {
        log.info("查询用户推荐书籍信息");
        return userService.bookListRecommend(id);
    }

    @GetMapping("/books")     // 分页条件查询书籍信息
    public Result pageBook(Integer userId,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = AppConfig.DEFAULT_PAGE_SIZE) Integer pageSize,
                           String name, String author, String className, String press, String language,
                           Double lowerPrice, Double upperPrice,
                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginPubDate,
                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endPubDate) {
        log.info("查询书籍信息");
        PageBean pageBean = userService.pageBook(userId, page, pageSize, name, author, className, press, language,
                lowerPrice, upperPrice, beginPubDate, endPubDate);
        return Result.success(pageBean);
    }

    @GetMapping("/book/{id}")     // 书籍详情
    public Result getBook(@PathVariable Integer id) {
        log.info("书籍详情, id: {}", id);
        Book book = userService.getBook(id);
        return Result.success(book);
    }

    @GetMapping("/classes")     // 类别信息
    public Result classList() {
        log.info("查询类别信息");
        List<Class> classList = userService.classList();
        return Result.success(classList);
    }

    @GetMapping("/class/books/{id}")     // 根据类别查询L
    public Result bookListByClass(@PathVariable Integer id) {
        log.info("根据类别{}查询", id);
        List<Book> bookList = userService.bookListByClass(id);
        return Result.success(bookList);
    }

    @GetMapping("/lends/{id}")    // 个人借阅信息
    public Result pageLend(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = AppConfig.DEFAULT_PAGE_SIZE) Integer pageSize, @PathVariable Integer id) {
        log.info("查询用户{}借阅信息", id);
        PageBean pageBean = userService.pageLend(page, pageSize, id);
        return Result.success(pageBean);
    }

    @GetMapping("/{id}")    // 个人详情
    public Result getUser(@PathVariable Integer id) {
        log.info("用户{}详情", id);
        User user = userService.getUser(id);
        return Result.success(user);
    }

    @GetMapping("/notifications/{id}")   // 通知信息
    public Result pageNotification(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = AppConfig.DEFAULT_PAGE_SIZE) Integer pageSize, @PathVariable Integer id) {
        log.info("查询用户{}通知信息", id);
        PageBean pageBean = userService.pageNotification(page, pageSize, id);
        return Result.success(pageBean);
    }

    @GetMapping("/notification/{id}")   // 通知信息详情
    public Result getNotification(@PathVariable Integer id) {
        log.info("查询通知信息{}详情", id);
        Notification notification = userService.getNotification(id);
        return Result.success(notification);
    }

    @GetMapping("/isLike/{id}")    // 收藏信息
    public Result pageIsLike(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = AppConfig.DEFAULT_PAGE_SIZE) Integer pageSize, @PathVariable Integer id) {
        log.info("查询用户{}收藏信息", id);
        PageBean pageBean = userService.pageIsLike(page, pageSize, id);
        return Result.success(pageBean);
    }

    @PutMapping("/notifications/setIsRead")   // 设置已读
    public Result setIsRead(@RequestBody IdsRequest ids) {
        log.info("设置已读, ids: {}", ids);
        userService.setIsRead(ids.getIds());
        return Result.success();
    }

    @PutMapping("/notifications/setIsVisual")   // “删除”, 设置为不显示
    public Result setIsVisual(@RequestBody IdsRequest ids) {
        log.info("\"删除\"通知, ids: {}", ids);
        userService.setIsVisual(ids.getIds());
        return Result.success();
    }

    @Log
    @PostMapping("/lend/saveLend")   // 借阅行为, 插入借阅记录
    public Result lendBook(@RequestBody Lend lend) {
        log.info("新增借阅记录, lend: {}", lend);
        return userService.lendBook(lend);
    }

    @Log
    @PutMapping("/lend/back")   // 归还书籍
    public Result backBook(@RequestBody Lend lend) {
        log.info("书籍归还记录, {}", lend);
        userService.backBook(lend.getId(), lend.getGrade(), lend.getAssess());
        return Result.success();
    }

    @Log
    @PutMapping("/lend/delay")   // 延长借阅
    public Result delayBook(@RequestBody IdRequest id) {
        log.info("书籍续借, lend: {}", id);
        userService.delayBook(id.getId());
        return Result.success();
    }

    @Log
    @PutMapping("/updateUser")    // 更新用户信息
    public Result updateUser(@RequestBody User user) {
        log.info("更新用户信息: {}", user);
        return userService.updateUser(user);
    }

    @Log
    @PutMapping("/stars")  // 用户点赞/取消点赞
    public Result updateStar(@RequestBody StartUpdateRequest startUpdateRequest) {
        log.info("点赞信息: {}", startUpdateRequest);
        return userService.updateStar(startUpdateRequest.getUserId(), startUpdateRequest.getBookId(), startUpdateRequest.getIsLike());
    }

    @Log
    @PutMapping("/removeLike")  // 移除收藏
    public Result removeIsLike(@RequestBody StartUpdateRequest startUpdateRequest) {
        log.info("移除收藏: {}", startUpdateRequest);
        userService.removeIsLike(startUpdateRequest.getUserId(), startUpdateRequest.getBookId());
        return Result.success();
    }


}
