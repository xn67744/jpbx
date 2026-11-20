package com.av.jpbx.service.impl;

import cn.hutool.core.io.FileUtil;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxSysconf;
import com.av.jpbx.service.FsEslClient;
import com.av.jpbx.service.IPbxLogService;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PbxLogServiceImpl implements IPbxLogService {

    /** 页面只展示最新的 1000 行，避免大日志把浏览器拖死 */
    private static final int MAX_LINES = 1000;

    @Autowired
    private FsEslClient fsEslClient;

    @Override
    public String readLogs(LocalDate startDate, LocalDate endDate) throws IOException {
        TPbxSysconf conf = fsEslClient.getSysconf();
        if (conf == null || StringUtils.isEmpty(conf.getFreeswitchLogPath())) {
            return "未配置 freeswitchLogPath";
        }
        // 只看今天：直接取 freeswitch.log 的尾部，避免归档文件先占满 1000 行预算
        if (startDate.equals(endDate) && startDate.equals(LocalDate.now())) {
            StringBuilder current = new StringBuilder();
            appendTail(current, Paths.get(conf.getFreeswitchLogPath(), "freeswitch.log"), 0);
            return current.toString().replace("\n", "<br>");
        }
        StringBuilder mergedLogs = new StringBuilder();
        int lineCount = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate) && lineCount < MAX_LINES) {
            Path logFilePath = Paths.get(conf.getFreeswitchLogPath(),
                    String.valueOf(currentDate.getYear()),
                    String.format("%02d", currentDate.getMonthValue()),
                    currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'.log'")));
            lineCount = appendTail(mergedLogs, logFilePath, lineCount);
            currentDate = currentDate.plusDays(1);
        }
        LocalDate today = LocalDate.now();
        if (!startDate.isAfter(today) && !endDate.isBefore(today) && lineCount < MAX_LINES) {
            Path logFilePath = Paths.get(conf.getFreeswitchLogPath(), "freeswitch.log");
            appendTail(mergedLogs, logFilePath, lineCount);
        }
        return mergedLogs.toString().replace("\n", "<br>");
    }

    /** 仅读取文件末尾，避免整文件进内存 */
    private int appendTail(StringBuilder out, Path path, int lineCount) throws IOException {
        if (!Files.exists(path)) {
            return lineCount;
        }
        List<String> lines = readLastLines(path, MAX_LINES - lineCount);
        for (String line : lines) {
            out.append(line).append("\n");
            lineCount++;
        }
        return lineCount;
    }

    private List<String> readLastLines(Path path, int max) throws IOException {
        if (max <= 0) {
            return new ArrayList<>();
        }
        long size = Files.size(path);
        if (size < 2 * 1024 * 1024) {
            String charset = detectCharset(path);
            List<String> all = Files.readAllLines(path, Charset.forName(charset));
            if (all.size() <= max) {
                return all;
            }
            return all.subList(all.size() - max, all.size());
        }
        // 大文件：从末尾读最多 1MB
        List<String> result = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
            long pos = Math.max(0, size - 1024 * 1024);
            raf.seek(pos);
            if (pos > 0) {
                raf.readLine();
            }
            String line;
            while ((line = raf.readLine()) != null) {
                result.add(new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
        }
        if (result.size() > max) {
            return result.subList(result.size() - max, result.size());
        }
        return result;
    }

    @Override
    public void generateLogFile() throws IOException {
        TPbxSysconf conf = fsEslClient.getSysconf();
        if (conf == null || StringUtils.isEmpty(conf.getFreeswitchLogPath())) {
            return;
        }
        Path logFilePath = Paths.get(conf.getFreeswitchLogPath() + FileUtil.FILE_SEPARATOR + "freeswitch.log");
        if (!Files.exists(logFilePath)) {
            return;
        }
        Map<LocalDate, List<String>> dailyLogs = new LinkedHashMap<>();
        String charset = detectCharset(logFilePath);
        List<String> allLines = Files.readAllLines(logFilePath, Charset.forName(charset));
        for (String line : allLines) {
            if (line.length() < 10) {
                continue;
            }
            try {
                LocalDate date = LocalDate.parse(line.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);
                dailyLogs.computeIfAbsent(date, k -> new ArrayList<>()).add(line);
            } catch (Exception ignore) {
                // skip
            }
        }
        for (Map.Entry<LocalDate, List<String>> entry : dailyLogs.entrySet()) {
            LocalDate date = entry.getKey();
            Path target = Paths.get(conf.getFreeswitchLogPath(),
                    String.valueOf(date.getYear()),
                    String.format("%02d", date.getMonthValue()),
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'.log'")));
            Files.createDirectories(target.getParent());
            Files.write(target, entry.getValue(), Charset.forName(charset),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    private String detectCharset(Path path) {
        try {
            byte[] buf = new byte[4096];
            int nread;
            UniversalDetector detector = new UniversalDetector(null);
            try (java.io.FileInputStream fis = new java.io.FileInputStream(path.toFile())) {
                while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                    detector.handleData(buf, 0, nread);
                }
            }
            detector.dataEnd();
            String encoding = detector.getDetectedCharset();
            detector.reset();
            return encoding != null ? encoding : "UTF-8";
        } catch (Exception e) {
            return "UTF-8";
        }
    }
}
