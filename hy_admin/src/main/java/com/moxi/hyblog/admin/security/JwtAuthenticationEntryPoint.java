package com.moxi.hyblog.admin.security;

import com.alibaba.fastjson.JSONObject;
import com.moxi.hyblog.base.global.ECode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
/**
 * @author hzh
 * @since 2020-08-07
 * springSecurity token 过期统一返回体
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(ECode.SUCCESS);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Map<String, Object> result = new HashMap<>();
        result.put("code", ECode.UNAUTHORIZED);
        result.put("data", "token无效或过期");
        response.getWriter().write(JSONObject.toJSONString(result));
    }
}

