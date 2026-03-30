package com.chenxiong.szhn.exam.book.manage.biz.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图书请求 DTO
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "图书请求参数")
public class BookDTO {

    @ApiModelProperty("图书ID")
    private Long id;

    @ApiModelProperty(value = "书名")
    private String bookName;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("出版社")
    private String publisher;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("库存数量")
    private Integer stock;

    @ApiModelProperty("状态：0-下架，1-上架")
    private Integer status;
}
