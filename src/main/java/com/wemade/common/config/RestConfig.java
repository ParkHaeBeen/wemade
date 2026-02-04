package com.wemade.common.config;

import com.wemade.common.properties.IpInfoProperties;
import com.wemade.infrastructure.client.IpInfoGeoApi;
import com.wemade.infrastructure.client.IpInfoLiteApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import java.util.Map;

@Configuration
public class RestConfig {
    @Bean
    RestClient ipInfoLiteRestClient(IpInfoProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.liteUrl())
                .defaultUriVariables(Map.of("token", properties.token()))
                .build();
    }

    @Bean
    RestClient ipInfoGeoRestClient(IpInfoProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.geoUrl())
                .defaultUriVariables(Map.of("token", properties.token()))
                .build();
    }

    @Bean
    IpInfoLiteApi ipInfoLiteApi(RestClient ipInfoLiteRestClient) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(ipInfoLiteRestClient))
                .build()
                .createClient(IpInfoLiteApi.class);
    }

    @Bean
    IpInfoGeoApi ipInfoGeoApi(RestClient ipInfoGeoRestClient) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(ipInfoGeoRestClient))
                .build()
                .createClient(IpInfoGeoApi.class);
    }
}