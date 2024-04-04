create table `sys_role_menu`
(
    `id`     bigint(20) auto_increment comment 'ID',
    `role_id` bigint(20) comment 'roleId',
    `menu_id` bigint(20) comment 'menu_id',
    primary key (id)
) engine = innodb comment '角色信息表 与 sys_menu 关联表';