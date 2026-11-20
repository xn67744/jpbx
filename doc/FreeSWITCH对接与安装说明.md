# FreeSWITCH 1.11.0 下载、安装与 JPBX 对接说明

本文覆盖两块内容：

1. **下载并安装** 与 JPBX 配套的 FreeSWITCH **1.11.0**  
2. **对接 JPBX**：`mod_xml_curl` + ESL 热生效（常规操作无需重启 FS 进程）

相关文档：

- [项目说明.md](项目说明.md) — 功能、菜单、开发约定  
- [架构设计.md](架构设计.md) — 系统架构与数据流  
- [README.md](../README.md) — 项目入口  

---

## 1. 版本与下载地址

JPBX 目标版本：**FreeSWITCH v1.11.0**（SignalWire 官方发布）。

| 资源 | 地址 |
|------|------|
| **v1.11.0 Release 页面** | https://github.com/signalwire/freeswitch/releases/tag/v1.11.0 |
| 源码包 `.tar.gz` | https://github.com/signalwire/freeswitch/archive/refs/tags/v1.11.0.tar.gz |
| 源码包 `.zip` | https://github.com/signalwire/freeswitch/archive/refs/tags/v1.11.0.zip |
| 官方仓库 | https://github.com/signalwire/freeswitch |
| 1.11.x 发行说明 | https://developer.signalwire.com/freeswitch/FreeSWITCH-Explained/Release-Notes/FreeSWITCH-1.11.x-Release-notes |
| 全量 Releases | https://github.com/signalwire/freeswitch/releases |
| 构建/安装文档入口 | https://developer.signalwire.com/freeswitch/FreeSWITCH-Explained/ |

> 说明：若需同系列补丁版，可选用 `v1.11.1` / `v1.11.2` / `v1.11.3` 等（见 Releases），但联调请先以 **v1.11.0** 对齐本文模块与配置。生产环境建议关注官方安全更新后再升级小版本。

用 Git 检出指定标签：

```bash
git clone https://github.com/signalwire/freeswitch.git
cd freeswitch
git checkout v1.11.0
```

或直接下载解压：

```bash
wget https://github.com/signalwire/freeswitch/archive/refs/tags/v1.11.0.tar.gz
tar xzf v1.11.0.tar.gz
cd freeswitch-1.11.0
```

---

## 2. 安装说明（推荐 Debian/Ubuntu 源码构建）

以下以 **Debian 12 / Ubuntu 22.04+** 为例。其他发行版请参考官方「Build from source」文档。  
Windows 可参考仓库内 Windows 工程与官方 Windows 安装说明（本平台联调以 Linux 为主）。

### 2.1 系统依赖（示例）

```bash
sudo apt-get update
sudo apt-get install -y \
  build-essential cmake autoconf automake libtool pkg-config \
  libssl-dev zlib1g-dev libpq-dev unixodbc-dev \
  libcurl4-openssl-dev libpcre2-dev libedit-dev \
  libsqlite3-dev libspeex-dev libspeexdsp-dev \
  libldns-dev libtiff-dev yasm libopus-dev \
  libsndfile1-dev libavformat-dev libswscale-dev \
  liblua5.2-dev
```

> v1.11.0 起核心正则库迁移为 **PCRE2**，请确保安装 `libpcre2-dev`。更多依赖以官方当前文档为准。

### 2.2 引导、配置、编译、安装

```bash
cd freeswitch-1.11.0   # 或 git 工作目录

# 生成构建系统
./bootstrap.sh -j

# 按需编辑 modules.conf，打开本平台需要的模块，例如：
#   xml_curl、event_socket、sofia、dialplan_xml、commands、dptools、
#   cdr_pg_csv、voicemail、conference、callcenter、hash、logfile 等
# vi modules.conf

./configure --enable-core-pgsql-support
make -j$(nproc)
sudo make install
sudo make cd-sounds-install
sudo make cd-moh-install
```

默认安装前缀一般为 `/usr/local/freeswitch`：

| 路径 | 用途 |
|------|------|
| `/usr/local/freeswitch/bin/freeswitch` | 主程序 |
| `/usr/local/freeswitch/bin/fs_cli` | 命令行 |
| `/usr/local/freeswitch/conf/` | 配置目录 |
| `/usr/local/freeswitch/mod/` | 模块 |
| `/usr/local/freeswitch/log/` | 日志 |

### 2.3 启动与验证

