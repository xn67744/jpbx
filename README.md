<p align="center">
  <img src="av-admin/src/main/resources/static/img/logo/jpbx-logo-192.png" width="88" alt="JPBX"/>
</p>

<h1 align="center">JPBX · FreeSWITCH 管理平台</h1>

<p align="center">基于 mod_xml_curl 动态下发配置 + ESL 热生效的 FreeSWITCH 图形化配置台<br>JDK 21 · Spring Boot 2.5.15 · Shiro 1.13 · PostgreSQL</p>

---

## 说明文档入口

所有详细说明都在 `doc/` 目录：

| 文档 | 路径 | 适合谁看 | 写什么 |
| --- | --- | --- | --- |
| **配置使用手册** | [doc/JPBX配置使用手册.md](doc/JPBX配置使用手册.md) | 实施 / 运维 / 管理员 | 逐模块、逐字段说明配置方法、取值含义、影响范围与验证方式 |
| 配置使用手册（Word） | [doc/JPBX配置使用手册.docx](doc/JPBX配置使用手册.docx) | 交付 / 存档 | 与 Markdown 同源的 Word 版 |
| **项目说明** | [doc/项目说明.md](doc/项目说明.md) | 产品 / 设计 / 开发 / 实施 | 功能模块、菜单、FS 参数页、校验、开发约定、表结构、FAQ |
| **架构设计** | [doc/架构设计.md](doc/架构设计.md) | 架构 / 后端开发 | 模块划分、xml_curl、ESL 热生效、包结构、时序、扩展点 |
| **FreeSWITCH 对接与安装** | [doc/FreeSWITCH对接与安装说明.md](doc/FreeSWITCH对接与安装说明.md) | 运维 / 实施 | 下载地址、编译安装、xml_curl/ESL 对接、热生效、联调清单 |
| **功能测试报告** | [doc/功能测试报告.md](doc/功能测试报告.md) | 验收 / 测试 | 18 个模块的用例表、执行结果、截图与结论 |
| **功能测试报告（Word）** | [doc/功能测试报告.docx](doc/功能测试报告.docx) | 交付 / 验收 | 与 Markdown 同源的交付版 |
| 数据库脚本 | [sql/pbx_pgsql.sql](sql/pbx_pgsql.sql) | 实施 / DBA | 系统表 + PBX 业务表 + 菜单（一份执行） |
| 菜单清理脚本 | [sql/upgrade_remove_demo_menu.sql](sql/upgrade_remove_demo_menu.sql) | 升级 | 移除历史演示菜单与角色关联 |
| 菜单分组脚本 | [sql/upgrade_pbx_menu_groups.sql](sql/upgrade_pbx_menu_groups.sql) | 升级 | 将 PBX 管理拆为「运行查看 / 分机与账号 / 路由与网关 / 呼叫应用 / 系统设置」 |
| 流程看板菜单 | [sql/upgrade_callflow_board_menu.sql](sql/upgrade_callflow_board_menu.sql) | 升级 | 在运行查看下新增「呼叫流程看板」菜单与权限 |

**建议阅读顺序：** README 快速上手 → 装 FS（对接文档）→ 按功能查配置（配置使用手册）→ 功能/改页面（项目说明）→ 改架构（架构设计）。

---

## 核心设计原则

1. **不直接改 FS 磁盘上的 XML 文件**
   管理台维护「配置段 + 参数名/参数值」或业务实体，`mod_xml_curl` 向 JPBX 拉取 XML。
2. **保存即可用（热生效）**
   业务保存后由 `FsApplyService` 调用 ESL（`reloadxml` / `sofia profile …` / `reloadacl` / `reload mod_xxx`），常规变更无需重启 FreeSWITCH。
3. **通话由 FreeSWITCH dialplan 执行**
   路由逻辑在 dialplan / 路由配置中，不用 ESL `originate` 充当拨号计划。

---

## 功能一览

| 模块 | 说明 |
| --- | --- |
| SIP 域名 / 分机 / 分机组 | directory 动态下发 |
| 中继网关 / Sofia | sofia 配置 + killgw/rescan |
| 系统配置 | IP、端口、编解码、录音路径等 |
| **FS 参数配置** | 参数名/值编辑，带说明、示例、前后端校验 |
| ACL | 访问控制列表 + reloadacl |
| 拨号计划 / 路由 | dialplan XML（兼容旧路由表） |
| IVR | 语音菜单与按键分支 |
| 呼叫中心 | 队列 / 坐席 / Tier |
| 会议室 | 会议室与 Profile |
| 语音留言 | voicemail 配置与留言列表 |
| 实时运维 | 通道、Sofia 注册、模块、ESL 控制台 |
| **呼叫流程看板** | 把 ACL → 路由 → 拨号计划 → 目的地渲染为工作流；输入号码模拟命中路径，点击节点直接改配置并热生效 |
| 悬浮软电话 | 顶栏「软电话」打开，可拖到任意位置；支持拨号、来电接听、静音、DTMF 拨号盘，SIP 配置保存在本机浏览器 |
| CDR / 录音 / 日志 | 话单、录音浏览、日志查看 |

