package com.wemade.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IpInfoApiAsnResponse(
        String ip,
        String asn,
        @JsonProperty("as_domain")
        String asDomain
) {
   public static IpInfoApiAsnResponse unknown(String ip) {
           return new IpInfoApiAsnResponse(ip, "UNKNOWN", "UNKNOWN");
   }
}
