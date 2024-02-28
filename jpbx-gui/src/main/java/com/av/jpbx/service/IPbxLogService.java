package com.av.jpbx.service;

import java.io.IOException;
import java.time.LocalDate;

public interface IPbxLogService {
    String readLogs(LocalDate startDate, LocalDate endDate) throws IOException;

    void generateLogFile() throws IOException;
}