---

## 技术栈

| 项 | 版本 / 说明 |
| --- | --- |
| JDK | **21**（编译 `release 21`，启动需 `--add-opens`） |
| Spring Boot | 2.5.15（Spring Framework 5.3.39） |
| 安全 | Apache Shiro 1.13.0 |
| UI | Thymeleaf + Bootstrap 3.3.7 + `pbx-theme.css` 主题层 |
| ORM | MyBatis + PageHelper |
| 数据库 | PostgreSQL |
| ESL | org.freeswitch.esl.client 0.9.2 |
| 模板 | Velocity 2.3（生成 FS XML） |
| 构建 | Maven 3.9+，Lombok 1.18.36 |
| 目标 FS | **1.11.0** |

---

## 快速开始

### 1. 初始化数据库

```bash
psql -U postgres -d youpbx -f sql/pbx_pgsql.sql
# 从旧版本升级：移除演示菜单、PBX 菜单分组（两条脚本可重复执行）
psql -U postgres -d youpbx -f sql/upgrade_remove_demo_menu.sql
psql -U postgres -d youpbx -f sql/upgrade_pbx_menu_groups.sql
psql -U postgres -d youpbx -f sql/upgrade_callflow_board_menu.sql
```

一份脚本包含：系统 `sys_*` 表 + PBX 业务表 + 菜单权限。

### 2. 修改配置

| 文件 | 用途 |
| --- | --- |
| `av-admin/src/main/resources/application-druid.yml` | 数据库连接 |
| `jpbx-gui/src/main/resources/fs.properties` | ESL 地址/密码、xml_curl token/IP 白名单 |

### 3. 打包与启动

```bash
# 打包（JDK 21；应用需处于停止状态，否则 jar 被占用）
mvn -pl av-admin -am clean package -Dmaven.test.skip=true

# Windows：唯一启动脚本（自动探测 JDK 21、检查 ESL 8021、日志落 logs/）
start-jpbx.bat            # 启动
start-jpbx.bat status     # 状态
start-jpbx.bat stop       # 停止
start-jpbx.bat force      # 杀掉占用 8090 的实例并重启
start-jpbx.bat service    # 前台运行（计划任务 / 服务包装器）

# Linux / macOS：对应脚本，用法与上面一致（首次使用先赋可执行权限）
chmod +x jpbx.sh
./jpbx.sh start|stop|restart|force|status|service

# 开发启动
mvn -pl av-admin -am spring-boot:run
```

默认访问：`http://localhost:8090`，初始账号 `admin / admin123`。

> JDK 21 启动脚本已内置 `--add-opens java.base/java.lang|java.util|java.lang.reflect|java.text=ALL-UNNAMED`，
> 手工启动时请一并带上，否则 Spring 5.3 / Shiro 反射可能报 `InaccessibleObjectException`。

### 4. 对接 FreeSWITCH

完整步骤（含 v1.11.0 下载地址、编译安装、xml_curl / ESL 配置）见
**[doc/FreeSWITCH对接与安装说明.md](doc/FreeSWITCH对接与安装说明.md)**。要点：

- `mod_xml_curl` → `http://<JPBX>:8090/pbx/api/xml`
- `mod_event_socket` → 与 `fs.properties` 一致（默认 8021）
- 必要模块：sofia / dialplan_xml / callcenter / conference / voicemail / cdr_pg_csv 等

---

## 界面说明

全站采用浅色专业风主题，入口为 `static/av/css/pbx-theme.css`：

- 设计令牌：品牌蓝 `#1E63B8`、画布 `#F5F7FA`、卡片白、边框 `#E3E8EF`、正文 `#1F2937`
- 侧边栏 220px 白底 + 蓝色选中态，可折叠为 64px 图标模式
- 列表页统一「搜索卡片 + 工具栏 + 数据表格 + 分页」
- 控制台类页面（实时运维 / 日志 / 通话记录）使用深色终端卡片
- 修改样式请只改主题层，不要改 `style.min.css` / `av-ui.css` 本体

---

## Maven 模块

```
pbx (父工程)
├── av-admin       # 启动入口、Web、打包
├── av-framework   # Shiro、过滤器、XSS
├── av-system      # 用户/角色/菜单/字典
├── av-common      # 公共工具、BaseEntity
├── av-quartz      # 定时任务
├── av-generator   # 代码生成
└── jpbx-gui       # FreeSWITCH 业务核心（重点）
```

---

## 许可

项目仅供内部部署与二次开发使用，第三方组件遵循各自许可证。
