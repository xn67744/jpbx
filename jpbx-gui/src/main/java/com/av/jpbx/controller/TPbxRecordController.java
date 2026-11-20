package com.av.jpbx.controller;

import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxSysconf;
import com.av.jpbx.service.FsEslClient;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 录音文件浏览/下载。
 */
@Controller
@RequestMapping("/pbx/record")
public class TPbxRecordController extends BaseController {

    private final String prefix = "pbx/record";

    @Autowired
    private FsEslClient fsEslClient;

    @RequiresPermissions("pbx:record:view")
    @GetMapping()
    public String page() {
        return prefix + "/record";
    }

    @RequiresPermissions("pbx:record:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String path) {
        TPbxSysconf conf = fsEslClient.getSysconf();
        String root = conf != null ? conf.getAudioRecordPath() : null;
        if (StringUtils.isEmpty(root)) {
            return getDataTable(Collections.emptyList());
        }
        Path base = Paths.get(root).normalize();
        Path dir = StringUtils.isEmpty(path) ? base : base.resolve(path).normalize();
        if (!dir.startsWith(base) || !Files.isDirectory(dir)) {
            return getDataTable(Collections.emptyList());
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Stream<Path> stream = Files.list(dir)) {
            List<Path> files = stream.sorted(Comparator.comparing(Path::getFileName).reversed())
                    .limit(500).collect(Collectors.toList());
            for (Path f : files) {
                Map<String, Object> m = new HashMap<>();
                m.put("name", f.getFileName().toString());
                m.put("path", base.relativize(f).toString().replace('\\', '/'));
                m.put("directory", Files.isDirectory(f));
                m.put("size", Files.isRegularFile(f) ? Files.size(f) : 0L);
                m.put("mtime", Files.getLastModifiedTime(f).toMillis());
                rows.add(m);
            }
        } catch (IOException e) {
            logger.error("list records: {}", e.getMessage());
        }
        return getDataTable(rows);
    }

    @RequiresPermissions("pbx:record:list")
    @GetMapping("/download")
    public void download(String path, HttpServletResponse response) throws IOException {
        TPbxSysconf conf = fsEslClient.getSysconf();
        String root = conf != null ? conf.getAudioRecordPath() : null;
        if (StringUtils.isEmpty(root) || StringUtils.isEmpty(path)) {
            response.sendError(404);
            return;
        }
        Path base = Paths.get(root).normalize();
        Path file = base.resolve(path).normalize();
        if (!file.startsWith(base) || !Files.isRegularFile(file)) {
            response.sendError(404);
            return;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(file.getFileName().toString(), "UTF-8"));
        Files.copy(file, response.getOutputStream());
    }

    @RequiresPermissions("pbx:record:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String path) throws IOException {
        TPbxSysconf conf = fsEslClient.getSysconf();
        String root = conf != null ? conf.getAudioRecordPath() : null;
        if (StringUtils.isEmpty(root) || StringUtils.isEmpty(path)) {
            return error("路径无效");
        }
        Path base = Paths.get(root).normalize();
        Path file = base.resolve(path).normalize();
        if (!file.startsWith(base) || !Files.isRegularFile(file)) {
            return error("文件不存在");
        }
        Files.deleteIfExists(file);
        return success();
    }
}
