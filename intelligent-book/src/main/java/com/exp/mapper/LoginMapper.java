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

    @Select("select * from tb_admin where username = #{username}")
    Admin getAdminByUsername(@Param("username") String username);

    @Select("select * from tb_user where username = #{username}")
    User getUserByUsername(@Param("username") String username);

    @Select("select * from tb_analyst where username = #{username}")
    Analyst getAnalystByUsername(@Param("username") String username);

    @Insert("insert into tb_user(username, password, create_time, update_time) " +
            "values(#{username}, #{password}, now(), now()) ")
    void insertUser(RegisterRequest registerRequest);


}
