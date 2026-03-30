package com.chenxiong.szhn.exam.book.manage.biz.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书VO对象
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "图书VO对象")
public class BookVO {

    @ApiModelProperty("图书ID")
    private Long id;

    @ApiModelProperty("书名")
    private String bookName;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("出版社")
    private String publisher;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("库存数量")
    private Integer stock;

    @ApiModelProperty("状态：0-下架，1-上架")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;
}
