package com.wemade.infrastructure.client;

import com.wemade.infrastructure.client.dto.IpInfoApiGeoResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface IpInfoGeoApi {
    @GetExchange("/{ip}/geo?token={token}")
    IpInfoApiGeoResponse getGeo(
            @PathVariable String ip
    );
}
