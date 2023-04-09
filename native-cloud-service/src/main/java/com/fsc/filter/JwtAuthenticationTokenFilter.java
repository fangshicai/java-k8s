package com.fsc.filter;

import com.fsc.config.JwtConfigProperties;
import com.fsc.config.RedisCache;
import com.fsc.model.LoginUser;
import com.fsc.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtConfigProperties jwtConfigProperties;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 获取token :Bearer token
        String authHeader = request.getHeader(jwtConfigProperties.getTokenHeader());

        if (!StringUtils.hasText(authHeader)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }

        //将token截取出来
        String token = authHeader.substring(jwtConfigProperties.getTokenHead().length());
        String userId;
        try {
            userId = jwtUtil.parseJWT(token).getSubject();

        } catch (Exception e) {
            throw new RuntimeException("非法token");
        }
        //从redis中获取用户信息并设置在上下文中
        String redisKey = "login:" + userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
//        System.out.println(loginUser.get);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
