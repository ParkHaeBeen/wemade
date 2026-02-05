package com.wemade.service;

import com.wemade.domain.IpInfo;
import com.wemade.infrastructure.client.IpInfoClient;
import com.wemade.infrastructure.client.dto.IpInfoApiAsnResponse;
import com.wemade.infrastructure.client.dto.IpInfoApiGeoResponse;
import com.wemade.infrastructure.persistence.IpInfoCacheRepository;
import org.springframework.stereotype.Service;

@Service
public class IpInfoService {
    private final IpInfoClient ipInfoClient;
    private final IpInfoCacheRepository ipInfoCacheRepository;

    public IpInfoService(IpInfoClient ipInfoClient, IpInfoCacheRepository ipInfoCacheRepository) {
        this.ipInfoClient = ipInfoClient;
        this.ipInfoCacheRepository = ipInfoCacheRepository;
    }

    public IpInfo read(String ip) {
        return ipInfoCacheRepository.findByIP(ip)
                .orElseGet(() -> {
                    IpInfoApiAsnResponse asn = ipInfoClient.getAsn(ip);
                    IpInfoApiGeoResponse geo = ipInfoClient.getGeo(ip);
                    return ipInfoCacheRepository.save(ip, toDomain(asn, geo));
                });
    }

    private IpInfo toDomain(IpInfoApiAsnResponse asn, IpInfoApiGeoResponse geo) {
        String ip = asn.ip();
        String country = geo.country();
        String region = geo.region();
        String city = geo.city();
        String asnValue = asn.asn();
        String asDomain = asn.asDomain();

        return new IpInfo(ip,country, region, city, asnValue, asDomain);
    }

}
