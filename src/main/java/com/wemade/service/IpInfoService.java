package com.wemade.service;

import com.wemade.domain.IpInfo;
import com.wemade.infrastructure.client.IpInfoGeoApi;
import com.wemade.infrastructure.client.IpInfoLiteApi;
import com.wemade.infrastructure.client.dto.IpInfoApiAsnResponse;
import com.wemade.infrastructure.client.dto.IpInfoApiGeoResponse;
import com.wemade.infrastructure.persistence.IpInfoCacheRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IpInfoService {
    private final IpInfoLiteApi ipInfoLiteApi;
    private final IpInfoGeoApi ipInfoGeoApi;
    private final IpInfoCacheRepository ipInfoCacheRepository;

    public IpInfoService(IpInfoLiteApi ipInfoLiteApi, IpInfoGeoApi ipInfoGeoApi, IpInfoCacheRepository ipInfoCacheRepository) {
        this.ipInfoLiteApi = ipInfoLiteApi;
        this.ipInfoGeoApi = ipInfoGeoApi;
        this.ipInfoCacheRepository = ipInfoCacheRepository;
    }

    public IpInfo read(String ip) {
        Optional<IpInfo> cached = ipInfoCacheRepository.findByIP(ip);
        if (cached.isPresent()) {
            return cached.get();
        }

        IpInfoApiAsnResponse asn = ipInfoLiteApi.getAsn(ip);
        IpInfoApiGeoResponse geo = ipInfoGeoApi.getGeo(ip);
        return ipInfoCacheRepository.save(ip, toDomain(asn, geo));
    }

    private IpInfo toDomain(IpInfoApiAsnResponse asn, IpInfoApiGeoResponse geo) {
        String ip = asn.ip();
        String country = geo.country();
        String region  = geo.region();
        String city    = geo.city();
        String asnValue   = asn.asn();
        String asDomain   = asn.asDomain();

        return new IpInfo(ip,country, region, city, asnValue, asDomain);
    }

}
