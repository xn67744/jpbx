package com.av.jpbx.enums;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 热生效执行结果。
 */
@Data
@Accessors(chain = true)
public class FsApplyResult {
    private boolean success = true;
    private Date applyTime = new Date();
    private String resourceType;
    private List<String> commands = new ArrayList<>();
    private List<String> responses = new ArrayList<>();
    private String message;

    public FsApplyResult addCommand(String cmd, String response) {
        commands.add(cmd);
        responses.add(response == null ? "" : response);
        return this;
    }

    public FsApplyResult fail(String message) {
        this.success = false;
        this.message = message;
        return this;
    }
}
