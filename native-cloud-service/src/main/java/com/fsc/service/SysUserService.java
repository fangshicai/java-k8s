package com.fsc.service;

import com.fsc.model.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.juli.logging.Log;

import java.util.List;

public interface SysUserService extends IService<SysUser>{

    List<String> selectPrivilegeByUserId(Long userId);
}
