package com.wemade.service;

import com.wemade.controller.dto.AnalysisCreateResponse;
import com.wemade.controller.dto.AnalysisReadResponse;
import org.springframework.stereotype.Service;

@Service
public class AnalysisService {

  public AnalysisCreateResponse create() {
    return new AnalysisCreateResponse();
  }

  public AnalysisReadResponse read(String  analysisId) {
    return new AnalysisReadResponse();
  }
}
