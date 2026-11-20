package com.av.jpbx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FsValidationError {
    private String key;
    private String title;
    private String message;
}
