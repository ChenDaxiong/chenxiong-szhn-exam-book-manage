-- 用户表
CREATE TABLE IF NOT EXISTS book_user_info (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username     VARCHAR(64)  NOT NULL COMMENT '用户名',
    password     VARCHAR(128) NOT NULL COMMENT '密码',
    nickname     VARCHAR(64)  DEFAULT '' COMMENT '昵称',
    role_code    VARCHAR(32)  NOT NULL COMMENT '角色编码',
    status       INT          DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除 0未删除 1已删除',
    gmt_create   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 用户名唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_info_username ON book_user_info(username);

-- 图书分类表
CREATE TABLE IF NOT EXISTS book_category (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    category_name VARCHAR(64)  NOT NULL COMMENT '分类名称',
    sort_order    INT          DEFAULT 0 COMMENT '排序',
    parent_id     BIGINT       DEFAULT 0 COMMENT '父分类ID',
    deleted       TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除 0未删除 1已删除',
    gmt_create    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 类目名唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS idx_category_name ON book_category(category_name);


-- 图书表
CREATE TABLE IF NOT EXISTS book (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    book_name    VARCHAR(128) NOT NULL COMMENT '书名',
    author       VARCHAR(64)  DEFAULT '' COMMENT '作者',
    publisher    VARCHAR(128) DEFAULT '' COMMENT '出版社',
    category_id  BIGINT       DEFAULT NULL COMMENT '分类ID',
    stock        INT          DEFAULT 0 COMMENT '库存数量',
    status       INT          DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    version      INT          DEFAULT 0 COMMENT '乐观锁版本号',
    deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除 0未删除 1已删除',
    gmt_create   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 书名+作者 联合唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS idx_book_name_author ON book(book_name, author);


-- 图书借阅记录表
CREATE TABLE IF NOT EXISTS book_borrow_record (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id            BIGINT       NOT NULL COMMENT '借阅用户ID',
    book_id            BIGINT       NOT NULL COMMENT '图书ID',
    book_name          VARCHAR(128) DEFAULT '' COMMENT '书名',
    borrow_time        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
    plan_return_time   TIMESTAMP    NULL COMMENT '计划归还时间',
    actual_return_time TIMESTAMP    NULL COMMENT '实际归还时间',
    remark             VARCHAR(256) DEFAULT '' COMMENT '备注',
    status             INT          DEFAULT 1 COMMENT '状态：1-借阅中，0-已归还',
    deleted            TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除 0未删除 1已删除',
    gmt_create         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 用户ID索引（用于查询用户的借阅列表）
CREATE INDEX IF NOT EXISTS idx_borrow_user_id ON book_borrow_record(user_id);