```bash
# 前台调试
/usr/local/freeswitch/bin/freeswitch -nonat

# 或后台（按你的 systemd/脚本习惯）
# /usr/local/freeswitch/bin/freeswitch -nc -nonat

# 进入 CLI
/usr/local/freeswitch/bin/fs_cli
```

在 `fs_cli` 中：

```
status
sofia status
show modules
```

能正常显示版本与模块即安装成功。

### 2.4 Docker / 包安装（可选）

- 官方示例 Dockerfile：https://github.com/signalwire/freeswitch/tree/master/docker/examples  
- Debian 包构建工具 FSDEB / FSGET：见官方 Release Notes 与 Explained 文档  
- 若使用发行版软件源，请确认软件包版本为 **1.11.x**，并仍按下文开启 `xml_curl`、`event_socket` 等模块  

---

## 3. 与 JPBX 对接总览

```
JPBX Web (8090)
  ├── ESL inbound  → FS :8021   （监控 / reloadxml / sofia / callcenter / module）
  └── xml_curl API ← FS          （directory / configuration / dialplan）
```

- **不直接改 FreeSWITCH 磁盘上的 XML 文件**。管理台按「配置段 → 参数名/参数值」编辑（菜单：**FS参数配置** `/pbx/fsconf`）；保存写入 `t_pbx_fs_param`，由 xml_curl 动态生成 XML，ESL 热生效。  
- 分机、拨号计划、Sofia/网关、ACL、IVR、Callcenter、Conference、Voicemail 均由 JPBX 数据库生成。  

---

## 4. 必开模块

编辑 `conf/autoload_configs/modules.conf.xml`（或构建前的 `modules.conf`），确保加载：

```
mod_console
mod_logfile
mod_event_socket
mod_sofia
mod_commands
mod_dptools
mod_dialplan_xml
mod_xml_curl
mod_cdr_pg_csv
mod_hash
mod_voicemail
mod_conference
mod_callcenter
mod_httapi   <!-- 可选 -->
```

热加载（无需重启 FS）：

```
load mod_xxx
unload mod_xxx
reload mod_xxx
```

也可在 JPBX「实时运维 → ESL 控制台 / 模块操作」执行。

---

## 5. event_socket（ESL）

编辑 `autoload_configs/event_socket.conf.xml`：

```xml
<configuration name="event_socket.conf" description="Socket Client">
  <settings>
    <param name="nat-map" value="false"/>
    <param name="listen-ip" value="0.0.0.0"/>
    <param name="listen-port" value="8021"/>
    <param name="password" value="ClueCon"/>
  </settings>
</configuration>
```

与 JPBX `jpbx-gui/src/main/resources/fs.properties` 保持一致：

```
eslHost: <FS主机IP>
eslInBoundPort: 8021
eslpwd: ClueCon
xmlCurlToken: jpbx-xml-curl-token
xmlCurlAllowedIps: <FS主机IP>
```

热加载：`reload mod_event_socket`（会短暂断开 ESL，JPBX 会自动重连）。

---

## 6. mod_xml_curl

编辑 `autoload_configs/xml_curl.conf.xml`：

```xml
<configuration name="xml_curl.conf" description="cURL XML Gateway">
  <bindings>
    <binding name="directory">
      <param name="gateway-url" value="http://<JPBX_IP>:8090/pbx/api/xml?section=directory&amp;token=jpbx-xml-curl-token" bindings="directory"/>
    </binding>
    <binding name="dialplan">
      <param name="gateway-url" value="http://<JPBX_IP>:8090/pbx/api/xml?section=dialplan&amp;token=jpbx-xml-curl-token" bindings="dialplan"/>
    </binding>
    <binding name="configuration">
      <param name="gateway-url" value="http://<JPBX_IP>:8090/pbx/api/xml?section=configuration&amp;token=jpbx-xml-curl-token" bindings="configuration"/>
    </binding>
  </bindings>
</configuration>
```

然后：

```
reloadxml
reload mod_xml_curl
```

兼容旧接口：`/pbx/api/user/getUserXML`、`/pbx/api/conf/getXML`。

---

## 7. Sofia 关键设置

本地 sofia 配置请确保：

```xml
<param name="accept-blind-reg" value="false"/>
```

否则注册可能不走 xml_curl directory。

- 网关：`external` profile  
- 分机注册：`internal` profile  

JPBX 保存网关后会执行：

```
reloadxml
sofia profile external killgw <gatewayId>
sofia profile external rescan
```

保存系统 / Profile 参数后会执行 profile `restart`（**仅重启 profile，不是 FS 进程**）。

