package com.chenxiong.szhn.exam.book.manage.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chenxiong.szhn.exam.book.manage.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图书分类实体
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("book_category")
@ApiModel(description = "图书分类实体")
public class BookCategory extends BaseEntity {

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("排序")
    private Integer sortOrder;

    @ApiModelProperty("父分类ID")
    private Long parentId;
}
