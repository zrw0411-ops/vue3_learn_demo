-- OA系统数据库初始化脚本
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS oa_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE oa_db;

-- 部门表
CREATE TABLE IF NOT EXISTS department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '部门名称',
    description VARCHAR(200) COMMENT '部门描述',
    manager_id BIGINT COMMENT '部门主管ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(32) NOT NULL COMMENT '密码(MD5)',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    department_id BIGINT COMMENT '所属部门',
    role TINYINT DEFAULT 1 COMMENT '角色:1=员工,2=主管,3=HR,4=管理员',
    status TINYINT DEFAULT 1 COMMENT '状态:1=正常,0=禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES department(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 部门外键修正（manager_id自引用）
ALTER TABLE department ADD CONSTRAINT fk_dept_manager FOREIGN KEY (manager_id) REFERENCES `user`(id);

-- 请假表
CREATE TABLE IF NOT EXISTS `leave` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '申请人ID',
    user_name VARCHAR(50) COMMENT '申请人姓名(冗余)',
    department_id BIGINT COMMENT '所属部门(冗余)',
    department_name VARCHAR(50) COMMENT '部门名称(冗余)',
    leave_type TINYINT NOT NULL COMMENT '请假类型:1=事假,2=病假,3=年假,4=婚假,5=丧假',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    days INT NOT NULL COMMENT '请假天数',
    reason TEXT COMMENT '请假原因',
    status TINYINT DEFAULT 0 COMMENT '状态:0=待审批,1=主管通过,2=HR通过,-1=驳回,-2=已撤回',
    approver_id BIGINT COMMENT '当前审批人',
    approver_name VARCHAR(50) COMMENT '审批人姓名',
    approve_comment VARCHAR(200) COMMENT '审批意见',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(id),
    FOREIGN KEY (approver_id) REFERENCES `user`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假表';

-- 报销表
CREATE TABLE IF NOT EXISTS reimbursement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '申请人ID',
    user_name VARCHAR(50) COMMENT '申请人姓名(冗余)',
    department_id BIGINT COMMENT '所属部门(冗余)',
    department_name VARCHAR(50) COMMENT '部门名称(冗余)',
    title VARCHAR(100) NOT NULL COMMENT '报销标题',
    amount DECIMAL(10,2) NOT NULL COMMENT '报销金额',
    type TINYINT NOT NULL COMMENT '类型:1=差旅,2=交通,3=餐饮,4=办公,5=其他',
    description TEXT COMMENT '报销说明',
    attachment VARCHAR(500) COMMENT '发票图片路径(逗号分隔)',
    status TINYINT DEFAULT 0 COMMENT '状态:0=待审批,1=主管通过,2=财务通过,3=老板通过,-1=驳回,-2=已撤回',
    approver_id BIGINT COMMENT '当前审批人',
    approver_name VARCHAR(50) COMMENT '审批人姓名',
    approve_comment VARCHAR(200) COMMENT '审批意见',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报销表';

-- 公告表
CREATE TABLE IF NOT EXISTS notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    content TEXT COMMENT '公告内容',
    publisher_id BIGINT COMMENT '发布人ID',
    publisher_name VARCHAR(50) COMMENT '发布人姓名',
    type TINYINT DEFAULT 2 COMMENT '类型:1=系统,2=公司,3=人事',
    status TINYINT DEFAULT 1 COMMENT '状态:1=已发布,0=草稿',
    publish_time DATETIME COMMENT '发布时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (publisher_id) REFERENCES `user`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 待办事项表
CREATE TABLE IF NOT EXISTS todo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '接收人ID',
    user_name VARCHAR(50) COMMENT '接收人姓名',
    type TINYINT NOT NULL COMMENT '类型:1=请假,2=报销,3=系统',
    related_id BIGINT COMMENT '关联业务ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content VARCHAR(500) COMMENT '内容摘要',
    status TINYINT DEFAULT 0 COMMENT '状态:0=未读,1=已读,2=已处理',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='待办事项表';