---

## 8. 拨号计划

拨号计划由 xml_curl 按 `Hunt-Context` / `Caller-Context` 动态生成，建议：

1. 精简本地 `conf/dialplan/`，避免静态 XML 抢先命中。  
2. 在 JPBX「拨号计划」维护 `default` / `public`。  
3. 旧「路由管理」仍可生成 bridge，可逐步迁到拨号计划表。  

保存后：`reloadxml`（下一通呼叫生效）。

---

## 9. CDR（PostgreSQL）

1. 执行 `sql/pbx_pgsql.sql`（含 `cdr` 及全部 PBX 表、菜单）。  
2. 在 JPBX「FS参数配置 / CDR」或系统配置填写 `db-info`，例如：

```
host=127.0.0.1 port=5432 dbname=youpbx user=postgres password=xxx connect_timeout=10
```

3. 确保 `mod_cdr_pg_csv` 经 xml_curl 拉取配置。  
4. `reloadxml` 后 `reload mod_cdr_pg_csv`。  

---

## 10. 热生效对照表（不重启 FS）

| JPBX 变更 | ESL 动作 |
|-----------|----------|
| 分机/域名/分组 | `reloadxml` |
| 拨号计划 / 路由 / IVR | `reloadxml` |
| 网关 | `reloadxml` + `sofia profile external killgw/rescan` |
| Sofia/系统端口媒体 | `reloadxml` + `sofia profile <name> restart` |
| ACL | `reloadxml` + `reloadacl` |
| Callcenter | `reloadxml` + queue load/unload 或 `reload mod_callcenter` |
| Conference / Voicemail | `reloadxml` |
| 模块启停 | `load` / `unload` / `reload` `mod_xxx` |

---

## 11. 例外：必须重启 FreeSWITCH 的项

1. 更换 `switch.conf` 部分全局核心参数且会话强依赖旧值时。  
2. ESL `listen-ip`/`listen-port` 变更后 `reload mod_event_socket` 失败。  
3. 操作系统级网卡/内核参数变更。  

RTP 端口范围变更建议：改配置 → profile restart → 观察新呼叫；异常再维护窗口重启 FS。

---

## 12. 防火墙与端口

| 端口 | 用途 |
|------|------|
| 8090 | JPBX Web + xml_curl |
| 8021 | ESL |
| 5060/5080 | SIP internal/external |
| RTP 范围 | 系统配置 start/end |
| 5066/7443 | WS/WSS（若启用） |

确保 FS 能访问：`http://<JPBX_IP>:8090/pbx/api/xml`。

---

## 13. 联调检查清单

1. JPBX 日志出现 `ESL connected`。  
2. `sofia status` 正常；分机注册后 JPBX「注册列表」可见。  
3. `fs_cli` → `reloadxml` 后测试呼叫。  
4. 改分机密码 → 保存 → 重新注册成功（无需重启 FS）。  
5. 改网关 → `sofia status gateway` 可见。  
6. 改拨号计划 → 下一通走新规则。  
7. CDR 写入 `cdr`；录音可在「录音管理」浏览。  

---

## 14. 安全建议

- 修改默认 ESL 密码与 `xmlCurlToken`。
- 配置 `xmlCurlAllowedIps` 为 FS 主机 IP。
- 生产环境数据库/ESL 口令外置，勿提交仓库。

---

## 15. 浏览器悬浮软电话（sip.js）

顶栏「软电话」打开悬浮面板，可拖动到任意位置，拨号/接听/静音不影响下方系统操作。配置项保存在浏览器 `localStorage`（键 `jpbx.sip.config.v1`），不会上传到 JPBX 服务端。

首次使用：

1. 在面板「SIP 配置」中填写 **WebSocket 地址**、**SIP 账号**（如 `1132@192.168.0.118`）、密码；默认地址按当前页面推导为 `ws(s)://<host>:7443`。
2. 勾选自动注册并点「保存并重新连接」；状态灯变绿表示注册成功。
3. 拨号盘输入号码回车或点「呼叫」；来电时点「接听」，通话中可静音。

> 注意：sip.js 使用标准 SIP over WebSocket（RFC 7118），要求 FreeSWITCH 侧提供 WSS 接入（或经 Kamailio/OpenSIPS 等 SIP WS 代理转发到 FreeSWITCH）。仅启用 `mod_verto` 的 Verto JSON 协议不兼容；连接失败时面板会显示具体错误。浏览器麦克风权限要求 `localhost` 或 HTTPS。
