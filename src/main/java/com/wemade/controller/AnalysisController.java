package com.wemade.controller;

import com.wemade.controller.dto.AnalysisCreateResponse;
import com.wemade.controller.dto.AnalysisReadResponse;
import com.wemade.service.AnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

  private final AnalysisService analysisService;

  public AnalysisController(AnalysisService analysisService) {
    this.analysisService = analysisService;
  }

  @PostMapping
  public AnalysisCreateResponse create() {
    return analysisService.create();
  }

  @GetMapping("/{analysisId}")
  public AnalysisReadResponse read(@PathVariable String analysisId) {
    return analysisService.read(analysisId);
  }

}
