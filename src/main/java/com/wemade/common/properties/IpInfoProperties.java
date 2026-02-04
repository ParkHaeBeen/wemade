package com.wemade.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ipinfo")
public record IpInfoProperties(
        String token,
        String geoUrl,
        String liteUrl
) {
}
