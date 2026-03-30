package com.chenxiong.szhn.exam.book.manage.biz.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图书查询参数 DTO
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "图书查询参数")
public class BookQueryDTO {

    @ApiModelProperty(value = "当前页", example = "1")
    private Long current = 1L;

    @ApiModelProperty(value = "每页条数", example = "10")
    private Long size = 10L;

    @ApiModelProperty("书名")
    private String bookName;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("分类ID（精确匹配）")
    private Long categoryId;
}
