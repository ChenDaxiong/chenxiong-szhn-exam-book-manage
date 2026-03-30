package com.chenxiong.szhn.exam.book.manage.member.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户管理DTO
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "用户管理请求参数")
public class UserDTO {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户角色")
    private String roleCode;

    @ApiModelProperty("状态（1-启用 0-禁用）")
    private Integer status;
}
