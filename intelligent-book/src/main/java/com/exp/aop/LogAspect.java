package com.exp.aop;

import com.alibaba.fastjson.JSONObject;
import com.exp.config.AppConfig;
import com.exp.dto.OperateInfo;
import com.exp.mapper.OperateLogMapper;
import com.exp.pojo.OperateLog;
import com.exp.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.exp.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable{
        // 设置系统默认参数
        OperateInfo operateInfo = new OperateInfo();
        try {
            String jwt = request.getHeader("token");
            Claims claims = JwtUtils.parseJWT(jwt);
            operateInfo.setOperateRole((String) claims.get("role"));
            operateInfo.setOperateUser((Integer) claims.get("id"));
            operateInfo.setOperateUsername((String) claims.get("username"));
        } catch (Exception e){
            operateInfo.setOperateRole(AppConfig.DEFAULT_OPERATE_ROLE);
            operateInfo.setOperateUser(AppConfig.DEFAULT_OPERATE_USER);
            operateInfo.setOperateUsername(AppConfig.DEFAULT_OPERATE_USERNAME);
        }

        // 操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        // 操作类名
        String className = joinPoint.getTarget().getClass().getName();

        // 操作方法名
        String methodName = joinPoint.getSignature().getName();

        // 操作方法参数
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        Long begin = System.currentTimeMillis();
        // 调用原始目标方法运行
        Object result = joinPoint.proceed();
        Long end = System.currentTimeMillis();

        // 方法返回值
        String returnValue = JSONObject.toJSONString(result);

        // 操作耗时
        Long costTime = end - begin;

        //记录操作日志
        OperateLog operateLog = new OperateLog(null, operateInfo.getOperateRole(), operateInfo.getOperateUser(), operateInfo.getOperateUsername(),
                operateTime, className, methodName, methodParams, returnValue, costTime);
        operateLogMapper.insert(operateLog);

        log.info("AOP记录操作日志: {}", operateLog);
        return result;
    }
}
