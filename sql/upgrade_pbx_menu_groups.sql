-- =============================================================
-- PBX 管理菜单分组：运行查看 / 分机与账号 / 路由与网关 / 呼叫应用 / 系统设置
-- 适用：已有 JPBX 数据库；幂等，可重复执行
-- =============================================================

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
SELECT 2130, '运行查看', 2100, 1, '#', 'M', '0', '', 'fa fa-eye', 'admin', now(), '实时状态与历史记录（只读）'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2130);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
SELECT 2131, '分机与账号', 2100, 2, '#', 'M', '0', '', 'fa fa-user-circle', 'admin', now(), '分机、域名、分组与语音信箱配置'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2131);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
SELECT 2132, '路由与网关', 2100, 3, '#', 'M', '0', '', 'fa fa-sitemap', 'admin', now(), '中继、路由、拨号计划与接入控制'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2132);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
SELECT 2133, '呼叫应用', 2100, 4, '#', 'M', '0', '', 'fa fa-exchange', 'admin', now(), 'IVR、队列、坐席与会议配置'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2133);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
SELECT 2134, '系统设置', 2100, 5, '#', 'M', '0', '', 'fa fa-cogs', 'admin', now(), '平台与 FreeSWITCH 基础参数'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2134);

-- 运行查看（只读）
UPDATE sys_menu SET parent_id = 2130, order_num = 1 WHERE menu_id = 2114;
UPDATE sys_menu SET parent_id = 2130, order_num = 2 WHERE menu_id = 2116;
UPDATE sys_menu SET parent_id = 2130, order_num = 3 WHERE menu_id = 2115;
UPDATE sys_menu SET parent_id = 2130, order_num = 4 WHERE menu_id = 2117;

-- 分机与账号
UPDATE sys_menu SET parent_id = 2131, order_num = 1 WHERE menu_id = 2101;
UPDATE sys_menu SET parent_id = 2131, order_num = 2 WHERE menu_id = 2102;
UPDATE sys_menu SET parent_id = 2131, order_num = 3 WHERE menu_id = 2103;
UPDATE sys_menu SET parent_id = 2131, order_num = 4 WHERE menu_id = 2113;

-- 路由与网关
UPDATE sys_menu SET parent_id = 2132, order_num = 1 WHERE menu_id = 2104;
UPDATE sys_menu SET parent_id = 2132, order_num = 2 WHERE menu_id = 2105;
UPDATE sys_menu SET parent_id = 2132, order_num = 3 WHERE menu_id = 2106;
UPDATE sys_menu SET parent_id = 2132, order_num = 4 WHERE menu_id = 2108;

-- 呼叫应用
UPDATE sys_menu SET parent_id = 2133, order_num = 1 WHERE menu_id = 2109;
UPDATE sys_menu SET parent_id = 2133, order_num = 2 WHERE menu_id = 2110;
UPDATE sys_menu SET parent_id = 2133, order_num = 3 WHERE menu_id = 2111;
UPDATE sys_menu SET parent_id = 2133, order_num = 4 WHERE menu_id = 2112;

-- 系统设置
UPDATE sys_menu SET parent_id = 2134, order_num = 1 WHERE menu_id = 2107;
UPDATE sys_menu SET parent_id = 2134, order_num = 2 WHERE menu_id = 2120;

-- 现有角色补齐分组菜单授权
INSERT INTO sys_role_menu(role_id, menu_id)
SELECT v.role_id, g.menu_id
FROM (VALUES (1), (2)) AS v(role_id)
JOIN sys_menu g ON g.menu_id IN (2130, 2131, 2132, 2133, 2134)
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm
    WHERE rm.role_id = v.role_id AND rm.menu_id = g.menu_id
);
