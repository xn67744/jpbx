/*
 Navicat Premium Data Transfer

 Source Server         : 124.221.186.6(亚博的云)
 Source Server Type    : PostgreSQL
 Source Server Version : 140001 (140001)
 Source Host           : 124.221.186.6:5432
 Source Catalog        : youpbx
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140001 (140001)
 File Encoding         : 65001

-- JPBX 统一初始化脚本（含系统表 + 业务表 + 菜单）

 Date: 02/02/2024 11:52:16
*/


-- ----------------------------
-- Sequence structure for gen_table_column_column_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gen_table_column_column_id_seq";
CREATE SEQUENCE "public"."gen_table_column_column_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for gen_table_table_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gen_table_table_id_seq";
CREATE SEQUENCE "public"."gen_table_table_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_config_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_config_config_id_seq";
CREATE SEQUENCE "public"."sys_config_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dept_dept_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dept_dept_id_seq";
CREATE SEQUENCE "public"."sys_dept_dept_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dict_data_dict_code_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dict_data_dict_code_seq";
CREATE SEQUENCE "public"."sys_dict_data_dict_code_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dict_type_dict_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dict_type_dict_id_seq";
CREATE SEQUENCE "public"."sys_dict_type_dict_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_job_job_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_job_job_id_seq";
CREATE SEQUENCE "public"."sys_job_job_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_job_log_job_log_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_job_log_job_log_id_seq";
CREATE SEQUENCE "public"."sys_job_log_job_log_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_logininfor_info_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_logininfor_info_id_seq";
CREATE SEQUENCE "public"."sys_logininfor_info_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_menu_menu_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_menu_menu_id_seq";
CREATE SEQUENCE "public"."sys_menu_menu_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_notice_notice_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_notice_notice_id_seq";
CREATE SEQUENCE "public"."sys_notice_notice_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_oper_log_oper_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_oper_log_oper_id_seq";
CREATE SEQUENCE "public"."sys_oper_log_oper_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_post_post_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_post_post_id_seq";
CREATE SEQUENCE "public"."sys_post_post_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_role_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_role_role_id_seq";
CREATE SEQUENCE "public"."sys_role_role_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_user_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_user_id_seq";
CREATE SEQUENCE "public"."sys_user_user_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_table";
CREATE TABLE "public"."gen_table" (
  "table_id" int8 NOT NULL DEFAULT nextval('gen_table_table_id_seq'::regclass),
  "table_name" varchar(200) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "table_comment" varchar(500) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "sub_table_name" varchar(64) COLLATE "pg_catalog"."default",
  "sub_table_fk_name" varchar(64) COLLATE "pg_catalog"."default",
  "class_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "tpl_category" varchar(200) COLLATE "pg_catalog"."default" DEFAULT 'crud'::character varying,
  "package_name" varchar(100) COLLATE "pg_catalog"."default",
  "module_name" varchar(30) COLLATE "pg_catalog"."default",
  "business_name" varchar(30) COLLATE "pg_catalog"."default",
  "function_name" varchar(50) COLLATE "pg_catalog"."default",
  "function_author" varchar(50) COLLATE "pg_catalog"."default",
  "gen_type" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "gen_path" varchar(200) COLLATE "pg_catalog"."default" DEFAULT '/'::character varying,
  "options" text COLLATE "pg_catalog"."default",
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON TABLE "public"."gen_table" IS '代码生成业务表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_table_column";
CREATE TABLE "public"."gen_table_column" (
  "column_id" int8 NOT NULL DEFAULT nextval('gen_table_column_column_id_seq'::regclass),
  "table_id" int8,
  "column_name" varchar(200) COLLATE "pg_catalog"."default",
  "column_comment" varchar(500) COLLATE "pg_catalog"."default",
  "column_type" varchar(100) COLLATE "pg_catalog"."default",
  "java_type" varchar(500) COLLATE "pg_catalog"."default",
  "java_field" varchar(200) COLLATE "pg_catalog"."default",
  "is_pk" char(1) COLLATE "pg_catalog"."default",
  "is_increment" char(1) COLLATE "pg_catalog"."default",
  "is_required" char(1) COLLATE "pg_catalog"."default",
  "is_insert" char(1) COLLATE "pg_catalog"."default",
  "is_edit" char(1) COLLATE "pg_catalog"."default",
  "is_list" char(1) COLLATE "pg_catalog"."default",
  "is_query" char(1) COLLATE "pg_catalog"."default",
  "query_type" varchar(200) COLLATE "pg_catalog"."default" DEFAULT 'EQ'::character varying,
  "html_type" varchar(200) COLLATE "pg_catalog"."default",
  "dict_type" varchar(200) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "sort" int4,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON TABLE "public"."gen_table_column" IS '代码生成业务表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_config";
CREATE TABLE "public"."sys_config" (
  "config_id" int4 NOT NULL DEFAULT nextval('sys_config_config_id_seq'::regclass),
  "config_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "config_key" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "config_value" varchar(500) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "config_type" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON TABLE "public"."sys_config" IS '参数配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO "public"."sys_config" VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2024-02-01 09:04:39.410164', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO "public"."sys_config" VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2024-02-01 09:04:39.420153', '', NULL, '初始化密码 123456');
INSERT INTO "public"."sys_config" VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2024-02-01 09:04:39.429161', '', NULL, '深黑主题theme-dark，浅色主题theme-light，深蓝主题theme-blue');
INSERT INTO "public"."sys_config" VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2024-02-01 09:04:39.43917', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO "public"."sys_config" VALUES (5, '用户管理-密码字符范围', 'sys.account.chrtype', '0', 'Y', 'admin', '2024-02-01 09:04:39.451215', '', NULL, '默认任意字符范围，0任意（密码可以输入任意字符），1数字（密码只能为0-9数字），2英文字母（密码只能为a-z和A-Z字母），3字母和数字（密码必须包含字母，数字）,4字母数字和特殊字符（目前支持的特殊字符包括：~!@#$%^&*()-=_+）');
INSERT INTO "public"."sys_config" VALUES (6, '用户管理-初始密码修改策略', 'sys.account.initPasswordModify', '1', 'Y', 'admin', '2024-02-01 09:04:39.462174', '', NULL, '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
INSERT INTO "public"."sys_config" VALUES (7, '用户管理-账号密码更新周期', 'sys.account.passwordValidateDays', '0', 'Y', 'admin', '2024-02-01 09:04:39.471169', '', NULL, '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
INSERT INTO "public"."sys_config" VALUES (8, '主框架页-菜单导航显示风格', 'sys.index.menuStyle', 'default', 'Y', 'admin', '2024-02-01 09:04:39.483168', '', NULL, '菜单导航显示风格（default为左侧导航菜单，topnav为顶部导航菜单）');
INSERT INTO "public"."sys_config" VALUES (9, '主框架页-是否开启页脚', 'sys.index.footer', 'true', 'Y', 'admin', '2024-02-01 09:04:39.498195', '', NULL, '是否开启底部页脚显示（true显示，false隐藏）');
INSERT INTO "public"."sys_config" VALUES (10, '主框架页-是否开启页签', 'sys.index.tagsView', 'true', 'Y', 'admin', '2024-02-01 09:04:39.507164', '', NULL, '是否开启菜单多页签显示（true显示，false隐藏）');
INSERT INTO "public"."sys_config" VALUES (11, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2024-02-01 09:04:39.517255', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept" (
  "dept_id" int8 NOT NULL DEFAULT nextval('sys_dept_dept_id_seq'::regclass),
  "parent_id" int8 DEFAULT 0,
  "ancestors" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "dept_name" varchar(30) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "order_num" int4 DEFAULT 0,
  "leader" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "phone" varchar(11) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "email" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "del_flag" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dept"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."sys_dept"."parent_id" IS '父部门id';
COMMENT ON COLUMN "public"."sys_dept"."ancestors" IS '祖级列表';
COMMENT ON COLUMN "public"."sys_dept"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_dept"."order_num" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_dept"."leader" IS '负责人';
COMMENT ON COLUMN "public"."sys_dept"."phone" IS '联系电话';
COMMENT ON COLUMN "public"."sys_dept"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_dept"."status" IS '部门状态(0正常 1停用)';
COMMENT ON COLUMN "public"."sys_dept"."del_flag" IS '删除标志(0代表存在 2代表删除)';
COMMENT ON COLUMN "public"."sys_dept"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dept"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dept"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dept"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_dept" IS '部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept" VALUES (100, 0, '0', 'JPBX 科技', 0, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);
INSERT INTO "public"."sys_dept" VALUES (101, 100, '0,100', '深圳总公司', 1, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);
INSERT INTO "public"."sys_dept" VALUES (102, 100, '0,100', '长沙分公司', 2, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);
INSERT INTO "public"."sys_dept" VALUES (103, 101, '0,100,101', '研发部门', 1, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);
INSERT INTO "public"."sys_dept" VALUES (104, 101, '0,100,101', '市场部门', 2, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);
INSERT INTO "public"."sys_dept" VALUES (105, 101, '0,100,101', '测试部门', 3, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);
INSERT INTO "public"."sys_dept" VALUES (106, 101, '0,100,101', '财务部门', 4, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);
INSERT INTO "public"."sys_dept" VALUES (107, 101, '0,100,101', '运维部门', 5, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);
INSERT INTO "public"."sys_dept" VALUES (108, 102, '0,100,102', '市场部门', 1, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);
INSERT INTO "public"."sys_dept" VALUES (109, 102, '0,100,102', '财务部门', 2, '管理员', '15888888888', 'admin@jpbx.local', '0', '0', 'admin', '2024-02-01 08:33:26.688292', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_data";
CREATE TABLE "public"."sys_dict_data" (
  "dict_code" int8 NOT NULL DEFAULT nextval('sys_dict_data_dict_code_seq'::regclass),
  "dict_sort" int4 DEFAULT 0,
  "dict_label" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "dict_value" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "dict_type" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "css_class" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "list_class" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "is_default" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON TABLE "public"."sys_dict_data" IS '字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO "public"."sys_dict_data" VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2024-02-01 09:04:39.527427', '', NULL, '性别男');
INSERT INTO "public"."sys_dict_data" VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-02-01 09:04:39.537184', '', NULL, '性别女');
INSERT INTO "public"."sys_dict_data" VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-02-01 09:04:39.547162', '', NULL, '性别未知');
INSERT INTO "public"."sys_dict_data" VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2024-02-01 09:04:39.559161', '', NULL, '显示菜单');
INSERT INTO "public"."sys_dict_data" VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2024-02-01 09:04:39.575145', '', NULL, '隐藏菜单');
INSERT INTO "public"."sys_dict_data" VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2024-02-01 09:04:39.584087', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data" VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2024-02-01 09:04:39.600147', '', NULL, '停用状态');
INSERT INTO "public"."sys_dict_data" VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2024-02-01 09:04:39.611374', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data" VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2024-02-01 09:04:39.621173', '', NULL, '停用状态');
INSERT INTO "public"."sys_dict_data" VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2024-02-01 09:04:39.63115', '', NULL, '默认分组');
INSERT INTO "public"."sys_dict_data" VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2024-02-01 09:04:39.663213', '', NULL, '系统分组');
INSERT INTO "public"."sys_dict_data" VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2024-02-01 09:04:39.673152', '', NULL, '系统默认是');
INSERT INTO "public"."sys_dict_data" VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2024-02-01 09:04:39.683144', '', NULL, '系统默认否');
INSERT INTO "public"."sys_dict_data" VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2024-02-01 09:04:39.692152', '', NULL, '通知');
INSERT INTO "public"."sys_dict_data" VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2024-02-01 09:04:39.702144', '', NULL, '公告');
INSERT INTO "public"."sys_dict_data" VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2024-02-01 09:04:39.71114', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data" VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2024-02-01 09:04:39.72114', '', NULL, '关闭状态');
INSERT INTO "public"."sys_dict_data" VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-02-01 09:04:39.733147', '', NULL, '其他操作');
INSERT INTO "public"."sys_dict_data" VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-02-01 09:04:39.748129', '', NULL, '新增操作');
INSERT INTO "public"."sys_dict_data" VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-02-01 09:04:39.763137', '', NULL, '修改操作');
INSERT INTO "public"."sys_dict_data" VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-02-01 09:04:39.773403', '', NULL, '删除操作');
INSERT INTO "public"."sys_dict_data" VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2024-02-01 09:04:39.78315', '', NULL, '授权操作');
INSERT INTO "public"."sys_dict_data" VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-02-01 09:04:39.792129', '', NULL, '导出操作');
INSERT INTO "public"."sys_dict_data" VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-02-01 09:04:39.809159', '', NULL, '导入操作');
INSERT INTO "public"."sys_dict_data" VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-02-01 09:04:39.819382', '', NULL, '强退操作');
INSERT INTO "public"."sys_dict_data" VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-02-01 09:04:39.828086', '', NULL, '生成操作');
INSERT INTO "public"."sys_dict_data" VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-02-01 09:04:39.836906', '', NULL, '清空操作');
INSERT INTO "public"."sys_dict_data" VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2024-02-01 09:04:39.846143', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data" VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2024-02-01 09:04:39.855385', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_type";
CREATE TABLE "public"."sys_dict_type" (
  "dict_id" int8 NOT NULL DEFAULT nextval('sys_dict_type_dict_id_seq'::regclass),
  "dict_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "dict_type" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON TABLE "public"."sys_dict_type" IS '字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO "public"."sys_dict_type" VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2024-02-01 09:04:39.865138', '', NULL, '用户性别列表');
INSERT INTO "public"."sys_dict_type" VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2024-02-01 09:04:39.875205', '', NULL, '菜单状态列表');
INSERT INTO "public"."sys_dict_type" VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2024-02-01 09:04:39.885136', '', NULL, '系统开关列表');
INSERT INTO "public"."sys_dict_type" VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2024-02-01 09:04:39.894146', '', NULL, '任务状态列表');
INSERT INTO "public"."sys_dict_type" VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2024-02-01 09:04:39.903407', '', NULL, '任务分组列表');
INSERT INTO "public"."sys_dict_type" VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2024-02-01 09:04:39.915144', '', NULL, '系统是否列表');
INSERT INTO "public"."sys_dict_type" VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2024-02-01 09:04:39.925182', '', NULL, '通知类型列表');
INSERT INTO "public"."sys_dict_type" VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2024-02-01 09:04:39.935153', '', NULL, '通知状态列表');
INSERT INTO "public"."sys_dict_type" VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2024-02-01 09:04:39.954164', '', NULL, '操作类型列表');
INSERT INTO "public"."sys_dict_type" VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2024-02-01 09:04:39.963172', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job";
CREATE TABLE "public"."sys_job" (
  "job_id" int8 NOT NULL DEFAULT nextval('sys_job_job_id_seq'::regclass),
  "job_name" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "job_group" varchar(64) COLLATE "pg_catalog"."default" DEFAULT 'DEFAULT'::character varying,
  "invoke_target" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "cron_expression" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "misfire_policy" varchar(20) COLLATE "pg_catalog"."default" DEFAULT '3'::character varying,
  "concurrent" char(1) COLLATE "pg_catalog"."default" DEFAULT '1'::bpchar,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT ''::character varying
)
;
COMMENT ON TABLE "public"."sys_job" IS '定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO "public"."sys_job" VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2024-02-01 09:04:39.992153', '', NULL, '');
INSERT INTO "public"."sys_job" VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(''ry'')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2024-02-01 09:09:41.813613', '', NULL, '');
INSERT INTO "public"."sys_job" VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(''ry'', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2024-02-01 09:09:41.825415', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job_log";
CREATE TABLE "public"."sys_job_log" (
  "job_log_id" int8 NOT NULL DEFAULT nextval('sys_job_log_job_log_id_seq'::regclass),
  "job_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "job_group" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "invoke_target" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "job_message" varchar(500) COLLATE "pg_catalog"."default",
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "exception_info" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON TABLE "public"."sys_job_log" IS '定时任务调度日志表';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_logininfor";
CREATE TABLE "public"."sys_logininfor" (
  "info_id" int8 NOT NULL DEFAULT nextval('sys_logininfor_info_id_seq'::regclass),
  "login_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "ipaddr" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "login_location" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "browser" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "os" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "msg" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "login_time" timestamp(6)
)
;
COMMENT ON TABLE "public"."sys_logininfor" IS '系统访问记录';

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO "public"."sys_logininfor" VALUES (10, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-02-01 23:58:10.927084');
INSERT INTO "public"."sys_logininfor" VALUES (11, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-02-02 02:39:36.681564');
INSERT INTO "public"."sys_logininfor" VALUES (12, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-02-02 02:41:54.156196');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu" (
  "menu_id" int8 NOT NULL DEFAULT nextval('sys_menu_menu_id_seq'::regclass),
  "menu_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "parent_id" int8 DEFAULT 0,
  "order_num" int4 DEFAULT 0,
  "url" varchar(200) COLLATE "pg_catalog"."default" DEFAULT '#'::character varying,
  "target" varchar(20) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "menu_type" char(1) COLLATE "pg_catalog"."default" DEFAULT ''::bpchar,
  "visible" int2 DEFAULT 0,
  "is_refresh" int2 DEFAULT 1,
  "perms" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "icon" varchar(100) COLLATE "pg_catalog"."default" DEFAULT '#'::character varying,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT ''::character varying
)
;
COMMENT ON COLUMN "public"."sys_menu"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "public"."sys_menu"."parent_id" IS '父菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."order_num" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_menu"."url" IS '请求地址';
COMMENT ON COLUMN "public"."sys_menu"."target" IS '打开方式(menuItem页签 menuBlank新窗口)';
COMMENT ON COLUMN "public"."sys_menu"."menu_type" IS '菜单类型(M目录 C菜单 F按钮)';
COMMENT ON COLUMN "public"."sys_menu"."visible" IS '菜单状态(0显示 1隐藏)';
COMMENT ON COLUMN "public"."sys_menu"."is_refresh" IS '是否刷新(0刷新 1不刷新)';
COMMENT ON COLUMN "public"."sys_menu"."perms" IS '权限标识';
COMMENT ON COLUMN "public"."sys_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."sys_menu"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_menu"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_menu"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_menu" IS '菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu" VALUES (1, '系统管理', 0, 1, '#', '', 'M', 0, 1, '', 'fa fa-gear', 'admin', '2024-02-01 08:38:31.814779', '', NULL, '系统管理目录');
INSERT INTO "public"."sys_menu" VALUES (2, '系统监控', 0, 2, '#', '', 'M', 0, 1, '', 'fa fa-video-camera', 'admin', '2024-02-01 08:38:31.825846', '', NULL, '系统监控目录');
INSERT INTO "public"."sys_menu" VALUES (3, '系统工具', 0, 3, '#', '', 'M', 0, 1, '', 'fa fa-bars', 'admin', '2024-02-01 08:38:31.835851', '', NULL, '系统工具目录');
INSERT INTO "public"."sys_menu" VALUES (4, '运维控制台', 0, 4, '/pbx/ops', '', 'C', 0, 1, '', 'fa fa-desktop', 'admin', '2024-02-01 08:38:31.8', '', NULL);
INSERT INTO "public"."sys_menu" VALUES (100, '用户管理', 1, 1, '/system/user', '', 'C', 0, 1, 'system:user:view', 'fa fa-user-o', 'admin', '2024-02-01 08:38:31.86086', '', NULL, '用户管理菜单');
INSERT INTO "public"."sys_menu" VALUES (101, '角色管理', 1, 2, '/system/role', '', 'C', 0, 1, 'system:role:view', 'fa fa-user-secret', 'admin', '2024-02-01 08:38:31.870864', '', NULL, '角色管理菜单');
INSERT INTO "public"."sys_menu" VALUES (102, '菜单管理', 1, 3, '/system/menu', '', 'C', 0, 1, 'system:menu:view', 'fa fa-th-list', 'admin', '2024-02-01 08:38:31.881854', '', NULL, '菜单管理菜单');
INSERT INTO "public"."sys_menu" VALUES (103, '部门管理', 1, 4, '/system/dept', '', 'C', 0, 1, 'system:dept:view', 'fa fa-outdent', 'admin', '2024-02-01 08:38:31.892862', '', NULL, '部门管理菜单');
INSERT INTO "public"."sys_menu" VALUES (104, '岗位管理', 1, 5, '/system/post', '', 'C', 0, 1, 'system:post:view', 'fa fa-address-card-o', 'admin', '2024-02-01 08:38:31.902854', '', NULL, '岗位管理菜单');
INSERT INTO "public"."sys_menu" VALUES (105, '字典管理', 1, 6, '/system/dict', '', 'C', 0, 1, 'system:dict:view', 'fa fa-bookmark-o', 'admin', '2024-02-01 08:38:31.912855', '', NULL, '字典管理菜单');
INSERT INTO "public"."sys_menu" VALUES (106, '参数设置', 1, 7, '/system/config', '', 'C', 0, 1, 'system:config:view', 'fa fa-sun-o', 'admin', '2024-02-01 08:38:31.922859', '', NULL, '参数设置菜单');
INSERT INTO "public"."sys_menu" VALUES (107, '通知公告', 1, 8, '/system/notice', '', 'C', 0, 1, 'system:notice:view', 'fa fa-bullhorn', 'admin', '2024-02-01 08:38:31.932854', '', NULL, '通知公告菜单');
INSERT INTO "public"."sys_menu" VALUES (108, '日志管理', 1, 9, '#', '', 'M', 0, 1, '', 'fa fa-pencil-square-o', 'admin', '2024-02-01 08:38:31.942844', '', NULL, '日志管理菜单');
INSERT INTO "public"."sys_menu" VALUES (109, '在线用户', 2, 1, '/monitor/online', '', 'C', 0, 1, 'monitor:online:view', 'fa fa-user-circle', 'admin', '2024-02-01 08:38:31.952847', '', NULL, '在线用户菜单');
INSERT INTO "public"."sys_menu" VALUES (110, '定时任务', 2, 2, '/monitor/job', '', 'C', 0, 1, 'monitor:job:view', 'fa fa-tasks', 'admin', '2024-02-01 08:38:31.962846', '', NULL, '定时任务菜单');
INSERT INTO "public"."sys_menu" VALUES (111, '数据监控', 2, 3, '/monitor/data', '', 'C', 0, 1, 'monitor:data:view', 'fa fa-bug', 'admin', '2024-02-01 08:38:31.973835', '', NULL, '数据监控菜单');
INSERT INTO "public"."sys_menu" VALUES (112, '服务监控', 2, 4, '/monitor/server', '', 'C', 0, 1, 'monitor:server:view', 'fa fa-server', 'admin', '2024-02-01 08:38:31.983813', '', NULL, '服务监控菜单');
INSERT INTO "public"."sys_menu" VALUES (113, '缓存监控', 2, 5, '/monitor/cache', '', 'C', 0, 1, 'monitor:cache:view', 'fa fa-cube', 'admin', '2024-02-01 08:38:31.993854', '', NULL, '缓存监控菜单');
INSERT INTO "public"."sys_menu" VALUES (114, '表单构建', 3, 1, '/tool/build', '', 'C', 0, 1, 'tool:build:view', 'fa fa-wpforms', 'admin', '2024-02-01 08:38:32.004853', '', NULL, '表单构建菜单');
INSERT INTO "public"."sys_menu" VALUES (115, '代码生成', 3, 2, '/tool/gen', '', 'C', 0, 1, 'tool:gen:view', 'fa fa-code', 'admin', '2024-02-01 08:38:32.01483', '', NULL, '代码生成菜单');
INSERT INTO "public"."sys_menu" VALUES (116, '系统接口', 3, 3, '/tool/swagger', '', 'C', 0, 1, 'tool:swagger:view', 'fa fa-gg', 'admin', '2024-02-01 08:38:32.024853', '', NULL, '系统接口菜单');
INSERT INTO "public"."sys_menu" VALUES (500, '操作日志', 108, 1, '/monitor/operlog', '', 'C', 0, 1, 'monitor:operlog:view', 'fa fa-address-book', 'admin', '2024-02-01 08:38:32.034913', '', NULL, '操作日志菜单');
INSERT INTO "public"."sys_menu" VALUES (501, '登录日志', 108, 2, '/monitor/logininfor', '', 'C', 0, 1, 'monitor:logininfor:view', 'fa fa-file-image-o', 'admin', '2024-02-01 08:38:32.044847', '', NULL, '登录日志菜单');
INSERT INTO "public"."sys_menu" VALUES (1000, '用户查询', 100, 1, '#', '', 'F', 0, 1, 'system:user:list', '#', 'admin', '2024-02-01 08:38:32.054857', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1001, '用户新增', 100, 2, '#', '', 'F', 0, 1, 'system:user:add', '#', 'admin', '2024-02-01 08:38:32.06492', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1002, '用户修改', 100, 3, '#', '', 'F', 0, 1, 'system:user:edit', '#', 'admin', '2024-02-01 08:38:32.074843', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1003, '用户删除', 100, 4, '#', '', 'F', 0, 1, 'system:user:remove', '#', 'admin', '2024-02-01 08:38:32.084861', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1004, '用户导出', 100, 5, '#', '', 'F', 0, 1, 'system:user:export', '#', 'admin', '2024-02-01 08:38:32.094857', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1005, '用户导入', 100, 6, '#', '', 'F', 0, 1, 'system:user:import', '#', 'admin', '2024-02-01 08:38:32.11288', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1006, '重置密码', 100, 7, '#', '', 'F', 0, 1, 'system:user:resetPwd', '#', 'admin', '2024-02-01 08:38:32.122847', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1007, '角色查询', 101, 1, '#', '', 'F', 0, 1, 'system:role:list', '#', 'admin', '2024-02-01 08:38:32.132858', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1008, '角色新增', 101, 2, '#', '', 'F', 0, 1, 'system:role:add', '#', 'admin', '2024-02-01 08:38:32.142838', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1009, '角色修改', 101, 3, '#', '', 'F', 0, 1, 'system:role:edit', '#', 'admin', '2024-02-01 08:38:32.152848', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1010, '角色删除', 101, 4, '#', '', 'F', 0, 1, 'system:role:remove', '#', 'admin', '2024-02-01 08:38:32.16284', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1011, '角色导出', 101, 5, '#', '', 'F', 0, 1, 'system:role:export', '#', 'admin', '2024-02-01 08:38:32.172847', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1012, '菜单查询', 102, 1, '#', '', 'F', 0, 1, 'system:menu:list', '#', 'admin', '2024-02-01 08:38:32.182841', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1013, '菜单新增', 102, 2, '#', '', 'F', 0, 1, 'system:menu:add', '#', 'admin', '2024-02-01 08:38:32.193844', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1014, '菜单修改', 102, 3, '#', '', 'F', 0, 1, 'system:menu:edit', '#', 'admin', '2024-02-01 08:38:32.203845', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1015, '菜单删除', 102, 4, '#', '', 'F', 0, 1, 'system:menu:remove', '#', 'admin', '2024-02-01 08:38:32.219886', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1016, '部门查询', 103, 1, '#', '', 'F', 0, 1, 'system:dept:list', '#', 'admin', '2024-02-01 08:38:32.232839', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1017, '部门新增', 103, 2, '#', '', 'F', 0, 1, 'system:dept:add', '#', 'admin', '2024-02-01 08:38:32.243851', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1018, '部门修改', 103, 3, '#', '', 'F', 0, 1, 'system:dept:edit', '#', 'admin', '2024-02-01 08:38:32.253827', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1019, '部门删除', 103, 4, '#', '', 'F', 0, 1, 'system:dept:remove', '#', 'admin', '2024-02-01 08:38:32.263827', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1020, '岗位查询', 104, 1, '#', '', 'F', 0, 1, 'system:post:list', '#', 'admin', '2024-02-01 08:38:32.274826', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1021, '岗位新增', 104, 2, '#', '', 'F', 0, 1, 'system:post:add', '#', 'admin', '2024-02-01 08:38:32.285828', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1022, '岗位修改', 104, 3, '#', '', 'F', 0, 1, 'system:post:edit', '#', 'admin', '2024-02-01 08:38:32.295814', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1023, '岗位删除', 104, 4, '#', '', 'F', 0, 1, 'system:post:remove', '#', 'admin', '2024-02-01 08:38:32.305826', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1024, '岗位导出', 104, 5, '#', '', 'F', 0, 1, 'system:post:export', '#', 'admin', '2024-02-01 08:38:32.315834', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1025, '字典查询', 105, 1, '#', '', 'F', 0, 1, 'system:dict:list', '#', 'admin', '2024-02-01 08:38:32.32586', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1026, '字典新增', 105, 2, '#', '', 'F', 0, 1, 'system:dict:add', '#', 'admin', '2024-02-01 08:38:32.337791', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1027, '字典修改', 105, 3, '#', '', 'F', 0, 1, 'system:dict:edit', '#', 'admin', '2024-02-01 08:38:32.347832', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1028, '字典删除', 105, 4, '#', '', 'F', 0, 1, 'system:dict:remove', '#', 'admin', '2024-02-01 08:38:32.357851', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1029, '字典导出', 105, 5, '#', '', 'F', 0, 1, 'system:dict:export', '#', 'admin', '2024-02-01 08:38:32.368793', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1030, '参数查询', 106, 1, '#', '', 'F', 0, 1, 'system:config:list', '#', 'admin', '2024-02-01 08:38:32.378851', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1031, '参数新增', 106, 2, '#', '', 'F', 0, 1, 'system:config:add', '#', 'admin', '2024-02-01 08:38:32.390849', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1032, '参数修改', 106, 3, '#', '', 'F', 0, 1, 'system:config:edit', '#', 'admin', '2024-02-01 08:38:32.400853', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1033, '参数删除', 106, 4, '#', '', 'F', 0, 1, 'system:config:remove', '#', 'admin', '2024-02-01 08:38:32.410866', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1034, '参数导出', 106, 5, '#', '', 'F', 0, 1, 'system:config:export', '#', 'admin', '2024-02-01 08:38:32.420854', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1035, '公告查询', 107, 1, '#', '', 'F', 0, 1, 'system:notice:list', '#', 'admin', '2024-02-01 08:38:32.431835', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1036, '公告新增', 107, 2, '#', '', 'F', 0, 1, 'system:notice:add', '#', 'admin', '2024-02-01 08:38:32.441856', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1037, '公告修改', 107, 3, '#', '', 'F', 0, 1, 'system:notice:edit', '#', 'admin', '2024-02-01 08:38:32.451857', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1038, '公告删除', 107, 4, '#', '', 'F', 0, 1, 'system:notice:remove', '#', 'admin', '2024-02-01 08:38:32.462844', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1039, '操作查询', 500, 1, '#', '', 'F', 0, 1, 'monitor:operlog:list', '#', 'admin', '2024-02-01 08:38:32.472844', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1040, '操作删除', 500, 2, '#', '', 'F', 0, 1, 'monitor:operlog:remove', '#', 'admin', '2024-02-01 08:38:32.483818', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1041, '详细信息', 500, 3, '#', '', 'F', 0, 1, 'monitor:operlog:detail', '#', 'admin', '2024-02-01 08:38:32.494821', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1042, '日志导出', 500, 4, '#', '', 'F', 0, 1, 'monitor:operlog:export', '#', 'admin', '2024-02-01 08:38:32.504832', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1043, '登录查询', 501, 1, '#', '', 'F', 0, 1, 'monitor:logininfor:list', '#', 'admin', '2024-02-01 08:38:32.514833', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1044, '登录删除', 501, 2, '#', '', 'F', 0, 1, 'monitor:logininfor:remove', '#', 'admin', '2024-02-01 08:38:32.526854', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1045, '日志导出', 501, 3, '#', '', 'F', 0, 1, 'monitor:logininfor:export', '#', 'admin', '2024-02-01 08:38:32.536831', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1046, '账户解锁', 501, 4, '#', '', 'F', 0, 1, 'monitor:logininfor:unlock', '#', 'admin', '2024-02-01 08:38:32.546838', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1047, '在线查询', 109, 1, '#', '', 'F', 0, 1, 'monitor:online:list', '#', 'admin', '2024-02-01 08:38:32.558849', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1048, '批量强退', 109, 2, '#', '', 'F', 0, 1, 'monitor:online:batchForceLogout', '#', 'admin', '2024-02-01 08:38:32.568839', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1049, '单条强退', 109, 3, '#', '', 'F', 0, 1, 'monitor:online:forceLogout', '#', 'admin', '2024-02-01 08:38:32.588872', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1050, '任务查询', 110, 1, '#', '', 'F', 0, 1, 'monitor:job:list', '#', 'admin', '2024-02-01 08:38:32.598852', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1051, '任务新增', 110, 2, '#', '', 'F', 0, 1, 'monitor:job:add', '#', 'admin', '2024-02-01 08:38:32.612845', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1052, '任务修改', 110, 3, '#', '', 'F', 0, 1, 'monitor:job:edit', '#', 'admin', '2024-02-01 08:38:32.623847', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1053, '任务删除', 110, 4, '#', '', 'F', 0, 1, 'monitor:job:remove', '#', 'admin', '2024-02-01 08:38:32.633846', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1054, '状态修改', 110, 5, '#', '', 'F', 0, 1, 'monitor:job:changeStatus', '#', 'admin', '2024-02-01 08:38:32.651859', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1055, '任务详细', 110, 6, '#', '', 'F', 0, 1, 'monitor:job:detail', '#', 'admin', '2024-02-01 08:38:32.66184', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1056, '任务导出', 110, 7, '#', '', 'F', 0, 1, 'monitor:job:export', '#', 'admin', '2024-02-01 08:38:32.671852', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1057, '生成查询', 115, 1, '#', '', 'F', 0, 1, 'tool:gen:list', '#', 'admin', '2024-02-01 08:38:32.681947', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1058, '生成修改', 115, 2, '#', '', 'F', 0, 1, 'tool:gen:edit', '#', 'admin', '2024-02-01 08:38:32.693841', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1059, '生成删除', 115, 3, '#', '', 'F', 0, 1, 'tool:gen:remove', '#', 'admin', '2024-02-01 08:38:32.704891', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1060, '预览代码', 115, 4, '#', '', 'F', 0, 1, 'tool:gen:preview', '#', 'admin', '2024-02-01 08:38:32.716844', '', NULL, '');
INSERT INTO "public"."sys_menu" VALUES (1061, '生成代码', 115, 5, '#', '', 'F', 0, 1, 'tool:gen:code', '#', 'admin', '2024-02-01 08:38:32.736859', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_notice";
CREATE TABLE "public"."sys_notice" (
  "notice_id" int4 NOT NULL DEFAULT nextval('sys_notice_notice_id_seq'::regclass),
  "notice_title" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "notice_type" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "notice_content" text COLLATE "pg_catalog"."default",
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON TABLE "public"."sys_notice" IS '通知公告表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO "public"."sys_notice" VALUES (1, '配置变更提醒：保存后自动热生效', '2', 'JPBX 在保存配置后会自动执行 reloadxml / reloadacl，常规变更无需重启 FreeSWITCH。', '0', 'admin', '2024-02-01 09:03:15.332133', '', NULL, '管理员');
INSERT INTO "public"."sys_notice" VALUES (2, '维护通知：计划内升级请提前备份配置', '1', '升级前请导出数据库与 /usr/local/freeswitch/conf 下的自定义配置，升级窗口建议安排在话务低谷。', '0', 'admin', '2024-02-01 09:03:15.342273', '', NULL, '管理员');
INSERT INTO "public"."sys_notice" VALUES (3, 'JPBX 平台介绍', '1', '<p>JPBX 是面向 FreeSWITCH 的图形化配置与运维平台，覆盖呼叫账户、网关、拨号方案、IVR、队列、会议室、语音留言与实时运维。</p>', '0', 'admin', '2024-02-01 09:03:15.352157', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_oper_log";
CREATE TABLE "public"."sys_oper_log" (
  "oper_id" int8 NOT NULL DEFAULT nextval('sys_oper_log_oper_id_seq'::regclass),
  "title" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "business_type" int4 DEFAULT 0,
  "method" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "request_method" varchar(10) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "operator_type" int4 DEFAULT 0,
  "oper_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "dept_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_url" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_ip" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_location" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "oper_param" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "json_result" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "status" int4 DEFAULT 0,
  "error_msg" text COLLATE "pg_catalog"."default" DEFAULT ''::text,
  "oper_time" timestamp(6),
  "cost_time" int8 DEFAULT 0
)
;
COMMENT ON TABLE "public"."sys_oper_log" IS '操作日志记录';

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO "public"."sys_oper_log" VALUES (1, '重置密码', 2, 'com.av.web.controller.system.SysProfileController.resetPwd()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/resetPwd', '127.0.0.1', '内网IP', '{"userId":["1"],"loginName":["admin"]}', '{"msg":"操作成功","code":0}', 0, NULL, '2024-02-01 18:20:13.103852', 93);
INSERT INTO "public"."sys_oper_log" VALUES (2, '角色管理', 2, 'com.av.web.controller.system.SysRoleController.changeStatus()', 'POST', 1, 'admin', '研发部门', '/system/role/changeStatus', '127.0.0.1', '内网IP', '{"roleId":["2"],"status":["1"]}', '{"msg":"操作成功","code":0}', 0, NULL, '2024-02-01 19:14:26.191785', 292);
INSERT INTO "public"."sys_oper_log" VALUES (3, '角色管理', 2, 'com.av.web.controller.system.SysRoleController.changeStatus()', 'POST', 1, 'admin', '研发部门', '/system/role/changeStatus', '127.0.0.1', '内网IP', '{"roleId":["2"],"status":["0"]}', '{"msg":"操作成功","code":0}', 0, NULL, '2024-02-01 19:14:28.927463', 15);
INSERT INTO "public"."sys_oper_log" VALUES (4, '角色管理', 5, 'com.av.web.controller.system.SysRoleController.export()', 'POST', 1, 'admin', '研发部门', '/system/role/export', '127.0.0.1', '内网IP', '{"roleName":[""],"roleKey":[""],"status":[""],"params[beginTime]":[""],"params[endTime]":[""],"orderByColumn":["roleSort"],"isAsc":["asc"]}', '{"msg":"186a4d58-65a7-43c2-bbb1-01c6ad7c57cd_角色数据.xlsx","code":0}', 0, NULL, '2024-02-01 19:14:38.212781', 1638);
INSERT INTO "public"."sys_oper_log" VALUES (5, '登录日志', 9, 'com.av.web.controller.monitor.SysLogininforController.clean()', 'POST', 1, 'admin', '研发部门', '/monitor/logininfor/clean', '127.0.0.1', '内网IP', '', '{"msg":"操作成功","code":0}', 0, NULL, '2024-02-01 19:21:41.048325', 25);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_post";
CREATE TABLE "public"."sys_post" (
  "post_id" int8 NOT NULL DEFAULT nextval('sys_post_post_id_seq'::regclass),
  "post_code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "post_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "post_sort" int4 NOT NULL,
  "status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_post"."post_id" IS '岗位ID';
COMMENT ON COLUMN "public"."sys_post"."post_code" IS '岗位编码';
COMMENT ON COLUMN "public"."sys_post"."post_name" IS '岗位名称';
COMMENT ON COLUMN "public"."sys_post"."post_sort" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_post"."status" IS '状态(0正常 1停用)';
COMMENT ON COLUMN "public"."sys_post"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_post"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_post"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_post"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_post"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_post" IS '岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO "public"."sys_post" VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2024-02-01 08:33:52.341817', '', NULL, '');
INSERT INTO "public"."sys_post" VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2024-02-01 08:33:52.341817', '', NULL, '');
INSERT INTO "public"."sys_post" VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2024-02-01 08:33:52.341817', '', NULL, '');
INSERT INTO "public"."sys_post" VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2024-02-01 08:33:52.341817', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
  "role_id" int8 NOT NULL DEFAULT nextval('sys_role_role_id_seq'::regclass),
  "role_name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "role_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "role_sort" int4 NOT NULL,
  "data_scope" char(1) COLLATE "pg_catalog"."default" DEFAULT '1'::bpchar,
  "status" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "del_flag" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_role"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "public"."sys_role"."role_key" IS '角色权限字符串';
COMMENT ON COLUMN "public"."sys_role"."role_sort" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_role"."data_scope" IS '数据范围(1:全部数据权限 2:自定数据权限 3:本部门数据权限 4:本部门及以下数据权限)';
COMMENT ON COLUMN "public"."sys_role"."status" IS '角色状态(0正常 1停用)';
COMMENT ON COLUMN "public"."sys_role"."del_flag" IS '删除标志(0代表存在 2代表删除)';
COMMENT ON COLUMN "public"."sys_role"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_role"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_role"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_role" IS '角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" VALUES (1, '超级管理员', 'admin', 1, '1', '0', '0', 'admin', '2024-02-01 08:33:52.537953', '', NULL, '超级管理员');
INSERT INTO "public"."sys_role" VALUES (2, '普通角色', 'common', 2, '2', '0', '0', 'admin', '2024-02-01 08:33:52.537953', '', '2024-02-01 19:14:28.889505', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_dept";
CREATE TABLE "public"."sys_role_dept" (
  "role_id" int8 NOT NULL,
  "dept_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_role_dept"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_dept"."dept_id" IS '部门ID';
COMMENT ON TABLE "public"."sys_role_dept" IS '角色和部门关联表';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO "public"."sys_role_dept" VALUES (2, 100);
INSERT INTO "public"."sys_role_dept" VALUES (2, 101);
INSERT INTO "public"."sys_role_dept" VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_menu";
CREATE TABLE "public"."sys_role_menu" (
  "role_id" int8 NOT NULL,
  "menu_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_role_menu"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_menu"."menu_id" IS '菜单ID';
COMMENT ON TABLE "public"."sys_role_menu" IS '角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO "public"."sys_role_menu" VALUES (2, 1);
INSERT INTO "public"."sys_role_menu" VALUES (2, 2);
INSERT INTO "public"."sys_role_menu" VALUES (2, 3);
INSERT INTO "public"."sys_role_menu" VALUES (2, 4);
INSERT INTO "public"."sys_role_menu" VALUES (2, 100);
INSERT INTO "public"."sys_role_menu" VALUES (2, 101);
INSERT INTO "public"."sys_role_menu" VALUES (2, 102);
INSERT INTO "public"."sys_role_menu" VALUES (2, 103);
INSERT INTO "public"."sys_role_menu" VALUES (2, 104);
INSERT INTO "public"."sys_role_menu" VALUES (2, 105);
INSERT INTO "public"."sys_role_menu" VALUES (2, 106);
INSERT INTO "public"."sys_role_menu" VALUES (2, 107);
INSERT INTO "public"."sys_role_menu" VALUES (2, 108);
INSERT INTO "public"."sys_role_menu" VALUES (2, 109);
INSERT INTO "public"."sys_role_menu" VALUES (2, 110);
INSERT INTO "public"."sys_role_menu" VALUES (2, 111);
INSERT INTO "public"."sys_role_menu" VALUES (2, 112);
INSERT INTO "public"."sys_role_menu" VALUES (2, 113);
INSERT INTO "public"."sys_role_menu" VALUES (2, 114);
INSERT INTO "public"."sys_role_menu" VALUES (2, 115);
INSERT INTO "public"."sys_role_menu" VALUES (2, 116);
INSERT INTO "public"."sys_role_menu" VALUES (2, 500);
INSERT INTO "public"."sys_role_menu" VALUES (2, 501);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1000);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1001);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1002);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1003);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1004);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1005);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1006);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1007);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1008);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1009);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1010);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1011);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1012);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1013);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1014);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1015);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1016);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1017);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1018);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1019);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1020);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1021);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1022);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1023);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1024);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1025);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1026);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1027);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1028);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1029);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1030);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1031);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1032);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1033);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1034);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1035);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1036);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1037);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1038);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1039);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1040);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1041);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1042);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1043);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1044);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1045);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1046);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1047);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1048);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1049);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1050);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1051);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1052);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1053);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1054);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1055);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1056);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1057);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1058);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1059);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1060);
INSERT INTO "public"."sys_role_menu" VALUES (2, 1061);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
  "user_id" int8 NOT NULL DEFAULT nextval('sys_user_user_id_seq'::regclass),
  "dept_id" int8,
  "login_name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_name" varchar(30) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "user_type" varchar(2) COLLATE "pg_catalog"."default" DEFAULT '00'::character varying,
  "email" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "phonenumber" varchar(11) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "sex" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "avatar" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "password" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "salt" varchar(20) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "del_flag" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "login_ip" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "login_date" timestamp(6),
  "pwd_update_date" timestamp(6),
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."sys_user"."login_name" IS '登录账号';
COMMENT ON COLUMN "public"."sys_user"."user_name" IS '用户昵称';
COMMENT ON COLUMN "public"."sys_user"."user_type" IS '用户类型(00系统用户 01注册用户)';
COMMENT ON COLUMN "public"."sys_user"."email" IS '用户邮箱';
COMMENT ON COLUMN "public"."sys_user"."phonenumber" IS '手机号码';
COMMENT ON COLUMN "public"."sys_user"."sex" IS '用户性别(0男 1女 2未知)';
COMMENT ON COLUMN "public"."sys_user"."avatar" IS '头像路径';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."salt" IS '盐加密';
COMMENT ON COLUMN "public"."sys_user"."status" IS '帐号状态(0正常 1停用)';
COMMENT ON COLUMN "public"."sys_user"."del_flag" IS '删除标志(0代表存在 2代表删除)';
COMMENT ON COLUMN "public"."sys_user"."login_ip" IS '最后登录IP';
COMMENT ON COLUMN "public"."sys_user"."login_date" IS '最后登录时间';
COMMENT ON COLUMN "public"."sys_user"."pwd_update_date" IS '密码最后更新时间';
COMMENT ON COLUMN "public"."sys_user"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_user" IS '用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES (2, 105, 'ry', '管理员', '00', 'ry@qq.com', '15666666666', '1', '', '8e6d98b90472783cc73c17047ddccf36', '222222', '0', '0', '127.0.0.1', NULL, NULL, 'admin', '2024-02-01 08:33:27.003334', '', NULL, '测试员');
INSERT INTO "public"."sys_user" VALUES (1, 103, 'admin', '管理员', '00', 'ry@163.com', '15888888888', '1', '', 'b23677ba8f76398a7dcbb7d00810d6eb', 'f4df7e', '0', '0', '127.0.0.1', '2024-02-02 02:41:54.737', '2024-02-01 18:20:13.685', 'admin', '2024-02-01 08:33:27.003334', '', '2024-02-02 02:41:54.125061', '管理员');

