-- PBX 模块按钮权限补齐
-- 背景：sys_menu 里只配了 pbx:xxx:view，Controller 的 /list、/add、/edit、/remove 分别要求
--       pbx:xxx:list / add / edit / remove，导致非 admin 角色即使勾了菜单也会 403。
-- 说明：只插入缺失的菜单项（按 perms 去重），可重复执行。
--       menu_id 沿用现有编号规则：父菜单 2101~2117，按钮为 parent*10 + 序号（1查询 2新增 3修改 4删除 5导出）。

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
SELECT v.menu_id, v.menu_name, v.parent_id, v.order_num, '#', '', 'F', 0, 1, v.perms, '#', 'admin', now(), ''
FROM (VALUES
    -- 分机管理 2101
    (21015::bigint, '分机导出', 2101::bigint, 5, 'pbx:user:export'),
    -- SIP域名 2102
    (21021, '域名查询', 2102, 1, 'pbx:domain:list'),
    (21022, '域名新增', 2102, 2, 'pbx:domain:add'),
    (21023, '域名修改', 2102, 3, 'pbx:domain:edit'),
    (21024, '域名删除', 2102, 4, 'pbx:domain:remove'),
    (21025, '域名导出', 2102, 5, 'pbx:domain:export'),
    -- 分机组 2103
    (21031, '分组查询', 2103, 1, 'pbx:group:list'),
    (21032, '分组新增', 2103, 2, 'pbx:group:add'),
    (21033, '分组修改', 2103, 3, 'pbx:group:edit'),
    (21034, '分组删除', 2103, 4, 'pbx:group:remove'),
    (21035, '分组导出', 2103, 5, 'pbx:group:export'),
    -- 网关 2104
    (21041, '网关查询', 2104, 1, 'pbx:gateway:list'),
    (21042, '网关新增', 2104, 2, 'pbx:gateway:add'),
    (21043, '网关修改', 2104, 3, 'pbx:gateway:edit'),
    (21044, '网关删除', 2104, 4, 'pbx:gateway:remove'),
    (21045, '网关导出', 2104, 5, 'pbx:gateway:export'),
    -- 路由 2105（无新增接口）
    (21051, '路由查询', 2105, 1, 'pbx:route:list'),
    (21053, '路由修改', 2105, 3, 'pbx:route:edit'),
    (21054, '路由删除', 2105, 4, 'pbx:route:remove'),
    (21055, '路由导出', 2105, 5, 'pbx:route:export'),
    -- 拨号方案 2106
    (21061, '方案查询', 2106, 1, 'pbx:dialplan:list'),
    (21062, '方案新增', 2106, 2, 'pbx:dialplan:add'),
    (21063, '方案修改', 2106, 3, 'pbx:dialplan:edit'),
    (21064, '方案删除', 2106, 4, 'pbx:dialplan:remove'),
    (21065, '方案导出', 2106, 5, 'pbx:dialplan:export'),
    -- 服务配置 2107
    (21071, '配置查询', 2107, 1, 'pbx:sysconf:list'),
    (21072, '配置新增', 2107, 2, 'pbx:sysconf:add'),
    (21073, '配置修改', 2107, 3, 'pbx:sysconf:edit'),
    (21074, '配置删除', 2107, 4, 'pbx:sysconf:remove'),
    (21075, '配置导出', 2107, 5, 'pbx:sysconf:export'),
    -- ACL 2108
    (21081, 'ACL查询', 2108, 1, 'pbx:acl:list'),
    (21082, 'ACL新增', 2108, 2, 'pbx:acl:add'),
    (21083, 'ACL修改', 2108, 3, 'pbx:acl:edit'),
    (21084, 'ACL删除', 2108, 4, 'pbx:acl:remove'),
    (21085, 'ACL导出', 2108, 5, 'pbx:acl:export'),
    -- IVR 2109
    (21091, 'IVR查询', 2109, 1, 'pbx:ivr:list'),
    (21092, 'IVR新增', 2109, 2, 'pbx:ivr:add'),
    (21093, 'IVR修改', 2109, 3, 'pbx:ivr:edit'),
    (21094, 'IVR删除', 2109, 4, 'pbx:ivr:remove'),
    (21095, 'IVR导出', 2109, 5, 'pbx:ivr:export'),
    -- 呼叫队列 2110
    (21101, '队列查询', 2110, 1, 'pbx:queue:list'),
    (21102, '队列新增', 2110, 2, 'pbx:queue:add'),
    (21103, '队列修改', 2110, 3, 'pbx:queue:edit'),
    (21104, '队列删除', 2110, 4, 'pbx:queue:remove'),
    (21105, '队列导出', 2110, 5, 'pbx:queue:export'),
    -- 坐席 2111
    (21111, '坐席查询', 2111, 1, 'pbx:agent:list'),
    (21112, '坐席新增', 2111, 2, 'pbx:agent:add'),
    (21113, '坐席修改', 2111, 3, 'pbx:agent:edit'),
    (21114, '坐席删除', 2111, 4, 'pbx:agent:remove'),
    (21115, '坐席导出', 2111, 5, 'pbx:agent:export'),
    -- 会议室 2112
    (21121, '会议室查询', 2112, 1, 'pbx:conference:list'),
    (21122, '会议室新增', 2112, 2, 'pbx:conference:add'),
    (21123, '会议室修改', 2112, 3, 'pbx:conference:edit'),
    (21124, '会议室删除', 2112, 4, 'pbx:conference:remove'),
    (21125, '会议室导出', 2112, 5, 'pbx:conference:export'),
    -- 语音信箱 2113
    (21131, '留言查询', 2113, 1, 'pbx:voicemail:list'),
    (21132, '留言新增', 2113, 2, 'pbx:voicemail:add'),
    (21133, '留言修改', 2113, 3, 'pbx:voicemail:edit'),
    (21134, '留言删除', 2113, 4, 'pbx:voicemail:remove'),
    (21135, '留言导出', 2113, 5, 'pbx:voicemail:export'),
    -- 通话记录 2115（仅缺删除）
    (21154, '记录删除', 2115, 4, 'pbx:record:remove'),
    -- 通话记录/呼叫 2116
    (21161, '呼叫查询', 2116, 1, 'pbx:call:list'),
    (21165, '呼叫导出', 2116, 5, 'pbx:call:export')
) AS v(menu_id, menu_name, parent_id, order_num, perms)
WHERE NOT EXISTS (SELECT 1 FROM sys_menu m WHERE m.perms = v.perms);
