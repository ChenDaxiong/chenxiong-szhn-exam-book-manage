package com.chenxiong.szhn.exam.book.manage.common.constant;

/**
 * 系统常量
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public final class SystemConstant {

    private SystemConstant() {
    }

    /**
     * 启用状态
     */
    public static final int STATUS_ENABLE = 1;

    /**
     * 禁用常量
     */
    public static final int STATUS_DISABLE = 0;

    /**
     * Session Cookie 名称
     */
    public static final String SESSION_COOKIE_NAME = "BOOK_SID";

    /**
     * Session Cookie 路径
     */
    public static final String SESSION_COOKIE_PATH = "/";

    /**
     * Session Redis key
     */
    public static final String SESSION_REDIS_KEY_PREFIX = "session:login:";

    /**
     * 默认库存数量
     */
    public static final int DEFAULT_STOCK = 0;

    /**
     * 默认排序值
     */
    public static final int DEFAULT_SORT_ORDER = 0;

    /**
     * 顶级分类父ID
     */
    public static final long TOP_PARENT_ID = 0L;

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final int HTTP_STATUS_OK = 200;

    public static final String DEFAULT_SUPER_ADMIN_USERNAME = "superadmin";

    public static final String DEFAULT_SUPER_ADMIN_PASSWORD = "super123";

    public static final String DEFAULT_SUPER_ADMIN_NICKNAME = "超级管理员";

    /**
     * 密码最小长度
     */
    public static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * 密码最大长度
     */
    public static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * 用户名最小长度
     */
    public static final int USER_NAME_MIN_LENGTH = 4;

    /**
     * 用户名最大长度
     */
    public static final int USER_NAME_MAX_LENGTH = 20;

    /**
     * 缓存列表key
     */
    public static final String CACHE_KEY_BOOK_LIST = "book:list:";

}