-- ----------------------------
-- Table structure for sys_user_online
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_online";
CREATE TABLE "public"."sys_user_online" (
  "sessionid" varchar(50) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "login_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "dept_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "ipaddr" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "login_location" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "browser" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "os" varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" varchar(10) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "start_timestamp" timestamp(6),
  "last_access_time" timestamp(6),
  "expire_time" int8 DEFAULT 0
)
;
COMMENT ON COLUMN "public"."sys_user_online"."sessionid" IS '用户会话id';
COMMENT ON COLUMN "public"."sys_user_online"."login_name" IS '登录账号';
COMMENT ON COLUMN "public"."sys_user_online"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_user_online"."ipaddr" IS '登录IP地址';
COMMENT ON COLUMN "public"."sys_user_online"."login_location" IS '登录地点';
COMMENT ON COLUMN "public"."sys_user_online"."browser" IS '浏览器类型';
COMMENT ON COLUMN "public"."sys_user_online"."os" IS '操作系统';
COMMENT ON COLUMN "public"."sys_user_online"."status" IS '在线状态on_line在线off_line离线';
COMMENT ON COLUMN "public"."sys_user_online"."start_timestamp" IS 'session创建时间';
COMMENT ON COLUMN "public"."sys_user_online"."last_access_time" IS 'session最后访问时间';
COMMENT ON COLUMN "public"."sys_user_online"."expire_time" IS '超时时间，单位为分钟';

