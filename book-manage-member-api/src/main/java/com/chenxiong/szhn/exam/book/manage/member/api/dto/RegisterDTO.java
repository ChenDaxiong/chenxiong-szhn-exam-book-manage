package com.chenxiong.szhn.exam.book.manage.member.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册请求DTO
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "用户注册请求参数")
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true, example = "陈雄")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true, example = "password123")
    private String password;

    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(value = "昵称", required = true, example = "陈雄")
    private String nickname;
}