-- 数据字典表
CREATE TABLE IF NOT EXISTS dictionary (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(50) NOT NULL COMMENT '字典分类',
    label VARCHAR(100) NOT NULL COMMENT '标签名',
    value VARCHAR(100) NOT NULL COMMENT '值',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态:1=启用,0=禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';

-- ==================== 初始化测试数据 ====================

-- 插入部门
INSERT INTO department (id, name, description, manager_id) VALUES
(1, '技术部', '技术研发部门', NULL),
(2, '人力资源部', 'HR和行政', NULL),
(3, '财务部', '财务和报销', NULL),
(4, '市场部', '市场推广', NULL),
(5, '总经理办公室', '公司管理层', NULL);

-- 插入用户 (密码统一: 123456 -> MD5: e10adc3949ba59abbe56e057f20f883e)
INSERT INTO `user` (id, username, password, real_name, phone, email, department_id, role, status) VALUES
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '13800000001', 'admin@oa.com', 5, 4, 1),
(2, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', '13800000002', 'zhangsan@oa.com', 1, 2, 1),
(3, 'lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', '13800000003', 'lisi@oa.com', 2, 3, 1),
(4, 'wangwu', 'e10adc3949ba59abbe56e057f20f883e', '王五', '13800000004', 'wangwu@oa.com', 3, 1, 1),
(5, 'zhaoliu', 'e10adc3949ba59abbe56e057f20f883e', '赵六', '13800000005', 'zhaoliu@oa.com', 4, 1, 1),
(6, 'sunqi', 'e10adc3949ba59abbe56e057f20f883e', '孙七', '13800000006', 'sunqi@oa.com', 1, 1, 1);

-- 更新部门主管
UPDATE department SET manager_id = 2 WHERE id = 1;
UPDATE department SET manager_id = 3 WHERE id = 2;
UPDATE department SET manager_id = 4 WHERE id = 3;
UPDATE department SET manager_id = 5 WHERE id = 4;
UPDATE department SET manager_id = 1 WHERE id = 5;

-- 插入公告
INSERT INTO notice (title, content, publisher_id, publisher_name, type, status, publish_time) VALUES
('欢迎使用OA办公系统', '欢迎各位同事使用本OA系统，请大家尽快完善个人资料，有问题联系HR。', 1, '系统管理员', 2, 1, NOW()),
('本周五下午全员会议', '本周五下午2点在会议室A召开全员大会，请准时参加。', 1, '系统管理员', 2, 1, NOW()),
('年假清零通知', '2024年度年假将于12月31日清零，请尽快使用。', 3, '李四', 3, 1, NOW());

-- 插入请假示例数据
INSERT INTO `leave` (user_id, user_name, department_id, department_name, leave_type, start_date, end_date, days, reason, status) VALUES
(6, '孙七', 1, '技术部', 1, '2024-12-20', '2024-12-22', 3, '家中有事需要处理', 0);

-- 插入报销示例数据
INSERT INTO reimbursement (user_id, user_name, department_id, department_name, title, amount, type, description, status) VALUES
(6, '孙七', 1, '技术部', '北京出差报销', 1580.00, 1, '12月10日-12日北京客户拜访差旅费', 0);

-- 插入数据字典
INSERT INTO dictionary (category, label, value, sort) VALUES
('leave_type', '事假', '1', 1),
('leave_type', '病假', '2', 2),
('leave_type', '年假', '3', 3),
('leave_type', '婚假', '4', 4),
('leave_type', '丧假', '5', 5),
('reimbursement_type', '差旅费', '1', 1),
('reimbursement_type', '交通费', '2', 2),
('reimbursement_type', '餐饮费', '3', 3),
('reimbursement_type', '办公用品', '4', 4),
('reimbursement_type', '其他', '5', 5),
('notice_type', '系统通知', '1', 1),
('notice_type', '公司公告', '2', 2),
('notice_type', '人事通知', '3', 3);

-- 插入待办事项
INSERT INTO todo (user_id, user_name, type, related_id, title, content, status) VALUES
(2, '张三', 1, 1, '请假申请待审批', '孙七提交了3天请假申请', 0),
(3, '李四', 2, 1, '报销申请待审批', '孙七提交了￥1580.00报销申请', 0);

SELECT 'OA数据库初始化完成!' AS message;