-- ----------------------------
-- Records of sys_user_online
-- ----------------------------
INSERT INTO "public"."sys_user_online" VALUES ('8ae939b1-1021-4c01-ad08-1d87bbbde3b1', 'admin', '研发部门', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', 'on_line', '2024-02-02 02:39:16.988', '2024-02-02 02:43:00.704', 1800000);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_post";
CREATE TABLE "public"."sys_user_post" (
  "user_id" int8 NOT NULL,
  "post_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_post"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_post"."post_id" IS '岗位ID';
COMMENT ON TABLE "public"."sys_user_post" IS '用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO "public"."sys_user_post" VALUES (1, 1);
INSERT INTO "public"."sys_user_post" VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role";
CREATE TABLE "public"."sys_user_role" (
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_role"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."sys_user_role" IS '用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "public"."sys_user_role" VALUES (1, 1);
INSERT INTO "public"."sys_user_role" VALUES (2, 2);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."gen_table_column_column_id_seq"
OWNED BY "public"."gen_table_column"."column_id";
SELECT setval('"public"."gen_table_column_column_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."gen_table_table_id_seq"
OWNED BY "public"."gen_table"."table_id";
SELECT setval('"public"."gen_table_table_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_config_config_id_seq"
OWNED BY "public"."sys_config"."config_id";
SELECT setval('"public"."sys_config_config_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dept_dept_id_seq"
OWNED BY "public"."sys_dept"."dept_id";
SELECT setval('"public"."sys_dept_dept_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dict_data_dict_code_seq"
OWNED BY "public"."sys_dict_data"."dict_code";
SELECT setval('"public"."sys_dict_data_dict_code_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dict_type_dict_id_seq"
OWNED BY "public"."sys_dict_type"."dict_id";
SELECT setval('"public"."sys_dict_type_dict_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_job_job_id_seq"
OWNED BY "public"."sys_job"."job_id";
SELECT setval('"public"."sys_job_job_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_job_log_job_log_id_seq"
OWNED BY "public"."sys_job_log"."job_log_id";
SELECT setval('"public"."sys_job_log_job_log_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_logininfor_info_id_seq"
OWNED BY "public"."sys_logininfor"."info_id";
SELECT setval('"public"."sys_logininfor_info_id_seq"', 12, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_menu_menu_id_seq"
OWNED BY "public"."sys_menu"."menu_id";
SELECT setval('"public"."sys_menu_menu_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_notice_notice_id_seq"
OWNED BY "public"."sys_notice"."notice_id";
SELECT setval('"public"."sys_notice_notice_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_oper_log_oper_id_seq"
OWNED BY "public"."sys_oper_log"."oper_id";
SELECT setval('"public"."sys_oper_log_oper_id_seq"', 5, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_post_post_id_seq"
OWNED BY "public"."sys_post"."post_id";
SELECT setval('"public"."sys_post_post_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_role_role_id_seq"
OWNED BY "public"."sys_role"."role_id";
SELECT setval('"public"."sys_role_role_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_user_user_id_seq"
OWNED BY "public"."sys_user"."user_id";
SELECT setval('"public"."sys_user_user_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table gen_table
-- ----------------------------
ALTER TABLE "public"."gen_table" ADD CONSTRAINT "gen_table_pkey" PRIMARY KEY ("table_id");

-- ----------------------------
-- Primary Key structure for table gen_table_column
-- ----------------------------
ALTER TABLE "public"."gen_table_column" ADD CONSTRAINT "gen_table_column_pkey" PRIMARY KEY ("column_id");

-- ----------------------------
-- Primary Key structure for table sys_config
-- ----------------------------
ALTER TABLE "public"."sys_config" ADD CONSTRAINT "sys_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept" ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("dept_id");

-- ----------------------------
-- Primary Key structure for table sys_dict_data
-- ----------------------------
ALTER TABLE "public"."sys_dict_data" ADD CONSTRAINT "sys_dict_data_pkey" PRIMARY KEY ("dict_code");

-- ----------------------------
-- Uniques structure for table sys_dict_type
-- ----------------------------
ALTER TABLE "public"."sys_dict_type" ADD CONSTRAINT "sys_dict_type_dict_type_key" UNIQUE ("dict_type");

-- ----------------------------
-- Primary Key structure for table sys_dict_type
-- ----------------------------
ALTER TABLE "public"."sys_dict_type" ADD CONSTRAINT "sys_dict_type_pkey" PRIMARY KEY ("dict_id");

-- ----------------------------
-- Primary Key structure for table sys_job
-- ----------------------------
ALTER TABLE "public"."sys_job" ADD CONSTRAINT "sys_job_pkey" PRIMARY KEY ("job_id");

-- ----------------------------
-- Primary Key structure for table sys_job_log
-- ----------------------------
ALTER TABLE "public"."sys_job_log" ADD CONSTRAINT "sys_job_log_pkey" PRIMARY KEY ("job_log_id");

-- ----------------------------
-- Primary Key structure for table sys_logininfor
-- ----------------------------
ALTER TABLE "public"."sys_logininfor" ADD CONSTRAINT "sys_logininfor_pkey" PRIMARY KEY ("info_id");

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu" ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("menu_id");

-- ----------------------------
-- Primary Key structure for table sys_notice
-- ----------------------------
ALTER TABLE "public"."sys_notice" ADD CONSTRAINT "sys_notice_pkey" PRIMARY KEY ("notice_id");

-- ----------------------------
-- Primary Key structure for table sys_oper_log
-- ----------------------------
ALTER TABLE "public"."sys_oper_log" ADD CONSTRAINT "sys_oper_log_pkey" PRIMARY KEY ("oper_id");

-- ----------------------------
-- Primary Key structure for table sys_post
-- ----------------------------
ALTER TABLE "public"."sys_post" ADD CONSTRAINT "sys_post_pkey" PRIMARY KEY ("post_id");

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("role_id");

-- ----------------------------
-- Primary Key structure for table sys_role_dept
-- ----------------------------
ALTER TABLE "public"."sys_role_dept" ADD CONSTRAINT "sys_role_dept_pkey" PRIMARY KEY ("role_id", "dept_id");

-- ----------------------------
-- Primary Key structure for table sys_role_menu
-- ----------------------------
ALTER TABLE "public"."sys_role_menu" ADD CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("role_id", "menu_id");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("user_id");

-- ----------------------------
-- Primary Key structure for table sys_user_online
-- ----------------------------
ALTER TABLE "public"."sys_user_online" ADD CONSTRAINT "sys_user_online_pkey" PRIMARY KEY ("sessionid");

-- ----------------------------
-- Primary Key structure for table sys_user_post
-- ----------------------------
ALTER TABLE "public"."sys_user_post" ADD CONSTRAINT "sys_user_post_pkey" PRIMARY KEY ("user_id", "post_id");

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role" ADD CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("user_id", "role_id");

-- =============================================================================
-- Part 2: PBX 业务表结构（原 pbx_schema.sql）
-- =============================================================================

-- JPBX FreeSWITCH 1.11 业务表结构 (PostgreSQL)
-- 在 youpbx 库执行

-- 系统配置
CREATE TABLE IF NOT EXISTS t_pbx_sysconf (
    id                varchar(64) PRIMARY KEY,
    local_ip          varchar(64),
    remote_ip         varchar(64),
    external_port     varchar(16),
    internal_port     varchar(16),
    start_rtp_port    varchar(16),
    end_rtp_port      varchar(16),
    audio_code        varchar(128),
    video_code        varchar(128),
    frame_rate        varchar(32),
    bit_rate          bigint,
    ice_start         boolean DEFAULT false,
    stun_address      varchar(128),
    audio_record      boolean DEFAULT true,
    video_record      boolean DEFAULT false,
    audio_record_path varchar(256),
    video_record_path varchar(256),
    sound_file_path   varchar(256),
    freeswitch_path   varchar(256),
    freeswitch_log_path varchar(256),
    ws_port           varchar(16),
    wss_port          varchar(16),
    cdr_db_info       varchar(512),
    remark            varchar(500)
);

-- SIP 域名
CREATE TABLE IF NOT EXISTS t_pbx_domain (
    id          varchar(64) PRIMARY KEY,
    name        varchar(128) NOT NULL UNIQUE,
    parse       smallint DEFAULT 1,
    alias       smallint DEFAULT 0,
    enabled     smallint DEFAULT 1,
    remark      varchar(500)
);

-- 分机
CREATE TABLE IF NOT EXISTS t_pbx_user (
    id          varchar(64) PRIMARY KEY,
    name        varchar(128),
    number      varchar(64) NOT NULL,
    domain      varchar(128),
    password    varchar(128),
    type        int DEFAULT 0,
    video       varchar(8) DEFAULT '1',
    audio       varchar(8) DEFAULT '0',
    level       int DEFAULT 0,
    vm_password varchar(64),
    toll_allow  varchar(128) DEFAULT 'domestic,international,local',
    callgroup   varchar(64),
    user_context varchar(64) DEFAULT 'default',
    enabled     smallint DEFAULT 1,
    remark      varchar(500)
);
CREATE INDEX IF NOT EXISTS idx_pbx_user_number ON t_pbx_user(number);
CREATE INDEX IF NOT EXISTS idx_pbx_user_domain ON t_pbx_user(domain);

-- 分组
CREATE TABLE IF NOT EXISTS t_pbx_group (
    id          varchar(64) PRIMARY KEY,
    name        varchar(128) NOT NULL,
    domain      varchar(128),
    remark      varchar(500)
);

CREATE TABLE IF NOT EXISTS t_pbx_group_user (
    id          varchar(64) PRIMARY KEY,
    group_id    varchar(64) NOT NULL,
    user_id     varchar(64) NOT NULL,
    user_number varchar(64)
);
CREATE INDEX IF NOT EXISTS idx_pbx_group_user_gid ON t_pbx_group_user(group_id);

-- 网关
CREATE TABLE IF NOT EXISTS t_pbx_gateway (
    id            varchar(64) PRIMARY KEY,
    name          varchar(128),
    realm         varchar(256),
    register      int DEFAULT 0,
    transport     varchar(16) DEFAULT 'udp',
    retry_seconds int DEFAULT 30,
    username      varchar(128),
    password      varchar(128),
    type          int DEFAULT 0,
    profile       varchar(64) DEFAULT 'external',
    proxy         varchar(256),
    from_user     varchar(128),
    from_domain   varchar(128),
    expire_seconds int DEFAULT 600,
    enabled       smallint DEFAULT 1,
    remark        varchar(500)
);

-- 旧版路由（保留兼容，同时用于生成 dialplan）
CREATE TABLE IF NOT EXISTS t_pbx_route (
    id                varchar(64) PRIMARY KEY,
    reg               varchar(256),
    type              bigint,
    inbound_model     bigint,
    gateway_id        varchar(512),
    gateway_name      varchar(512),
    caller_pre        varchar(64),
    caller_pre_remove bigint,
    callee_pre        varchar(64),
    callee_pre_remove bigint,
    did               varchar(64),
    callee_check      varchar(64),
    transform         varchar(128),
    name              varchar(128),
    destination_type  varchar(32),
    destination_value varchar(128),
    context           varchar(64) DEFAULT 'default',
    enabled           smallint DEFAULT 1,
    sort_order        int DEFAULT 100,
    remark            varchar(500)
);

-- 老库升级：DID 路由的目的地（extension/ivr/queue/conference/voicemail/gateway/app）
ALTER TABLE t_pbx_route ADD COLUMN IF NOT EXISTS destination_type  varchar(32);
ALTER TABLE t_pbx_route ADD COLUMN IF NOT EXISTS destination_value varchar(128);

CREATE TABLE IF NOT EXISTS t_pbx_change_number (
    id              varchar(64) PRIMARY KEY,
    type            int,
    caller_reg      varchar(256),
    changed_caller  varchar(128),
    callee_reg      varchar(256),
    changed_callee  varchar(128),
    "order"         int,
    caller          varchar(128),
    route_id        varchar(64)
);

-- 标准拨号计划
CREATE TABLE IF NOT EXISTS t_pbx_dialplan (
    id              varchar(64) PRIMARY KEY,
    name            varchar(128) NOT NULL,
    context         varchar(64) NOT NULL DEFAULT 'default',
    condition_field varchar(64) DEFAULT 'destination_number',
    condition_expr  varchar(256) NOT NULL,
    continue_flag   smallint DEFAULT 0,
    enabled         smallint DEFAULT 1,
    sort_order      int DEFAULT 100,
    remark          varchar(500)
);

CREATE TABLE IF NOT EXISTS t_pbx_dialplan_action (
    id              varchar(64) PRIMARY KEY,
    dialplan_id     varchar(64) NOT NULL,
    application     varchar(64) NOT NULL,
    data            varchar(512),
    inline_flag     smallint DEFAULT 0,
    sort_order      int DEFAULT 10
);
CREATE INDEX IF NOT EXISTS idx_pbx_dp_action_dp ON t_pbx_dialplan_action(dialplan_id);

-- ACL
CREATE TABLE IF NOT EXISTS t_pbx_acl_list (
    id          varchar(64) PRIMARY KEY,
    name        varchar(128) NOT NULL UNIQUE,
    default_type varchar(16) DEFAULT 'deny',
    remark      varchar(500)
);

CREATE TABLE IF NOT EXISTS t_pbx_acl_node (
    id          varchar(64) PRIMARY KEY,
    list_id     varchar(64) NOT NULL,
    cidr        varchar(64) NOT NULL,
    node_type   varchar(16) DEFAULT 'allow',
    domain      varchar(128),
    sort_order  int DEFAULT 10
);

-- IVR
CREATE TABLE IF NOT EXISTS t_pbx_ivr (
    id              varchar(64) PRIMARY KEY,
    name            varchar(128) NOT NULL UNIQUE,
    greet_long      varchar(256),
    greet_short     varchar(256),
    invalid_sound   varchar(256),
    exit_sound      varchar(256),
    timeout         int DEFAULT 10000,
    max_failures    int DEFAULT 3,
    max_timeouts    int DEFAULT 3,
    digit_len       int DEFAULT 1,
    inter_digit     int DEFAULT 2000,
    enabled         smallint DEFAULT 1,
    remark          varchar(500)
);

CREATE TABLE IF NOT EXISTS t_pbx_ivr_entry (
    id          varchar(64) PRIMARY KEY,
    ivr_id      varchar(64) NOT NULL,
    digits      varchar(32) NOT NULL,
    action      varchar(64) NOT NULL,
    param       varchar(256),
    sort_order  int DEFAULT 10
);

-- Callcenter
CREATE TABLE IF NOT EXISTS t_pbx_queue (
    id                  varchar(64) PRIMARY KEY,
    name                varchar(128) NOT NULL UNIQUE,
    strategy            varchar(64) DEFAULT 'longest-idle-agent',
    moh_sound           varchar(256),
    time_base_score     varchar(32) DEFAULT 'system',
    max_wait_time       int DEFAULT 0,
    max_wait_time_with_no_agent int DEFAULT 0,
    tier_rules_apply    smallint DEFAULT 0,
    discard_abandoned_after int DEFAULT 60,
    abandoned_resume_allowed smallint DEFAULT 0,
    enabled             smallint DEFAULT 1,
    remark              varchar(500)
);

CREATE TABLE IF NOT EXISTS t_pbx_agent (
    id              varchar(64) PRIMARY KEY,
    name            varchar(128) NOT NULL UNIQUE,
    contact         varchar(256) NOT NULL,
    status          varchar(32) DEFAULT 'Logged Out',
    type            varchar(32) DEFAULT 'callback',
    max_no_answer   int DEFAULT 3,
    wrap_up_time    int DEFAULT 10,
    reject_delay_time int DEFAULT 10,
    busy_delay_time int DEFAULT 60,
    enabled         smallint DEFAULT 1,
    remark          varchar(500)
);

CREATE TABLE IF NOT EXISTS t_pbx_tier (
    id          varchar(64) PRIMARY KEY,
    queue_name  varchar(128) NOT NULL,
    agent_name  varchar(128) NOT NULL,
    level       int DEFAULT 1,
    position    int DEFAULT 1
);

-- Conference
CREATE TABLE IF NOT EXISTS t_pbx_conference (
    id              varchar(64) PRIMARY KEY,
    name            varchar(128) NOT NULL UNIQUE,
    profile         varchar(64) DEFAULT 'default',
    pin             varchar(32),
    max_members     int DEFAULT 50,
    record          smallint DEFAULT 0,
    enabled         smallint DEFAULT 1,
    remark          varchar(500)
);

CREATE TABLE IF NOT EXISTS t_pbx_conference_profile (
    id          varchar(64) PRIMARY KEY,
    name        varchar(128) NOT NULL UNIQUE,
    rate        int DEFAULT 8000,
    interval    int DEFAULT 20,
    energy_level int DEFAULT 300,
    caller_controls varchar(64) DEFAULT 'default',
    params_json text,
    remark      varchar(500)
);

-- Voicemail 元数据（留言文件列表）
CREATE TABLE IF NOT EXISTS t_pbx_voicemail (
    id          varchar(64) PRIMARY KEY,
    user_number varchar(64),
    domain      varchar(128),
    file_path   varchar(512),
    caller      varchar(64),
    created_at  timestamp DEFAULT CURRENT_TIMESTAMP,
    read_flag   smallint DEFAULT 0,
    duration    int DEFAULT 0
);

-- CDR（与 mod_cdr_pg_csv 对齐，若已存在可跳过）
CREATE TABLE IF NOT EXISTS cdr (
    local_ip_v4         varchar(64),
    caller_id_name      varchar(128),
    caller_id_number    varchar(64),
    destination_number  varchar(64),
    context             varchar(64),
    start_stamp         timestamp,
    answer_stamp        timestamp,
    end_stamp           timestamp,
    duration            int,
    billsec             int,
    hangup_cause        varchar(64),
    uuid                varchar(64),
    bleg_uuid           varchar(64),
    accountcode         varchar(64),
    read_codec          varchar(32),
    write_codec         varchar(32),
    ani                 varchar(64)
);

-- 兼容旧表增加字段（已存在时忽略错误可手工执行）
ALTER TABLE t_pbx_user ADD COLUMN IF NOT EXISTS vm_password varchar(64);
ALTER TABLE t_pbx_user ADD COLUMN IF NOT EXISTS toll_allow varchar(128) DEFAULT 'domestic,international,local';
ALTER TABLE t_pbx_user ADD COLUMN IF NOT EXISTS callgroup varchar(64);
ALTER TABLE t_pbx_user ADD COLUMN IF NOT EXISTS user_context varchar(64) DEFAULT 'default';
ALTER TABLE t_pbx_user ADD COLUMN IF NOT EXISTS enabled smallint DEFAULT 1;
ALTER TABLE t_pbx_gateway ADD COLUMN IF NOT EXISTS profile varchar(64) DEFAULT 'external';
ALTER TABLE t_pbx_gateway ADD COLUMN IF NOT EXISTS proxy varchar(256);
ALTER TABLE t_pbx_gateway ADD COLUMN IF NOT EXISTS from_user varchar(128);
ALTER TABLE t_pbx_gateway ADD COLUMN IF NOT EXISTS from_domain varchar(128);
ALTER TABLE t_pbx_gateway ADD COLUMN IF NOT EXISTS expire_seconds int DEFAULT 600;
ALTER TABLE t_pbx_gateway ADD COLUMN IF NOT EXISTS enabled smallint DEFAULT 1;
ALTER TABLE t_pbx_route ADD COLUMN IF NOT EXISTS context varchar(64) DEFAULT 'default';
ALTER TABLE t_pbx_route ADD COLUMN IF NOT EXISTS enabled smallint DEFAULT 1;
ALTER TABLE t_pbx_route ADD COLUMN IF NOT EXISTS sort_order int DEFAULT 100;
ALTER TABLE t_pbx_sysconf ADD COLUMN IF NOT EXISTS cdr_db_info varchar(512);

-- FreeSWITCH 配置参数（按段+键存储，不直接改 XML 文件）
CREATE TABLE IF NOT EXISTS t_pbx_fs_param (
    id          varchar(64) PRIMARY KEY,
    section     varchar(128) NOT NULL,
    param_key   varchar(128) NOT NULL,
    param_value text,
    UNIQUE(section, param_key)
);
CREATE INDEX IF NOT EXISTS idx_pbx_fs_param_section ON t_pbx_fs_param(section);


-- =============================================================================
-- Part 3: PBX 菜单与权限（原 pbx_menu.sql）
-- =============================================================================

-- JPBX 菜单与权限（PostgreSQL / JPBX sys_menu）
-- 请按实际菜单 ID 规则调整；以下使用 2100 段避免与默认菜单冲突

-- 一级：PBX
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
SELECT 2100, 'PBX管理', 0, 5, '#', 'M', '0', '', 'fa fa-phone', 'admin', now(), 'FreeSWITCH'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2100);

-- 分机
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2101, '分机管理', 2100, 1, '/pbx/user', 'C', '0', 'pbx:user:view', 'fa fa-user', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2101);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21011, '分机查询', 2101, 1, '#', 'F', '0', 'pbx:user:list', 'admin', now() WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id=21011);
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21012, '分机新增', 2101, 2, '#', 'F', '0', 'pbx:user:add', 'admin', now() WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id=21012);
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21013, '分机修改', 2101, 3, '#', 'F', '0', 'pbx:user:edit', 'admin', now() WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id=21013);
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21014, '分机删除', 2101, 4, '#', 'F', '0', 'pbx:user:remove', 'admin', now() WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id=21014);

-- 域名 / 分组 / 网关 / 路由 / 拨号计划 / 系统 / ACL / IVR / 队列 / 坐席 / 会议 / 留言 / 运维 / 录音 / CDR / 日志
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2102, 'SIP域名', 2100, 2, '/pbx/domain', 'C', '0', 'pbx:domain:view', 'fa fa-globe', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2102);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2103, '分机组', 2100, 3, '/pbx/group', 'C', '0', 'pbx:group:view', 'fa fa-users', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2103);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2104, '中继网关', 2100, 4, '/pbx/gateway', 'C', '0', 'pbx:gateway:view', 'fa fa-sitemap', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2104);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2105, '路由管理', 2100, 5, '/pbx/route', 'C', '0', 'pbx:route:view', 'fa fa-share-alt', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2105);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2106, '拨号计划', 2100, 6, '/pbx/dialplan', 'C', '0', 'pbx:dialplan:view', 'fa fa-list-alt', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2106);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2107, '系统配置', 2100, 7, '/pbx/sysconf', 'C', '0', 'pbx:sysconf:view', 'fa fa-cogs', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2107);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2120, 'FS参数配置', 2100, 7, '/pbx/fsconf', 'C', '0', 'pbx:fsconf:view', 'fa fa-sliders', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2120);
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21201, 'FS参数查询', 2120, 1, '#', 'F', '0', 'pbx:fsconf:list', 'admin', now() WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id=21201);
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21202, 'FS参数编辑', 2120, 2, '#', 'F', '0', 'pbx:fsconf:edit', 'admin', now() WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id=21202);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2108, 'ACL', 2100, 8, '/pbx/acl', 'C', '0', 'pbx:acl:view', 'fa fa-shield', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2108);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2109, 'IVR菜单', 2100, 9, '/pbx/ivr', 'C', '0', 'pbx:ivr:view', 'fa fa-keyboard-o', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2109);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2110, '呼叫队列', 2100, 10, '/pbx/queue', 'C', '0', 'pbx:queue:view', 'fa fa-headphones', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2110);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2111, '坐席管理', 2100, 11, '/pbx/agent', 'C', '0', 'pbx:agent:view', 'fa fa-headset', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2111);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2112, '会议室', 2100, 12, '/pbx/conference', 'C', '0', 'pbx:conference:view', 'fa fa-comments', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2112);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2113, '语音留言', 2100, 13, '/pbx/voicemail', 'C', '0', 'pbx:voicemail:view', 'fa fa-envelope', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2113);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2114, '实时运维', 2100, 14, '/pbx/ops', 'C', '0', 'pbx:ops:view', 'fa fa-dashboard', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2114);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2115, '录音管理', 2100, 15, '/pbx/record', 'C', '0', 'pbx:record:view', 'fa fa-file-audio-o', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2115);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2116, '通话记录', 2100, 16, '/pbx/call/view', 'C', '0', 'pbx:call:view', 'fa fa-history', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2116);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time)
SELECT 2117, 'FS日志', 2100, 17, '/pbx/log/view', 'C', '0', 'pbx:log:view', 'fa fa-file-text', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2117);

