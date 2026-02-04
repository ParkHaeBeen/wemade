package com.wemade.infrastructure.parser.mapper;

import com.wemade.common.exception.ErrorCode;
import com.wemade.common.exception.ParserException;
import com.wemade.infrastructure.parser.dto.AnalysisLogRow;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class AnalysisLogRowMapper {

    private static final String TIME = "TimeGenerated [UTC]";
    private static final String TIME_WITH_BOM = "\uFEFFTimeGenerated [UTC]";

    public AnalysisLogRow map(CSVRecord record) {
        return new AnalysisLogRow(
                get(record, TIME),
                record.get("ClientIp"),
                record.get("HttpMethod"),
                record.get("RequestUri"),
                record.get("UserAgent"),
                record.get("HttpStatus"),
                record.get("HttpVersion"),
                record.get("ReceivedBytes"),
                record.get("SentBytes"),
                record.get("ClientResponseTime"),
                record.get("SslProtocol"),
                record.get("OriginalRequestUriWithArgs")
        );
    }


    private String get(CSVRecord record, String key) {
        if (record.isMapped(key)) return record.get(key);
        if (TIME.equals(key) && record.isMapped(TIME_WITH_BOM)) {
            return record.get(TIME_WITH_BOM);
        }
        throw new ParserException(
                ErrorCode.PARSE_INVALID_FORMAT,
                "Missing header: " + key
        );
    }
}
