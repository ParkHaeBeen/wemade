package com.wemade.service;

import com.wemade.common.exception.ErrorCode;
import com.wemade.common.exception.NotFoundException;
import com.wemade.controller.dto.AnalysisCreateResponse;
import com.wemade.controller.dto.AnalysisReadResponse;
import com.wemade.domain.Analysis;
import com.wemade.infrastructure.persistence.AnalysisRepository;
import com.wemade.service.mapper.AnalysisReadMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnalysisService {
  private final AnalysisRepository analysisRepository;
  private final AnalysisFileValidator analysisFileValidator;
  private final AnalysisExecutor analysisExecutor;
  private final AnalysisReadMapper analysisReadMapper;

  public AnalysisService(
          AnalysisRepository analysisRepository,
          AnalysisFileValidator analysisFileValidator,
          AnalysisExecutor analysisExecutor,
          AnalysisReadMapper analysisReadMapper
  ) {
    this.analysisRepository = analysisRepository;
    this.analysisFileValidator = analysisFileValidator;
    this.analysisExecutor = analysisExecutor;
    this.analysisReadMapper = analysisReadMapper;
  }

  public AnalysisCreateResponse create(MultipartFile file) {
    analysisFileValidator.validate(file);

    Analysis analysis = analysisRepository.save(Analysis.create());
    analysisExecutor.run(file, analysis);

    return new AnalysisCreateResponse(analysis.getId());
  }

  public AnalysisReadResponse read(String  analysisId, int topN) {
    Analysis analysis = analysisRepository.findById(analysisId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, analysisId));
    return analysisReadMapper.mapper(analysis, topN);
  }
}
