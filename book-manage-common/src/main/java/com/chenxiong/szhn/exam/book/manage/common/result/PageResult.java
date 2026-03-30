package com.chenxiong.szhn.exam.book.manage.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页result
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "分页返回结果")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页码")
    private long current;

    @ApiModelProperty("每页条数")
    private long size;

    @ApiModelProperty("总记录数")
    private long total;

    @ApiModelProperty("总页数")
    private long pages;

    @ApiModelProperty("数据列表")
    private List<T> records;


    public static <T> PageResult<T> of(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(page.getRecords());
        return result;
    }
}
