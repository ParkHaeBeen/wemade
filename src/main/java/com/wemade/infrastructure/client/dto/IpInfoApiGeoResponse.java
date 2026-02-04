package com.wemade.infrastructure.client.dto;

public record IpInfoApiGeoResponse(
        String city,
        String country,
        String region
) {
}
