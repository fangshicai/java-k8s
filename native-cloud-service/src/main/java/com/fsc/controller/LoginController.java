package com.fsc.controller;

import cn.hutool.http.Header;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fsc.config.RedisCache;
import com.fsc.mapper.SysUserMapper;
import com.fsc.model.LoginUser;
import com.fsc.model.R;
import com.fsc.model.SysUser;
import com.fsc.model.form.UserLoginForm;
import com.fsc.service.LoginService;
import com.fsc.utils.CheckCodeUtil;
import com.sun.istack.internal.NotNull;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/login")
    public R login(@RequestBody UserLoginForm user, @NotNull @RequestHeader String captcha) {
        if (!checkCode(user.getCaptcha(), captcha)) {
            return R.fail("验证码错误");
        }
        return loginService.login(user);
    }

    @GetMapping("/getCaptcha")
    @ApiOperation(value = "获取验证码")
    public void getMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteAddr = request.getRemoteAddr();
        // redis中的key
        response.addHeader("captcha", remoteAddr + "captcha");
        log.info("captcha:{}", remoteAddr + "captcha");
        ServletOutputStream os = response.getOutputStream();
        // 图形验证码
        String code = CheckCodeUtil.outputVerifyImage(120, 50, os, 4);
        //获取用户ip作为键值
        redisTemplate.opsForValue().set(remoteAddr + "captcha", code, 3, TimeUnit.MINUTES);
    }

    /**
     * 校验图形验证码
     *
     * @param code 图形验证码
     * @param key  redis的key
     * @return 是否有效
     */
    private boolean checkCode(String code, String key) {
        // 校验验证码的有效性
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(code)) {
            return false;
        }
        String getCode = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(getCode)) {
            return false;
        }
        // 忽略大小写校验验证码code
        if (!code.equalsIgnoreCase(getCode)) {
            redisTemplate.delete(key);
            return false;
        }
        redisTemplate.delete(key);
        return true;
    }


}
