-- ============================================================
--  JPBX 升级脚本：清理历史品牌残留（菜单 / 公告 / 账号显示名）
--  执行前建议备份：pg_dump -t sys_menu -t sys_notice -t sys_user youpbx > brand_backup.sql
--  说明：仅修改业务数据，不改任何 sys_* 表名与结构。
-- ============================================================

BEGIN;

-- 1) 顶部「若依官网」外链改为站内运维入口
UPDATE sys_menu
   SET menu_name = '运维控制台',
       url = '/pbx/ops',
       target = '',
       icon = 'fa fa-desktop'
 WHERE url LIKE '%ruoyi%'
    OR menu_name LIKE '%若依%';

-- 2) 演示菜单兜底清理（代码中已删除 /demo/**）
DELETE FROM sys_role_menu
 WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE url LIKE '/demo%');
DELETE FROM sys_menu
 WHERE url LIKE '/demo%';

-- 3) 公告替换为平台自身说明
UPDATE sys_notice
   SET notice_title = '配置变更提醒：保存后自动热生效',
       notice_type = '2',
       notice_content = 'JPBX 在保存配置后会自动执行 reloadxml / reloadacl，常规变更无需重启 FreeSWITCH。'
 WHERE notice_id = 1;

UPDATE sys_notice
   SET notice_title = '维护通知：计划内升级请提前备份配置',
       notice_type = '1',
       notice_content = '升级前请导出数据库与 FreeSWITCH 自定义配置，升级窗口建议安排在话务低谷。'
 WHERE notice_id = 2;

UPDATE sys_notice
   SET notice_title = 'JPBX 平台介绍',
       notice_type = '1',
       notice_content = 'JPBX 是面向 FreeSWITCH 的图形化配置与运维平台，覆盖呼叫账户、网关、拨号方案、IVR、队列、会议室、语音留言与实时运维。'
 WHERE notice_id = 3;

-- 4) 账号显示名去除历史品牌字样
UPDATE sys_user SET user_name = '管理员' WHERE user_name LIKE '%若依%';
UPDATE sys_user SET email = replace(email, 'ruoyi', 'jpbx') WHERE email LIKE '%ruoyi%';

COMMIT;
