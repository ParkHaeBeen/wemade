package com.wemade.domain;

public class IpInfo {
    private final String ip;
    private final String country;
    private final String region;
    private final String city;
    private final String asn;
    private final String asDomain;

    public IpInfo(String ip, String country, String region, String city, String asn, String asDomain) {
        this.ip = ip;
        this.country = country;
        this.region = region;
        this.city = city;
        this.asn = asn;
        this.asDomain = asDomain;
    }

    public String getIp() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getAsn() {
        return asn;
    }

    public String getAsDomain() {
        return asDomain;
    }
}
