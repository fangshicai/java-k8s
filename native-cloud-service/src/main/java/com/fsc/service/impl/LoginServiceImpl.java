package com.fsc.service.impl;

import com.fsc.config.JwtConfigProperties;
import com.fsc.config.RedisCache;
import com.fsc.model.LoginUser;
import com.fsc.model.R;
import com.fsc.model.form.UserLoginForm;
import com.fsc.service.LoginService;
import com.fsc.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtConfigProperties jwtConfigProperties;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public R login(UserLoginForm user) {


        // 通过usernamePasswordAuthenticationToken来设置authenticationToken 用户名和密码
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        // AuthenticationManager委托机制对authenticationToken 进行用户认证
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //如果认证通过，使用user生成jwt  jwt存入ResponseResult 返回

        // 获取当前用户信息
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long id = loginUser.getSysUser().getId();
        String token = jwtUtil.createJWT(id.toString());
        //authenticate存入redis
        redisCache.setCacheObject("login:" + id, loginUser);
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        // 设置为 Authorization: token
        map.put(jwtConfigProperties.getTokenHeader(), jwtConfigProperties.getTokenHead()+" "+token);
        return R.ok(map);
    }

    @Override
    public R logOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getSysUser().getId();
        redisCache.deleteObject("login:"+userid);
        return R.ok("logout");
    }


}
