package com.wemade.controller.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record AnalysisCreateRequest(
        MultipartFile file
) {
}
