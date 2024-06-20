package com.exp.mapper;

import com.exp.pojo.Book;
import com.exp.pojo.Class;
import com.exp.pojo.Lend;
import com.exp.pojo.Notification;
import com.exp.pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {

    // 查询书籍信息
    List<Book> bookList(@Param("name") String name, @Param("author") String author, @Param("press") String press,
                        @Param("language") String language, @Param("lowerPrice") Double lowerPrice, @Param("upperPrice") Double upperPrice,
                        @Param("beginPubDate") LocalDate beginPubDate, @Param("endPubDate") LocalDate endPubDate);

    // 查询书籍详情
    @Select("select * from tb_book where id = #{id}")
    Book getBookById(@Param("id") Integer id);

    // 查询类别
    @Select("select * from tb_class order by id")
    List<Class> classList();

    // 根据类别查询书籍
    @Select("select * from tb_book where class_id = #{id}")
    List<Book> bookListByClass(@Param("id") Integer id);

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

    // 查询个人收藏总数
    @Select("select count(*) from tb_like where user_id = #{userId}")
    Long countIsLikeByUserId(@Param("userId") Integer userId);

    // 查询个人收藏
    @Select("select * from tb_like where user_id = #{userId} order by create_time desc limit #{start}, #{pageSize}")
    List<Notification> isLikeListByUserId(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("userId") Integer userId);


    // 借阅, 插入lend信息
    @Insert("insert into tb_lend(user_id, book_id, duration, lend_date, back_date, create_time, update_time)" +
            "values (#{userId}, #{bookId}, #{duration}, #{lendDate}, #{backDate}, #{createTime}, #{updateTime})")
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
    int getDisFrequency(@Param("id") Integer id);

    // 插入通知信息
    @Insert("insert into  tb_notification(user_id, message, is_read, create_time) " +
            "values (#{userId}, #{message}, #{isRead}, #{createTime})")
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
    @Update("update tb_lend set is_back = 1, back_date = curdate(), update_time = now() where id = #{id}")
    void backBook(@Param("id") Integer id);

    // 续借
    @Update("update tb_lend set duration = duration + #{delayDays}, update_time = now() where id = #{id}")
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
    @Insert("insert into tb_like(user_id, book_id, create_time) values (#{userId}, #{bookId},now())")
    void insertLike(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    // 删除点赞信息
    @Delete("delete from tb_like where user_id = #{userId} and book_id = #{bookId}")
    void deleteLike(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    // 点赞+1
    @Update("update tb_book set stars = stars + 1, update_time = now() where id = #{bookId}")
    void incrementStars(@Param("bookId") Integer bookId);

    // 点赞1
    @Update("update tb_book set stars = stars - 1, update_time = now() where id = #{bookId}")
    void decrementStars(@Param("bookId") Integer bookId);

}
