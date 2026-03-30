package com.chenxiong.szhn.exam.book.manage.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chenxiong.szhn.exam.book.manage.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("book_user_info")
@ApiModel(description = "用户实体")
public class BookUserInfo extends BaseEntity {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;
}
