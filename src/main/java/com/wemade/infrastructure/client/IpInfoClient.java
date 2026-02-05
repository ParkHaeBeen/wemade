package com.wemade.infrastructure.client;

import com.wemade.infrastructure.client.dto.IpInfoApiAsnResponse;
import com.wemade.infrastructure.client.dto.IpInfoApiGeoResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Component
public class IpInfoClient {

    private final IpInfoLiteApi ipInfoLiteApi;
    private final IpInfoGeoApi ipInfoGeoApi;

    public IpInfoClient(IpInfoLiteApi ipInfoLiteApi, IpInfoGeoApi ipInfoGeoApi) {
        this.ipInfoLiteApi = ipInfoLiteApi;
        this.ipInfoGeoApi = ipInfoGeoApi;
    }

    @Retryable(
            retryFor = { ResourceAccessException.class, HttpServerErrorException.class },
            maxAttempts = 2,
            backoff = @Backoff(delay = 200, multiplier = 2.0)
    )
    public IpInfoApiAsnResponse getAsn(String ip) {
        return ipInfoLiteApi.getAsn(ip);
    }

    @Recover
    public IpInfoApiAsnResponse recoverAsn(Exception e, String ip) {
        return IpInfoApiAsnResponse.unknown(ip);
    }

    @Retryable(
            retryFor = { ResourceAccessException.class, HttpServerErrorException.class },
            maxAttempts = 2,
            backoff = @Backoff(delay = 200, multiplier = 2.0)
    )
    public IpInfoApiGeoResponse getGeo(String ip) {
        return ipInfoGeoApi.getGeo(ip);
    }

    @Recover
    public IpInfoApiGeoResponse recoverGeo(Exception e, String ip) {
        return IpInfoApiGeoResponse.unknown();
    }
}
