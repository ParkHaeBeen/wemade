package com.wemade.controller;

import com.wemade.controller.dto.AnalysisCreateResponse;
import com.wemade.controller.dto.AnalysisReadResponse;
import com.wemade.service.AnalysisService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

  private final AnalysisService analysisService;

  public AnalysisController(AnalysisService analysisService) {
    this.analysisService = analysisService;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public AnalysisCreateResponse create(
          @RequestPart MultipartFile file
  ) {
    return analysisService.create(file);
  }

  @GetMapping("/{analysisId}")
  public AnalysisReadResponse read(@PathVariable String analysisId) {
    return analysisService.read(analysisId);
  }

}
