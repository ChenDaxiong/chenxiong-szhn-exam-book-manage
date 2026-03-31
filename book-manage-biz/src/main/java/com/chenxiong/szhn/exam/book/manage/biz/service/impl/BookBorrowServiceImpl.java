package com.chenxiong.szhn.exam.book.manage.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.chenxiong.szhn.exam.book.manage.biz.api.service.BookBorrowService;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BorrowRecordVO;
import com.chenxiong.szhn.exam.book.manage.biz.entity.Book;
import com.chenxiong.szhn.exam.book.manage.biz.entity.BookBorrowRecord;
import com.chenxiong.szhn.exam.book.manage.biz.manager.BookBorrowRecordManager;
import com.chenxiong.szhn.exam.book.manage.biz.manager.BookManager;
import com.chenxiong.szhn.exam.book.manage.common.annotation.ClearBookListCache;
import com.chenxiong.szhn.exam.book.manage.common.annotation.ServiceLog;
import com.chenxiong.szhn.exam.book.manage.common.enums.BorrowStatusEnum;
import com.chenxiong.szhn.exam.book.manage.common.helper.RateLimitHelper;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginContextHolder;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginUser;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;
import com.chenxiong.szhn.exam.book.manage.common.util.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图书借阅service实现
 *
 * @author chenxiong
 * @date 2026/3/31
 */
@Slf4j
@Service
public class BookBorrowServiceImpl implements BookBorrowService {

    /**
     * 限流时间毫秒
     */
    private static final long RATE_LIMIT_INTERVAL_MS = 5000L;

    /**
     * 借书限流前缀
     */
    private static final String RATE_LIMIT_BRR_PREFIX = "BRR_RATE_LIMIT_";

    /**
     * 还书限流前缀
     */
    private static final String RATE_LIMIT_RETURN_PREFIX = "RETURN_RATE_LIMIT_";

    @Resource
    private BookManager bookManager;

    @Resource
    private BookBorrowRecordManager bookBorrowRecordManager;

    @Resource
    private RateLimitHelper rateLimitHelper;

    @ServiceLog("借阅图书")
    @ClearBookListCache
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> borrowBook(Long bookId,Long userId) {
        ThrowUtil.throwIfNull(bookId, ErrorCode.PARAM_ERROR, "图书ID不能为空");
        ThrowUtil.throwIfNull(userId, ErrorCode.UNAUTHORIZED);

        // 1. 借阅限流
        checkRateLimit(RATE_LIMIT_BRR_PREFIX + userId);

        // 2. 校验是否借阅过
        long borrowingCount = bookBorrowRecordManager.lambdaQuery()
                .eq(BookBorrowRecord::getUserId, userId)
                .eq(BookBorrowRecord::getBookId, bookId)
                .eq(BookBorrowRecord::getStatus, BorrowStatusEnum.BORROWING.getCode())
                .count();
        if (borrowingCount > 0) {
            return ServiceResult.fail(ErrorCode.BOOK_ALREADY_BORROWED);
        }

        // 3. 校验图书的状态和库存
        Book book = bookManager.getById(bookId);
        ThrowUtil.throwIfNull(book, ErrorCode.BOOK_NOT_FOUND);
        ThrowUtil.throwIf(book.getStatus() != 1, ErrorCode.PARAM_ERROR, "图书已下架，无法借阅");
        ThrowUtil.throwIf(book.getStock() <= 0, ErrorCode.BOOK_OUT_OF_STOCK);

        // 4. 乐观锁扣减库存，这里使用了@version注解，实际上执行的sql会带上version+1和 where version = 当前version,不会出现超借
        book.setStock(book.getStock() - 1);
        boolean updated = bookManager.updateById(book);
        ThrowUtil.throwIf(!updated, ErrorCode.BORROW_CONCURRENT_CONFLICT, "系统异常，请稍后再试");

        // 5. 创建借阅记录
        create(userId,bookId, book.getBookName());
        log.info("Borrowed book success,userID:{}, bookId: {}, bookName: {}", userId, bookId, book.getBookName());
        return ServiceResult.success();
    }