-- 通用按钮权限（为新菜单补充 list/add/edit/remove）
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21141, '运维查询', 2114, 1, '#', 'F', '0', 'pbx:ops:list', 'admin', now() WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id=21141);
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21142, '运维操作', 2114, 2, '#', 'F', '0', 'pbx:ops:edit', 'admin', now() WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id=21142);

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21151, '录音查询', 2115, 1, '#', 'F', '0', 'pbx:record:list', 'admin', now() WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id=21151);

-- =============================================================
-- PBX 菜单分组：运行查看（只读） / 分机与账号 / 路由与网关 / 呼叫应用 / 系统设置
-- ID 2130-2134 为分组菜单，叶子菜单 parent_id 指向对应分组
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

UPDATE sys_menu SET parent_id = 2130, order_num = 1 WHERE menu_id = 2114;
UPDATE sys_menu SET parent_id = 2130, order_num = 2 WHERE menu_id = 2116;
UPDATE sys_menu SET parent_id = 2130, order_num = 3 WHERE menu_id = 2115;
UPDATE sys_menu SET parent_id = 2130, order_num = 4 WHERE menu_id = 2117;
UPDATE sys_menu SET parent_id = 2131, order_num = 1 WHERE menu_id = 2101;
UPDATE sys_menu SET parent_id = 2131, order_num = 2 WHERE menu_id = 2102;
UPDATE sys_menu SET parent_id = 2131, order_num = 3 WHERE menu_id = 2103;
UPDATE sys_menu SET parent_id = 2131, order_num = 4 WHERE menu_id = 2113;
UPDATE sys_menu SET parent_id = 2132, order_num = 1 WHERE menu_id = 2104;
UPDATE sys_menu SET parent_id = 2132, order_num = 2 WHERE menu_id = 2105;
UPDATE sys_menu SET parent_id = 2132, order_num = 3 WHERE menu_id = 2106;
UPDATE sys_menu SET parent_id = 2132, order_num = 4 WHERE menu_id = 2108;
UPDATE sys_menu SET parent_id = 2133, order_num = 1 WHERE menu_id = 2109;
UPDATE sys_menu SET parent_id = 2133, order_num = 2 WHERE menu_id = 2110;
UPDATE sys_menu SET parent_id = 2133, order_num = 3 WHERE menu_id = 2111;
UPDATE sys_menu SET parent_id = 2133, order_num = 4 WHERE menu_id = 2112;
UPDATE sys_menu SET parent_id = 2134, order_num = 1 WHERE menu_id = 2107;
UPDATE sys_menu SET parent_id = 2134, order_num = 2 WHERE menu_id = 2120;

