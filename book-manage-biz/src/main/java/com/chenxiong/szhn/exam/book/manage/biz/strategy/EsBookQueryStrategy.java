package com.chenxiong.szhn.exam.book.manage.biz.strategy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookQueryDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BookVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Elasticsearch 时间问题待完整实现，请面试官忽略方法内部实现，目前部署默认都走db，不支持模糊查询
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
@Component("esBookQueryStrategy")
@ConditionalOnProperty(name = "es.enabled", havingValue = "true")
public class EsBookQueryStrategy implements BookQueryStrategy {


    @Override
    public IPage<BookVO> queryBooks(BookQueryDTO queryDTO) {
        // ============ES相关时间问题使用伪代码实现，体现设计思路，真实生产环境模糊检索不能走DB todo chenxiong==========
        // 1. 构建ES搜索参数
        // 2. 执行ES的search
        // 3. 解析并转换搜索结果返回
        return null;
    }
}
