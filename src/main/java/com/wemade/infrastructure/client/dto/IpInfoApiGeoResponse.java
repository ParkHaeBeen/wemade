package com.wemade.infrastructure.client.dto;

public record IpInfoApiGeoResponse(
        String city,
        String country,
        String region
) {
    public static IpInfoApiGeoResponse unknown() {
        return new IpInfoApiGeoResponse("UNKNOWN", "UNKNOWN", "UNKNOWN");
    }
}
