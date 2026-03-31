package com.chenxiong.szhn.exam.book.manage.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chenxiong.szhn.exam.book.manage.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 图书借阅记录实体
 *
 * @author chenxiong
 * @date 2026/3/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("book_borrow_record")
@ApiModel(description = "图书借阅记录实体")
public class BookBorrowRecord extends BaseEntity {

    @ApiModelProperty("借阅用户ID")
    private Long userId;

    @ApiModelProperty("图书ID")
    private Long bookId;

    @ApiModelProperty("书名")
    private String bookName;

    @ApiModelProperty("借阅时间")
    private LocalDateTime borrowTime;

    @ApiModelProperty("计划归还时间")
    private LocalDateTime planReturnTime;

    @ApiModelProperty("实际归还时间")
    private LocalDateTime actualReturnTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态：1-借阅中，0-已归还")
    private Integer status;
}