-- 呼叫流程看板（运行查看分组下）
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, remark)
SELECT 2135, '呼叫流程看板', 2130, 5, '/pbx/flow', 'C', '0', 'pbx:flow:view', 'fa fa-sitemap', 'admin', now(), '号码匹配→路由→拨号计划→目的地可视化'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2135);
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, menu_type, visible, perms, create_by, create_time)
SELECT 21351, '看板查询', 2135, 1, '#', 'F', '0', 'pbx:flow:list', 'admin', now()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 21351);

-- 授权：超级管理员角色（role_id=1）拥有全部菜单与按钮权限
-- 注意：JPBX的 *:*:* 万能权限只对 user_id=1 生效，其他账号即使拥有该角色，
--       权限也来自 sys_role_menu，因此必须全量授权，否则会出现「有角色但缺菜单/按钮」。
INSERT INTO sys_role_menu(role_id, menu_id)
SELECT 1, m.menu_id FROM sys_menu m
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu rm WHERE rm.role_id=1 AND rm.menu_id=m.menu_id);

-- 授权：把 PBX 整棵菜单树（含各页面下的按钮权限）授予普通角色（role_id=2）
-- 按需调整 role_id，或按同样写法再给其他角色授权
WITH RECURSIVE pbx_menu AS (
    SELECT menu_id FROM sys_menu WHERE menu_id = 2100
    UNION ALL
    SELECT m.menu_id FROM sys_menu m
    JOIN pbx_menu p ON m.parent_id = p.menu_id
)
INSERT INTO sys_role_menu(role_id, menu_id)
SELECT 2, p.menu_id FROM pbx_menu p
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu rm WHERE rm.role_id=2 AND rm.menu_id=p.menu_id);

