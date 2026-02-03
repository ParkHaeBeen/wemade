package com.wemade.service;

import com.wemade.controller.dto.AnalysisCreateResponse;
import com.wemade.controller.dto.AnalysisReadResponse;
import com.wemade.domain.Analysis;
import com.wemade.infrastructure.persistence.AnalysisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnalysisService {
  private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);

  private final AnalysisRepository analysisRepository;
  private final AnalysisFileValidator analysisFileValidator;
  private final AnalysisExecutor analysisExecutor;

  public AnalysisService(AnalysisRepository analysisRepository, AnalysisFileValidator analysisFileValidator, AnalysisExecutor analysisExecutor) {
    this.analysisRepository = analysisRepository;
    this.analysisFileValidator = analysisFileValidator;
    this.analysisExecutor = analysisExecutor;
  }

  public AnalysisCreateResponse create(MultipartFile file) {
    analysisFileValidator.validate(file);

    Analysis analysis = analysisRepository.save(Analysis.create());
    log.info("event=analysis_start analysisId={} filename={} sizeBytes={}",
            analysis.getId(), file.getOriginalFilename(), file.getSize());

    analysisExecutor.run(file, analysis);

    return new AnalysisCreateResponse(analysis.getId());
  }

  public AnalysisReadResponse read(String  analysisId) {
    return new AnalysisReadResponse();
  }
}