    @ServiceLog("归还图书")
    @ClearBookListCache
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> returnBook(Long bookId,Long userId) {
        ThrowUtil.throwIfNull(bookId, ErrorCode.PARAM_ERROR, "图书ID不能为空");

        ThrowUtil.throwIfNull(userId, ErrorCode.UNAUTHORIZED);

        // 1. 还书限流
        checkRateLimit(RATE_LIMIT_RETURN_PREFIX + userId);

        // 2. 查询当前借阅记录和图书
        BookBorrowRecord record = bookBorrowRecordManager.lambdaQuery()
                .eq(BookBorrowRecord::getUserId, userId)
                .eq(BookBorrowRecord::getBookId, bookId)
                .eq(BookBorrowRecord::getStatus, BorrowStatusEnum.BORROWING.getCode())
                .one();
        ThrowUtil.throwIfNull(record, ErrorCode.BOOK_NOT_BORROWED);
        // 3. 查询图书
        Book book = bookManager.getById(bookId);
        ThrowUtil.throwIfNull(book, ErrorCode.BOOK_NOT_FOUND);

        // 4. 同借阅，基于version的乐观锁，不会出现库存数量的并发问题
        book.setStock(book.getStock() + 1);
        boolean updateSuccess = bookManager.updateById(book);
        ThrowUtil.throwIf(!updateSuccess, ErrorCode.BORROW_CONCURRENT_CONFLICT, "系统异常，请稍后再试");

        // 5. 更新借阅记录
        record.setActualReturnTime(LocalDateTime.now());
        record.setStatus(BorrowStatusEnum.RETURNED.getCode());
        bookBorrowRecordManager.updateById(record);
        log.info("Returned book success,userid:{}, bookId: {}, bookName: {}", userId, bookId, book.getBookName());
        return ServiceResult.success();
    }

    @ServiceLog("查询我的借阅列表")
    @Override
    public ServiceResult<List<BorrowRecordVO>> myBorrowList(Long userId) {
        ThrowUtil.throwIfNull(userId, ErrorCode.UNAUTHORIZED);

        List<BookBorrowRecord> records = bookBorrowRecordManager.lambdaQuery()
                .eq(BookBorrowRecord::getUserId, userId)
                .orderByDesc(BookBorrowRecord::getBorrowTime)
                .list();

        List<Long> bookIds = records.stream().map(BookBorrowRecord::getBookId).distinct().collect(Collectors.toList());
        Map<Long, Book> bookMap = bookIds.isEmpty()
                ? Map.of()
                : bookManager.listByIds(bookIds).stream().collect(Collectors.toMap(Book::getId, b -> b));

        List<BorrowRecordVO> voList = records.stream().map(r -> {
            BorrowRecordVO vo = new BorrowRecordVO();
            BeanUtil.copyProperties(r, vo);
            Book book = bookMap.get(r.getBookId());
            if (book != null) {
                vo.setBookName(book.getBookName());
                vo.setAuthor(book.getAuthor());
            }
            return vo;
        }).collect(Collectors.toList());

        return ServiceResult.success(voList);
    }


    private void checkRateLimit(String rateLimitKey) {
        ThrowUtil.throwIf(!rateLimitHelper.tryLimit(rateLimitKey,RATE_LIMIT_INTERVAL_MS)
                , ErrorCode.BORROW_RATE_LIMITED);
    }

    private void create(Long userId,Long bookId,String bookName){
        BookBorrowRecord record = new BookBorrowRecord();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setBookName(bookName);
        record.setBorrowTime(LocalDateTime.now());
        record.setPlanReturnTime(LocalDateTime.now().plusDays(30));
        record.setStatus(BorrowStatusEnum.BORROWING.getCode());
        bookBorrowRecordManager.save(record);
    }
}
