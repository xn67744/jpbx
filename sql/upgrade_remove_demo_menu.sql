-- ============================================================
--  JPBX 升级脚本：移除示例演示菜单
--
--  说明：
--   1. 历史版本中「实例演示」下挂载了 70 个演示页面（/demo/**），
--      这些页面与控制器已从代码库删除，此处同步清理菜单与角色关联。
--   2. 递归删除「实例演示」及其所有子菜单，避免残留空目录。
--   3. 执行前请先备份 sys_menu / sys_role_menu：
--        pg_dump -h 127.0.0.1 -U postgres -t sys_menu -t sys_role_menu youpbx > menu_backup.sql
--   表前缀 sys_ 保持不变。
-- ============================================================

BEGIN;

DO $$
DECLARE
    demo_ids int8[];
BEGIN
    WITH RECURSIVE demo_tree AS (
        SELECT menu_id
          FROM sys_menu
         WHERE url LIKE '/demo%'
            OR menu_name IN ('实例演示', '示例演示')
        UNION ALL
        SELECT m.menu_id
          FROM sys_menu m
          JOIN demo_tree d ON m.parent_id = d.menu_id
    )
    SELECT array_agg(menu_id) INTO demo_ids FROM demo_tree;

    IF demo_ids IS NOT NULL THEN
        DELETE FROM sys_role_menu WHERE menu_id = ANY (demo_ids);
        DELETE FROM sys_menu WHERE menu_id = ANY (demo_ids);
        RAISE NOTICE 'JPBX: removed % demo menu node(s)', array_length(demo_ids, 1);
    ELSE
        RAISE NOTICE 'JPBX: no demo menu found, nothing to do';
    END IF;
END $$;

COMMIT;

-- 可选：清理演示用的历史公告（按需执行）
-- DELETE FROM sys_notice WHERE notice_title LIKE '%演示%';
