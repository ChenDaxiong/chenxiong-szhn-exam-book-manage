package com.chenxiong.szhn.exam.book.manage.biz.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 图书分类请求 DTO
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "图书分类请求参数")
public class BookCategoryDTO {

    @ApiModelProperty("分类ID")
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    @ApiModelProperty(value = "分类名称", required = true)
    private String categoryName;

    @ApiModelProperty("排序")
    private Integer sortOrder;

    @ApiModelProperty("父分类ID")
    private Long parentId;
}
