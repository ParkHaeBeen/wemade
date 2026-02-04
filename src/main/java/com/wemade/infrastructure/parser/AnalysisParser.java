package com.wemade.infrastructure.parser;

import com.wemade.common.exception.ErrorCode;
import com.wemade.common.exception.ParserException;
import com.wemade.infrastructure.parser.dto.AnalysisLogRow;
import com.wemade.infrastructure.parser.dto.ParsedLogLine;
import org.springframework.stereotype.Component;

@Component
public class AnalysisParser {
    public ParsedLogLine parse(AnalysisLogRow row) {
        String ip = row.clientIp();
        String path = row.requestUri();
        String status = row.httpStatus();

        if (ip == null || ip.isBlank()) {
            throw new ParserException(
                    ErrorCode.PARSE_INVALID_FORMAT,
                    "ClientIp is blank"
            );
        }

        if (path == null || path.isBlank()) {
            throw new ParserException(
                    ErrorCode.PARSE_INVALID_FORMAT,
                    "RequestUri is blank"
            );
        }

        return new ParsedLogLine(ip.trim(), path.trim(), status);
    }
}
