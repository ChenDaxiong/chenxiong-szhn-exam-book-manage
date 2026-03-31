package com.chenxiong.szhn.exam.book.manage.biz.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author chenxiong
 * @date 2026/3/31
 */
@Data
@ApiModel(description = "借阅记录vo")
public class BorrowRecordVO {

    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("图书ID")
    private Long bookId;

    @ApiModelProperty("书名")
    private String bookName;

    @ApiModelProperty("作者")
    private String author;

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

