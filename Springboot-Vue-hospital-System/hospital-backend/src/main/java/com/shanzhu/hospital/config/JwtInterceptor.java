package com.shanzhu.hospital.config;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shanzhu.hospital.utils.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt 拦截器
 *
 * 
 * @date: 2023-11-17
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 白名单：允许 OPTIONS 预检请求，以及一些无需 token 的接口（登录、注册、发送邮箱、文件导出）直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        String servletPath = request.getServletPath();
        String pathToCheck = "";
        if (servletPath != null && !servletPath.isEmpty()) {
            pathToCheck = servletPath.toLowerCase();
        } else if (uri != null) {
            pathToCheck = uri.toLowerCase();
        }

        if (pathToCheck.contains("/login") || pathToCheck.contains("/addpatient")
                || pathToCheck.contains("/patient/pdf") || pathToCheck.contains("/sendemail")) {
            return true;
        }

        // 获取请求头中的令牌
        String token = request.getHeader("token");
        // 如果 token 为空，直接返回错误信息，避免后续 JwtUtil.verify(token) 抛出 NPE
        if (token == null || token.trim().isEmpty()) {
            map.put("msg", "token不存在或为空！");
            map.put("state", false);
            String json = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(json);
            return false;
        }
        try {
            JwtUtil.verify(token);// 验证令牌
            return true;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg", "无效签名！");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "token过期！");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "token算法不一致！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效！");
        }
        map.put("state", false);
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);

        return false;
    }
}
