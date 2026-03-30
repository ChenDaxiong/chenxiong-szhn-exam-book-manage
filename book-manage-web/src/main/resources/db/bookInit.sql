-- 初始图书分类
INSERT INTO book_category (category_name, sort_order, parent_id) VALUES ('科目01', 1, 0);
INSERT INTO book_category (category_name, sort_order, parent_id) VALUES ('科目02', 2, 0);

-- 初始图书数据
INSERT INTO book (book_name, author, publisher, category_id, stock, status)
VALUES ('图书测试名称01', '陈雄', '出版社001', 1, 10, 1);
INSERT INTO book (book_name, author, publisher, category_id, stock, status)
VALUES ('图书测试02', '陈雄', '出版社002', 2, 20, 1);
