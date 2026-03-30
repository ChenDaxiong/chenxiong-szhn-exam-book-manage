package com.chenxiong.szhn.exam.book.manage.biz.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书分类VO对象
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "图书分类VO对象")
public class BookCategoryVO {

    @ApiModelProperty("分类ID")
    private Long id;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("排序值")
    private Integer sortOrder;

    @ApiModelProperty("父分类ID")
    private Long parentId;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;
}
