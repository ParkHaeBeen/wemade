package com.wemade.infrastructure.persistence;

import com.wemade.domain.IpInfo;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class IpInfoCacheRepository {
    private final ConcurrentHashMap<String, IpInfo> store = new ConcurrentHashMap<>();

    public Optional<IpInfo> findByIP(String ip) {
        return Optional.ofNullable(store.get(ip));
    }

    public IpInfo save(String ip, IpInfo ipinfo) {
        store.put(ip, ipinfo);
        return ipinfo;
    }
}
