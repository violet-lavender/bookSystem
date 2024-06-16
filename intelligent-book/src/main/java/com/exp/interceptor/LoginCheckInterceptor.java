package com.exp.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.exp.pojo.Result;
import com.exp.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取请求url
        String url = request.getRequestURL().toString();
        log.info("请求的url: {}", url);

        // 获取请求头中的令牌token
        String jwt = request.getHeader("token");

        // 判断令牌是否存在, 如果不存在, 返回错误结果(未登录)
        if (!StringUtils.hasLength(jwt)){
            log.info("请求头token为空, 返回未登录的信息");
            sendErrorResponse(response, "NOT_LOGIN");
            return false;
        }

        // 解析token, 如果解析失败, 返回错误结果(未登录) —— 报错(异常)即解析失败
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {   // jwt解析失败
            log.info("解析令牌失败, 返回未登录的信息");
            sendErrorResponse(response, "NOT_LOGIN");
            return false;
        }

        log.info("令牌合法, 放行");
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        Result error = Result.error(message);
        String notLogin = JSONObject.toJSONString(error);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(notLogin);
    }
}
