package com.wemade.service;

import com.wemade.common.aop.AnalysisLogging;
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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class AnalysisExecutor {
    private static final long MAX_LINES = 200_000;

    private final AnalysisParser parser;
    private final AnalysisLogRowMapper rowMapper;

    public AnalysisExecutor(AnalysisParser parser, AnalysisLogRowMapper rowMapper) {
        this.parser = parser;
        this.rowMapper = rowMapper;
    }

    @AnalysisLogging
    public void run(MultipartFile file, Analysis analysis) {
        long rowNo = 0;

        try (CSVParser csvParser = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .build()
                .parse(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            for (CSVRecord record : csvParser) {
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
                    ParsedLogLine parsed = parser.parse(row);
                    applyParsedLine(analysis, parsed);

                } catch (ParserException e) {
                    analysis.getParseReport().addError(record.toString());
                }
            }

            long totalRequests =
                    analysis.getParseReport().getTotalCount()
                            - analysis.getParseReport().getErrorCount();

            analysis.getStatistics().setTotalRequests(totalRequests);

        } catch (ParserException e) {
            throw e;

        } catch (Exception e) {
            throw new ParserException(ErrorCode.PARSE_FAILED, e.getMessage());
        }
    }


    private void applyParsedLine(Analysis analysis, ParsedLogLine parsed) {
        analysis.getStatistics().incrementIp(parsed.ip());
        analysis.getStatistics().incrementPath(parsed.path());
        analysis.getStatistics().incrementStatus(parsed.statusCode());
        analysis.getStatistics().incrementStatusGroup(parsed.statusCode());
    }
}
