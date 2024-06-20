package com.exp.mapper;

import com.exp.pojo.*;
import com.exp.pojo.Class;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {

    // 查询书籍信息
    List<Book> bookList(@Param("name") String name, @Param("author") String author,
                        @Param("press") String press, @Param("language") String language);

    // 查询书籍详情
    @Select("select * from tb_book where id = #{id}")
    Book getBookById(@Param("id") Integer id);

    // 用户总记录数
    @Select("select count(*) from tb_user")
    Long countUser();

    // 查询用户信息
    @Select("select * from tb_user order by update_time desc limit #{start}, #{pageSize}")
    List<User> userList(@Param("start") Integer start, @Param("pageSize") Integer pageSize);

    // 查询用户详情
    @Select("select * from tb_user where id = #{id}")
    User getUserById(@Param("id") Integer id);

    // 借阅信息总记录数
    @Select("select count(*) from tb_lend")
    Long countLend();

    // 查询借阅信息
    @Select("select * from tb_lend order by update_time desc limit #{start}, #{pageSize}")
    List<Lend> lendList(@Param("start") Integer start, @Param("pageSize") Integer pageSize);

    // 查询管理员信息
    @Select("select * from tb_admin order by id")
    List<Admin> adminList();

    // 查询分析师信息
    @Select("select * from tb_analyst order by id")
    List<Analyst> analystList();

    // (批量)删除书籍
    void deleteBooks(@Param("ids") List<Integer> ids);

    // 新增书籍
    @Insert("insert into tb_book(name, author, press, ISBN, image, introduction, language, " +
            "price, pub_date, class_id, number, create_time, update_time) values (#{name}, #{author}, " +
            "#{press}, #{isbn}, #{image}, #{introduction}, #{language}, #{price}, #{pubDate}, #{classId}," +
            "#{number}, #{createTime}, #{updateTime})")
    void insertBook(Book book);

    // 更新书籍信息
    void updateBook(Book book);

    // 查询类别
    @Select("select * from tb_class where id = #{id}")
    Class getClassById(@Param("id") Integer id);

    // 新增类别
    @Insert("insert into tb_class(name, create_time, update_time) values(#{name}, #{createTime}, #{updateTime})")
    void insertClass(Class clas);

    // 删除类别
    @Delete("delete from tb_class where id = #{id}")
    void deleteClass(@Param("id") Integer id);

    // 更新类别
    @Update("update tb_class set name = #{name}, update_time = now() where id = #{id}")
    void updateClass(@Param("id") Integer id, @Param("name") String name);

    // (批量)删除用户
    void deleteUsers(@Param("ids") List<Integer> ids);

    // 启用/禁用用户
    @Update("update tb_user set is_enabled = #{isEnabled}, update_time = now() where id = #{id}")
    void updateUserStatus(@Param("id") Integer id, @Param("isEnabled") Integer isEnabled);

    // 新增分析师
    @Insert("insert into tb_analyst(username, password, create_time, update_time) " +
            "values(#{username}, #{password}, #{createTime}, #{updateTime}) ")
    void insertAnalyst(Analyst analyst);

    // 删除分析师
    @Delete("delete from tb_analyst where id = #{id}")
    void deleteAnalyst(@Param("id") Integer id);
}
