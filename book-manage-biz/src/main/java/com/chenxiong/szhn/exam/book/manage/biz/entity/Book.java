package com.chenxiong.szhn.exam.book.manage.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chenxiong.szhn.exam.book.manage.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图书实体
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("book")
@ApiModel(description = "图书实体")
public class Book extends BaseEntity {

    @ApiModelProperty("书名")
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
