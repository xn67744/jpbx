package com.av.jpbx.service.impl;

import cn.hutool.core.io.FileUtil;
import com.av.jpbx.service.EslListenerServer;
import com.av.jpbx.service.IPbxLogService;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
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

/**
 * 服务配置Service业务层处理
 * 
 * @author long
 * @date 2024-02-21
 */
@Service
public class PbxLogServiceImpl implements IPbxLogService
{

    @Override
    public String readLogs(LocalDate startDate, LocalDate endDate) throws IOException {
        StringBuilder mergedLogs = new StringBuilder();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            Path logFilePath = Paths.get(EslListenerServer.sysconf.getFreeswitchLogPath(),
                    String.valueOf(currentDate.getYear()),
                    String.format("%02d", currentDate.getMonthValue()),
                    currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'.log'")));
            if (Files.exists(logFilePath)) {
                String charset = detectCharset(logFilePath);
                List<String> lines = Files.readAllLines(logFilePath, Charset.forName(charset));
                for (String line : lines) {
                    mergedLogs.append(line).append("\n");
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        LocalDate today = LocalDate.now(); // 获取当前日期
        // 如果查询范围包括当天，添加freeswitch.log的内容
        if (!startDate.isAfter(today) && !endDate.isBefore(today)) {
            Path logFilePath = Paths.get(EslListenerServer.sysconf.getFreeswitchLogPath(), "freeswitch.log");
            if (Files.exists(logFilePath)) {
                String charset = detectCharset(logFilePath);
                List<String> lines = Files.readAllLines(logFilePath, Charset.forName(charset));
                for (String line : lines) {
                    mergedLogs.append(line).append("\n");
                }
            }
        }

        // 将换行符替换为HTML的<br>标签
        return mergedLogs.toString().replace("\n", "<br>");
    }

    @Override
    public void generateLogFile() throws IOException {
        //获取系统配置的地址
        Path logFilePath = Paths.get(EslListenerServer.sysconf.getFreeswitchLogPath()+ FileUtil.FILE_SEPARATOR+"freeswitch.log");
        Map<LocalDate, List<String>> dailyLogs = new LinkedHashMap<>();
        String charset = detectCharset(logFilePath);
        List<String> allLines = Files.readAllLines(logFilePath, Charset.forName(charset));
        LocalDate lastDate = null;

        // 解析并分组每一行日志
        for (String line : allLines) {
            LocalDate date = extractDate(line);
            if (date != null) {
                dailyLogs.putIfAbsent(date, new ArrayList<>());
                lastDate = date;
            }
            if (lastDate != null) {
                dailyLogs.get(lastDate).add(line);
            }
        }

        // 写入分割后的日志文件，跳过当前日期
        for (Map.Entry<LocalDate, List<String>> entry : dailyLogs.entrySet()) {
            LocalDate date = entry.getKey();
            if (date.equals(LocalDate.now())) {
                continue; // 跳过当前日期
            }
            List<String> logs = entry.getValue();
            saveLogContent(date, String.join("\n", logs), charset);
        }


        // 重置原始日志文件，仅保留最新一天的日志
        if (lastDate != null) {
            List<String> lastDayLogs = dailyLogs.get(lastDate);
            Files.write(logFilePath, lastDayLogs, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    private LocalDate extractDate(String line) {
        if (line.length() >= 10) { // "yyyy-MM-dd".length() == 10
            try {
                String dateString = line.substring(0, 10);
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                // 解析失败，忽略该行
            }
        }
        return null;
    }

    private void saveLogContent(LocalDate date, String content, String charsetName) throws IOException {
        Path directoryPath = Paths.get(EslListenerServer.sysconf.getFreeswitchLogPath(), String.valueOf(date.getYear()), String.format("%02d", date.getMonthValue()));
        Files.createDirectories(directoryPath);
        Path logFilePath = directoryPath.resolve(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'.log'")));
        Files.write(logFilePath, content.getBytes(Charset.forName(charsetName)), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    // 使用第三方库探测文件编码
    private String detectCharset(Path path) throws IOException {
        byte[] buf = new byte[4096];
        FileInputStream fis = new FileInputStream(path.toFile());
        // (1)
        UniversalDetector detector = new UniversalDetector(null);
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding != null) {
            return encoding;
        } else {
            return "UTF-8"; // 默认编码
        }
    }
}