-- 演示账号 ry（user_id=2）加入超级管理员角色，与 admin 拥有相同的菜单与权限
INSERT INTO sys_user_role(user_id, role_id)
SELECT 2, 1
WHERE EXISTS (SELECT 1 FROM sys_user WHERE user_id = 2)
  AND NOT EXISTS (SELECT 1 FROM sys_user_role WHERE user_id = 2 AND role_id = 1);

-- ----------------------------
-- 字典：呼叫账户类型（pbx_account_type）
-- 页面 templates/pbx/user/add.html、edit.html、user.html 依赖该字典渲染「账号类型」，
-- 字典缺失时下拉为空、新增账户 type 写为 NULL，帐户不会下发到 FreeSWITCH directory。
-- 需要更多类型时，在「系统管理 -> 字典管理 -> 呼叫账户类型」里追加即可。
-- ----------------------------
INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_id), 0) + 1 FROM sys_dict_type), '呼叫账户类型', 'pbx_account_type', '0', 'admin', now(), '呼叫账户管理-账号类型'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'pbx_account_type');

INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 1, '分机', '0', 'pbx_account_type', '', 'primary', 'Y', '0', 'admin', now(), '注册到 FreeSWITCH 的普通分机'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_account_type' AND dict_value = '0');

-- ----------------------------
-- 字典：中继类型 / 中继传输类型 / 路由类型 / 呼入模式
-- 对应页面 templates/pbx/gateway/*.html、templates/pbx/route/*.html 的下拉框，
-- 缺失时下拉为空、字段以 NULL 入库，FreeSWITCH 侧 sofia/dialplan 会出现空参数。
-- ----------------------------
INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_id), 0) + 1 FROM sys_dict_type), '中继类型', 'pbx_gateway_type', '0', 'admin', now(), '呼叫中继-中继类型'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'pbx_gateway_type');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 1, '对等中继（不注册）', '0', 'pbx_gateway_type', '', 'primary', 'Y', '0', 'admin', now(), 'register=false'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_gateway_type' AND dict_value = '0');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 2, '注册中继（向对端注册）', '1', 'pbx_gateway_type', '', 'success', 'N', '0', 'admin', now(), 'register=true，需填写账户/密码'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_gateway_type' AND dict_value = '1');

INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_id), 0) + 1 FROM sys_dict_type), '中继传输类型', 'pbx_gateway_translate_type', '0', 'admin', now(), '呼叫中继-传输类型(sofia register-transport)'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'pbx_gateway_translate_type');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 1, 'UDP', 'udp', 'pbx_gateway_translate_type', '', 'primary', 'Y', '0', 'admin', now(), NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_gateway_translate_type' AND dict_value = 'udp');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 2, 'TCP', 'tcp', 'pbx_gateway_translate_type', '', 'info', 'N', '0', 'admin', now(), NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_gateway_translate_type' AND dict_value = 'tcp');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 3, 'TLS', 'tls', 'pbx_gateway_translate_type', '', 'warning', 'N', '0', 'admin', now(), NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_gateway_translate_type' AND dict_value = 'tls');

INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_id), 0) + 1 FROM sys_dict_type), '路由类型', 'pbx_route_type', '0', 'admin', now(), '路由管理-路由类型'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'pbx_route_type');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 1, '呼出（走中继）', '1', 'pbx_route_type', '', 'primary', 'Y', '0', 'admin', now(), 'context=default，按匹配前缀出局'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_route_type' AND dict_value = '1');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 2, '呼入（中继接入）', '2', 'pbx_route_type', '', 'success', 'N', '0', 'admin', now(), 'context=public，按 DID 匹配'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_route_type' AND dict_value = '2');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 3, '内部分机（bridge user）', '0', 'pbx_route_type', '', 'info', 'N', '0', 'admin', now(), 'context=default，直接 bridge 本地分机'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_route_type' AND dict_value = '0');

INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_id), 0) + 1 FROM sys_dict_type), '呼入模式', 'pbx_route_inbound_model', '0', 'admin', now(), '路由管理-呼入模式'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'pbx_route_inbound_model');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 1, '普通呼入（按被叫号码）', '1', 'pbx_route_inbound_model', '', 'primary', 'Y', '0', 'admin', now(), NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_route_inbound_model' AND dict_value = '1');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT (SELECT COALESCE(MAX(dict_code), 0) + 1 FROM sys_dict_data), 2, 'DID 呼入（按 DID 号码）', '2', 'pbx_route_inbound_model', '', 'success', 'N', '0', 'admin', now(), '需要填写 DID 号码'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'pbx_route_inbound_model' AND dict_value = '2');
