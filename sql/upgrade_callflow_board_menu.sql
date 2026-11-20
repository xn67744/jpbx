-- =============================================================
-- 新增「呼叫流程看板」菜单（挂在 PBX管理 / 运行查看 下，幂等）
-- =============================================================
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
SELECT 2135, '呼叫流程看板', 2130, 5, '/pbx/flow', 'C', '0', 'pbx:flow:view', 'fa fa-sitemap', 'admin', now(), '号码匹配→路由→拨号计划→目的地可视化'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2135);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21351, '看板查询', 2135, 1, '#', 'F', '0', 'pbx:flow:list', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 21351);

INSERT INTO sys_role_menu(role_id, menu_id)
SELECT v.role_id, g.menu_id
FROM (VALUES (1), (2)) AS v(role_id)
JOIN sys_menu g ON g.menu_id IN (2135, 21351)
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm WHERE rm.role_id = v.role_id AND rm.menu_id = g.menu_id
);
