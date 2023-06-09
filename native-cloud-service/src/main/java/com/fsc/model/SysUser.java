package com.fsc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 平台用户
    */
@ApiModel(value="com-fsc-model-SysUser")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class SysUser implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 账号
     */
    @TableField(value = "username")
    @ApiModelProperty(value="账号")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value="密码")
    private String password;

    /**
     * 姓名
     */
    @TableField(value = "fullname")
    @ApiModelProperty(value="姓名")
    private String fullname;

    /**
     * 手机号
     */
    @TableField(value = "mobile")
    @ApiModelProperty(value="手机号")
    private String mobile;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
     * 状态 0-无效； 1-有效；
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态 0-无效； 1-有效；")
    private Byte status;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建人")
    private Long createBy;

    /**
     * 修改人
     */
    @TableField(value = "modify_by")
    @ApiModelProperty(value="修改人")
    private Long modifyBy;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    @ApiModelProperty(value="创建时间")
    private Date created;

    /**
     * 修改时间
     */
    @TableField(value = "last_update_time")
    @ApiModelProperty(value="修改时间")
    private Date lastUpdateTime;

    /**
     * 性别
     */
    @TableField(value = "gender")
    @ApiModelProperty(value="性别")
    private Boolean gender;

    /**
     * 出生日期
     */
    @TableField(value = "birth")
    @ApiModelProperty(value="出生日期")
    private Long birth;

    /**
     * 个人简介
     */
    @TableField(value = "introduce")
    @ApiModelProperty(value="个人简介")
    private String introduce;

    /**
     * 邀请码
     */
    @TableField(value = "invite_code")
    @ApiModelProperty(value="邀请码")
    private String inviteCode;

    /**
     * 直接邀请人ID
     */
    @TableField(value = "direct_invite_id")
    @ApiModelProperty(value="直接邀请人ID")
    private Long directInviteId;

    /**
     * 头像资源路径
     */
    @TableField(value = "image_url")
    @ApiModelProperty(value="头像资源路径")
    private String imageUrl;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USERNAME = "username";

    public static final String COL_PASSWORD = "password";

    public static final String COL_FULLNAME = "fullname";

    public static final String COL_MOBILE = "mobile";

    public static final String COL_EMAIL = "email";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_MODIFY_BY = "modify_by";

    public static final String COL_CREATED = "created";

    public static final String COL_LAST_UPDATE_TIME = "last_update_time";

    public static final String COL_GENDER = "gender";

    public static final String COL_BIRTH = "birth";

    public static final String COL_INTRODUCE = "introduce";

    public static final String COL_INVITE_CODE = "invite_code";

    public static final String COL_DIRECT_INVITE_ID = "direct_invite_id";

    public static final String COL_IMAGE_URL = "image_url";
}