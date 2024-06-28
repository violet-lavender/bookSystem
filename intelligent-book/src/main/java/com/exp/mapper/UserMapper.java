package com.exp.mapper;

import com.exp.dto.BookGrade;
import com.exp.pojo.*;
import com.exp.pojo.Class;
import com.exp.vo.Assess;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {

    // 书的总数
    @Select("select count(*) from tb_book")
    Long countBook();

    // 上架时间列表
    @Select("select tb_book.*, tc.name as class_name from tb_book join tb_class tc on tc.id = tb_book.class_id order by create_time desc")
    List<Book> bookListByTime();

    // 热度列表
    @Select("select tb_book.*, tc.name as class_name from tb_book join tb_class tc on tc.id = tb_book.class_id order by grade desc, stars desc")
    List<Book> bookListByUp();

    // 查询书籍信息
    List<Book> bookList(@Param("name") String name, @Param("author") String author, @Param("press") String press, @Param("className") String className, @Param("language") String language, @Param("lowerPrice") Double lowerPrice, @Param("upperPrice") Double upperPrice, @Param("beginPubDate") LocalDate beginPubDate, @Param("endPubDate") LocalDate endPubDate);

    // 查询书籍详情
    @Select("select tb_book.*, tc.name as class_name from tb_book join tb_class tc on tc.id = tb_book.class_id where tb_book.id = #{id}")
    Book getBookById(@Param("id") Integer id);

    // 用户、书籍、评分
    @Select("select user_id, book_id, grade from tb_lend where grade is not null")
    List<BookGrade> bookGradeList();

    // 书籍id列表
    @Select("select id from tb_book")
    List<Integer> bookIdList();

    // 根据ids查询书籍列表
    @Select("select * from tb_book where id in (#{ids})")
    List<Book> bookListByIds(@Param("ids") List<Integer> ids);

    // 查询书籍评价列表
    @Select("select username, grade, assess, tb_lend.update_time as update_time" +
            " from tb_lend join tb_user tu on tu.id = tb_lend.user_id where book_id = #{id} and assess is not null")
    List<Assess> assessList(@Param("id") Integer id);

    // 查询类别
    @Select("select * from tb_class order by id")
    List<Class> classList();

    // 根据类别查询书籍
    @Select("select * from tb_book where class_id = #{id}")
    List<Book> bookListByClass(@Param("id") Integer id);

    // 根据id获得类别名称
    @Select("select name from tb_class where id = #{id}")
    String getClassNameById(@Param("id") Integer id);

    // 查询个人详情
    @Select("select * from tb_user where id = #{id}")
    User getUserById(@Param("id") Integer id);

    // 个人借阅总记录数
    @Select("select count(*) from tb_lend where user_id = #{userId}")
    Long countLendByUserId(@Param("userId") Integer userId);

    // 查询个人借阅信息
    @Select("select * from tb_lend where user_id = #{userId} order by is_back, update_time desc limit #{start}, #{pageSize}")
    List<Lend> lendListByUserId(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("userId") Integer userId);

    // 查询个人通知总数
    @Select("select count(*) from tb_notification where user_id = #{userId} and is_visual = 1")
    Long countNotificationByUserId(@Param("userId") Integer userId);

    // 查询个人通知
    @Select("select * from tb_notification where user_id = #{userId} and is_visual = 1 order by is_read, create_time desc limit #{start}, #{pageSize}")
    List<Notification> notificationListByUserId(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("userId") Integer userId);

    // 查询通知详情
    @Select("select * from tb_notification where id = #{id}")
    Notification getNotification(@Param("id") Integer id);

    // 查询个人收藏总数
    @Select("select count(*) from tb_like where user_id = #{userId}")
    Long countIsLikeByUserId(@Param("userId") Integer userId);

    // 查询个人收藏
    @Select("select * from tb_like where user_id = #{userId} order by create_time desc limit #{start}, #{pageSize}")
    List<Like> isLikeListByUserId(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("userId") Integer userId);

    // 借阅, 插入lend信息
    @Insert("insert into tb_lend(user_id, book_id, lend_date, duration, back_date, create_time, update_time, book_class, book_name, book_author)" + "values (#{userId}, #{bookId}, #{lendDate}, #{duration}, #{backDate}, #{createTime}, #{updateTime}, #{bookClass}, #{bookName}, #{bookAuthor})")
    void insertLend(Lend lend);

    // 更新书籍借阅和数量信息
    @Update("update tb_book set number = number - 1, lend_frequency = lend_frequency + 1, update_time = now() where id = #{id}")
    void updateBook(@Param("id") Integer id);

    // 更新用户借阅信息
    @Update("update tb_user set lend_frequency = lend_frequency + 1, update_time = now() where id = #{id}")
    void updateFrequency(@Param("id") Integer id);

    // 查询未按时归还
    @Select("select * from tb_lend where back_date < #{today} and is_back = 0")
    List<Lend> findOverdueLends(@Param("today") LocalDate today);

    // 更新用户的 disFrequency
    @Update("update tb_user set dis_frequency = dis_frequency + 1, update_time = now() where id = #{id}")
    void updateDisFrequency(@Param("id") Integer id);

    // 检查用户的逾期次数
    @Select("select dis_frequency from tb_user where id = #{id}")
    Integer getDisFrequency(@Param("id") Integer id);

    // 插入通知信息
    @Insert("insert into  tb_notification(user_id, message, is_read, create_time) " + "values (#{userId}, #{message}, #{isRead}, #{createTime})")
    void insertNotification(Notification notification);

    // 用户加入黑名单
    @Update("update tb_user set is_enabled = 0, blacklist_until = #{blacklistUntil}, update_time = now() where id = #{userId}")
    void blacklistUser(@Param("userId") Integer userId, @Param("blacklistUntil") LocalDateTime blacklistUntil);

    // 查询黑名单
    @Select("select * from tb_user where is_enabled = 0")
    List<User> findUsersInBlacklist();

    // 移除黑名单
    @Update("update tb_user set is_enabled = 1, dis_frequency = 0, blacklist_until = null, update_time = now() where id = #{userId}")
    void removeUserFromBlacklist(Integer userId);

    // 归还书籍
    @Update("update tb_lend set is_back = 1, back_date = curdate(), grade = #{grade}, " + "assess = #{assess}, update_time = now() where id = #{id}")
    void backBook(@Param("id") Integer id, @Param("grade") Integer grade, @Param("assess") String assess);

    // 由id获得lend记录
    @Select("select * from tb_lend where id = #{id}")
    Lend getLendById(@Param("id") Integer id);

    // 计算书籍评级
    @Select("select round(avg(grade)) from tb_lend where book_id = #{bookId} and grade is not null")
    Integer calculateAverageGrade(@Param("bookId") Integer bookId);

    // 更新书籍评级
    @Update("update tb_book set grade = #{grade} where id = #{id}")
    void updateBookGrade(@Param("id") Integer id, @Param("grade") Integer grade);

    // 续借
    @Update("update tb_lend set duration = duration + #{delayDays}, " + "back_date = date_add(back_date, interval #{delayDays} day), " + "update_time = now() where id = #{id}")
    void delayBook(@Param("id") Integer id, @Param("delayDays") Integer delayDays);

    // 更新个人信息
    void updateUser(User user);

    // 设置已读
    void setIsRead(@Param("ids") List<Integer> ids);

    // “删除"消息
    void setIsVisual(@Param("ids") List<Integer> ids);

    // 是否存在点赞信息
    @Select("select exists(select * from tb_like where user_id = #{userId} and book_id = #{bookId})")
    boolean existsLike(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    // 插入点赞信息
    @Insert("insert into tb_like(user_id, book_id, create_time, book_name, book_class, book_author) " + "values (#{userId}, #{bookId}, #{createTime}, #{bookName}, #{bookClass}, #{bookAuthor})")
    void insertLike(Like like);

    // 删除点赞信息
    @Delete("delete from tb_like where user_id = #{userId} and book_id = #{bookId}")
    void deleteLike(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    // 点赞+1
    @Update("update tb_book set stars = stars + 1, update_time = now() where id = #{bookId}")
    void incrementStars(@Param("bookId") Integer bookId);

    // 点赞-1
    @Update("update tb_book set stars = stars - 1, update_time = now() where id = #{bookId}")
    void decrementStars(@Param("bookId") Integer bookId);

}
