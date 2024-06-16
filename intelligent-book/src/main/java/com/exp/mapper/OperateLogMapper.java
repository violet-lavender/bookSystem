package com.exp.mapper;

import com.exp.pojo.OperateLog;
import com.exp.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperateLogMapper {

    //插入系统日志数据
    @Insert("insert into tb_operate_log (operate_role, operate_user, operate_username, operate_time, class_name, method_name, method_params, return_value, cost_time) "
            +"values (#{operateRole}, #{operateUser}, #{operateUsername}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime});")
    void insert(OperateLog log);

    //系统日志记录总数
    @Select("select count(*) from tb_operate_log")
    Long countOperateLog();

    //查询系统日志信息
    @Select("select * from tb_operate_log order by operate_time desc limit #{start}, #{pageSize}")
    List<OperateLog> operateLogList(@Param("start") Integer start, @Param("pageSize") Integer pageSize);

    //查询系统日志详情
    @Select("select * from tb_operate_log where id = #{id}")
    OperateLog getOperateLogById(@Param("id") Integer id);

}
