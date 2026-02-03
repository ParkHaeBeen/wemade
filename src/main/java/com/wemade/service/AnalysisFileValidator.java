package com.wemade.service;

import com.wemade.common.exception.ErrorCode;
import com.wemade.common.exception.FileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AnalysisFileValidator {
    private static final long MAX_BYTES = 50L * 1024 * 1024;

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileException(ErrorCode.FILE_REQUIRED);
        }
        if (file.getSize() > MAX_BYTES) {
            throw new FileException(ErrorCode.FILE_SIZE_EXCEEDED);
        }
    }
}
