package com.wemade.service;

import com.wemade.common.exception.ErrorCode;
import com.wemade.common.exception.NotFoundException;
import com.wemade.controller.dto.AnalysisCreateResponse;
import com.wemade.controller.dto.AnalysisReadResponse;
import com.wemade.controller.dto.AnalysisTopIpInfoResponse;
import com.wemade.controller.dto.AnalysisTopItem;
import com.wemade.domain.Analysis;
import com.wemade.domain.AnalysisStatistics;
import com.wemade.domain.IpInfo;
import com.wemade.infrastructure.persistence.AnalysisRepository;
import com.wemade.service.mapper.AnalysisReadMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

  private final AnalysisRepository analysisRepository;
  private final AnalysisFileValidator analysisFileValidator;
  private final AnalysisExecutor analysisExecutor;
  private final AnalysisReadMapper analysisReadMapper;
  private final IpInfoService ipInfoService;

  public AnalysisService(
          AnalysisRepository analysisRepository,
          AnalysisFileValidator analysisFileValidator,
          AnalysisExecutor analysisExecutor,
          AnalysisReadMapper analysisReadMapper,
          IpInfoService ipInfoService
  ) {
    this.analysisRepository = analysisRepository;
    this.analysisFileValidator = analysisFileValidator;
    this.analysisExecutor = analysisExecutor;
    this.analysisReadMapper = analysisReadMapper;
    this.ipInfoService = ipInfoService;
  }

  public AnalysisCreateResponse create(MultipartFile file) {
    analysisFileValidator.validate(file);

    Analysis analysis = analysisRepository.save(Analysis.create());
    analysisExecutor.run(file, analysis);

    return new AnalysisCreateResponse(analysis.getId());
  }

  public AnalysisReadResponse read(String analysisId, int topN) {
    Analysis analysis = analysisRepository.findById(analysisId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, analysisId));

    AnalysisStatistics statistics = analysis.getStatistics();

    List<AnalysisTopItem> topPaths = topN(statistics.getPathCounts(), topN);
    List<AnalysisTopItem> topStatusCodes = topN(statistics.getStatusCounts(), topN);
    List<AnalysisTopIpInfoResponse> topIpInfos = getIpInfos(topN(statistics.getIpCounts(), topN));


    return analysisReadMapper.mapper(
            analysis,
            topPaths,
            topStatusCodes,
            topIpInfos
    );
  }

  private List<AnalysisTopItem> topN(
          Map<String, Long> counts,
          int n
  ) {
    return counts.entrySet().stream()
            .sorted((left, right) -> Long.compare(right.getValue(), left.getValue()))
            .limit(n)
            .map(entry -> new AnalysisTopItem(entry.getKey(), entry.getValue()))
            .toList();
  }

  private List<AnalysisTopIpInfoResponse> getIpInfos(List<AnalysisTopItem> topIps) {
    return topIps.stream()
            .map(item -> {
              IpInfo info = ipInfoService.read(item.key());

              return new AnalysisTopIpInfoResponse(
                      item.key(),
                      item.count(),
                      info.getCountry(),
                      info.getRegion(),
                      info.getCity(),
                      info.getAsn(),
                      info.getAsDomain()
              );
            })
            .toList();
  }
}
