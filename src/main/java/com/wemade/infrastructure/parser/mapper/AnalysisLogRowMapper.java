package com.wemade.infrastructure.parser.mapper;

import com.wemade.infrastructure.parser.dto.AnalysisLogRow;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class AnalysisLogRowMapper {
    public AnalysisLogRow map(CSVRecord record) {
        return new AnalysisLogRow(
                record.get("TimeGenerated [UTC]"),
                record.get("ClientIp"),
                record.get("HttpMethod"),
                record.get("RequestUri"),
                record.get("UserAgent"),
                Integer.parseInt(record.get("HttpStatus")),
                record.get("HttpVersion"),
                parseLongSafe(record.get("ReceivedBytes")),
                parseLongSafe(record.get("SentBytes")),
                parseLongSafe(record.get("ClientResponseTime")),
                record.get("SslProtocol"),
                record.get("OriginalRequestUriWithArgs")
        );
    }

    private static long parseLongSafe(String v) {
        if (v == null || v.isBlank()) return 0L;
        return Long.parseLong(v.trim());
    }
}
