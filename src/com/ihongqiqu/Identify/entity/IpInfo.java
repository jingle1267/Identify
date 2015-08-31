package com.ihongqiqu.Identify.entity;

/**
 * IP地址信息实体
 * <p/>
 * Created by zhenguo on 8/30/15.
 */
public class IpInfo extends BaseEntity {

    /**
     * region : 东城区 regionCode : 110101 cityCode : 110000 proCode : 110000 pro : 北京市 addr : 北京市东城区
     * 电信 regionNames : city : 北京市 ip : 103.16.127.8
     */
    private String region;
    private String regionCode;
    private String cityCode;
    private String proCode;
    private String pro;
    private String addr;
    private String regionNames;
    private String city;
    private String ip;

    public void setRegion(String region) { this.region = region;}

    public void setRegionCode(String regionCode) { this.regionCode = regionCode;}

    public void setCityCode(String cityCode) { this.cityCode = cityCode;}

    public void setProCode(String proCode) { this.proCode = proCode;}

    public void setPro(String pro) { this.pro = pro;}

    public void setAddr(String addr) { this.addr = addr;}

    public void setRegionNames(String regionNames) { this.regionNames = regionNames;}

    public void setCity(String city) { this.city = city;}

    public void setIp(String ip) { this.ip = ip;}

    public String getRegion() { return region;}

    public String getRegionCode() { return regionCode;}

    public String getCityCode() { return cityCode;}

    public String getProCode() { return proCode;}

    public String getPro() { return pro;}

    public String getAddr() { return addr;}

    public String getRegionNames() { return regionNames;}

    public String getCity() { return city;}

    public String getIp() { return ip;}
}
