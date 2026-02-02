package com.wemade.service;

import com.wemade.common.exception.ErrorCode;
import com.wemade.common.exception.ParserException;
import com.wemade.domain.Analysis;
import com.wemade.infrastructure.parser.mapper.AnalysisLogRowMapper;
import com.wemade.infrastructure.parser.AnalysisParser;
import com.wemade.infrastructure.parser.dto.AnalysisLogRow;
import com.wemade.infrastructure.parser.dto.ParsedLogLine;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class AnalysisExecutor {
    private static final Logger log = LoggerFactory.getLogger(AnalysisExecutor.class);
    private static final long MAX_LINES = 200_000;

    private final AnalysisParser analysisParser;
    private final AnalysisLogRowMapper rowMapper = new AnalysisLogRowMapper();

    public AnalysisExecutor(AnalysisParser analysisParser) {
        this.analysisParser = analysisParser;
    }

    public void run(MultipartFile file, Analysis analysis) {
        long rowNo = 0;

        try (CSVParser parser = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .build()
                .parse(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            for (CSVRecord record : parser) {
                rowNo++;

                if (rowNo > MAX_LINES) {
                    throw new ParserException(
                            ErrorCode.PARSE_LINE_EXCEEDED,
                            "too many lines: " + rowNo + " (max 200k)"
                    );
                }

                analysis.getParseReport().addLine();

                try {
                    AnalysisLogRow row = rowMapper.map(record);
                    ParsedLogLine parsed = analysisParser.parse(row);
                    applyParsedLine(analysis, parsed);

                } catch (ParserException e) {
                    analysis.getParseReport().addError(record.toString());

                    log.warn(
                            "event=parse_error analysisId={} rowNo={} errorCode={} message={}",
                            analysis.getId(),
                            rowNo,
                            e.getErrorCode().name(),
                            e.getMessage()
                    );
                }
            }

            long totalRequests =
                    analysis.getParseReport().getTotalCount()
                            - analysis.getParseReport().getErrorCount();

            analysis.getStatistics().setTotalRequests(totalRequests);

        } catch (ParserException e) {
            log.error(
                    "event=analysis_failed analysisId={} errorCode={} message={}",
                    analysis.getId(),
                    e.getErrorCode().name(),
                    e.getMessage()
            );
            throw e;

        } catch (Exception e) {
            log.error(
                    "event=analysis_failed analysisId={} message={}",
                    analysis.getId(),
                    safeMessage(e)
            );
            throw new ParserException(ErrorCode.PARSE_FAILED, e.getMessage());
        }
    }


    private void applyParsedLine(Analysis analysis, ParsedLogLine parsed) {
        analysis.getStatistics().incrementIp(parsed.ip());
        analysis.getStatistics().incrementPath(parsed.path());
        analysis.getStatistics().incrementStatus(parsed.statusCode());
        analysis.getStatistics().incrementStatusGroup(parsed.statusCode());
    }

    private static String safeMessage(Exception e) {
        String msg = e.getMessage();
        return (msg == null || msg.isBlank()) ? e.getClass().getSimpleName() : msg;
    }

}
