create table `sys_user_role`
(
    `id`     bigint(20) auto_increment comment 'ID',
    `user_id` bigint(20) comment 'userId',
    `role_id` bigint(20) comment 'role_id',
    primary key (id)
) engine = innodb comment '用户信息表 与 sys_role 关联表';