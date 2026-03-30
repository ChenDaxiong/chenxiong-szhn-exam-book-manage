package com.chenxiong.szhn.exam.book.manage.member.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户查询参数DTO
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "用户查询参数")
public class UserQueryDTO {

    @ApiModelProperty(value = "当前页", example = "1")
    private Long current = 1L;

    @ApiModelProperty(value = "每页条数", example = "10")
    private Long size = 10L;

    @ApiModelProperty("用户名")
    private String username;
}
