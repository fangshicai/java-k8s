package com.fsc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fsc.model.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<String> selectPrivilegeByUserId(Long userId);
}