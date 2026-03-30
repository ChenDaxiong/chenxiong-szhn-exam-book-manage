package com.chenxiong.szhn.exam.book.manage.biz.strategy;

import cn.hutool.core.util.StrUtil;
import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 图书查询策略工厂
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
@Component
public class BookQueryStrategyFactory {

    @Value("${es.enabled:false}")
    private boolean esEnabled;

    @Resource
    private DbBookQueryStrategy dbBookQueryStrategy;

    @Autowired(required = false)
    private EsBookQueryStrategy esBookQueryStrategy;

    /**
     * 根据查询参数获取合适的查询策略
     */
    public BookQueryStrategy getStrategy(BookQueryDTO queryDTO) {
        // 书名或作者有值且 ES 已启用 → ES 检索
        boolean hasTextQuery = StrUtil.isNotBlank(queryDTO.getBookName()) || StrUtil.isNotBlank(queryDTO.getAuthor());
        if (esEnabled && esBookQueryStrategy != null && hasTextQuery) {
            return esBookQueryStrategy;
        }
        // 默认走 DB 查询
        return dbBookQueryStrategy;
    }
}
