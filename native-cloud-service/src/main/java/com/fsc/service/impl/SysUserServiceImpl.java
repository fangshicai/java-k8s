package com.fsc.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fsc.mapper.SysUserMapper;
import com.fsc.model.SysUser;
import com.fsc.service.SysUserService;
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Override
    public List<String> selectPrivilegeByUserId(Long userId) {
        return this.getBaseMapper().selectPrivilegeByUserId(userId);
    }
}
