package com.exp.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.exp.anno.RequiresRole;
import com.exp.pojo.Result;
import com.exp.pojo.Role;
import com.exp.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的令牌token
        String jwt = request.getHeader("token");

        // 判断令牌是否存在, 如果不存在, 返回错误结果(未登录)
        if (jwt == null || jwt.isEmpty()) {
            log.info("请求头token为空, 返回未登录的信息");
            sendErrorResponse(response, "NOT_LOGIN");
            return false;
        }

        // 解析token, 如果解析失败, 返回错误结果(未登录)
        Claims claims;
        try {
            claims = JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            log.info("解析令牌失败, 返回未登录的信息");
            sendErrorResponse(response, "NOT_LOGIN");
            return false;
        }

        // 获取用户角色
        String role = (String) claims.get("role");

        // 获取方法上的注解
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            RequiresRole requiresRole = handlerMethod.getMethodAnnotation(RequiresRole.class);

            // 获取类上的注解
            if (requiresRole == null) {
                requiresRole = handlerMethod.getBeanType().getAnnotation(RequiresRole.class);
            }

            // 如果注解存在，检查角色
            if (requiresRole != null) {
                List<Role> requiredRoles = Arrays.asList(requiresRole.value());
                boolean hasRole = requiredRoles.stream().anyMatch(r -> r.getRole().equals(role));
                if (!hasRole) {
                    log.info("返回权限错误的信息");
                    sendErrorResponse(response, "NO_PERMISSION");
                    return false;
                }
            }
        }

        log.info("令牌合法, 放行");
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        Result error = Result.error(message);
        String jsonResponse = JSONObject.toJSONString(error);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
