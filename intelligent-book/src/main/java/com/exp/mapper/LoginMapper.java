package com.exp.mapper;

import com.exp.dto.RegisterRequest;
import com.exp.pojo.Admin;
import com.exp.pojo.Analyst;
import com.exp.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {

    @Select("select * from tb_admin where username = #{username} and password = #{password}")
    Admin getAdmin(@Param("username") String username, @Param("password") String password);

    @Select("select * from tb_user where username = #{username} and password = #{password}")
    User getUser(@Param("username") String username, @Param("password") String password);

    @Select("select * from tb_analyst where username = #{username} and password = #{password}")
    Analyst getAnalyst(@Param("username") String username, @Param("password") String password);

    @Insert("insert into tb_user(username, password, name, gender, create_time, update_time) " +
            "values(username, password, name, gender, now(), now()) ")
    void insertUser(RegisterRequest registerRequest);


}
