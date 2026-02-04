package com.wemade.infrastructure.client;


import com.wemade.infrastructure.client.dto.IpInfoApiAsnResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface IpInfoLiteApi {
    @GetExchange("/lite/{ip}?token={token}")
    IpInfoApiAsnResponse getAsn(
            @PathVariable String ip
    );

}
