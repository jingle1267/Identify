package com.ihongqiqu.Identify.entity;

/**
 * 彩票开奖信息
 * <p/>
 * Created by zhenguo on 9/4/15.
 */
public class LotteryDetailInfo extends BaseEntity {
    /**
     * expect : 2015093 openCode : 16,17,25,26,27+02,04 openTime : 2015-08-12 20:38:00 openTimeStamp
     * : 1441197480000
     */

    private String expect;
    private String openCode;
    private String openTime;
    private String openTimeStamp;

    public void setExpect(String expect) { this.expect = expect;}

    public void setOpenCode(String openCode) { this.openCode = openCode;}

    public void setOpenTime(String openTime) { this.openTime = openTime;}

    public void setOpenTimeStamp(String openTimeStamp) { this.openTimeStamp = openTimeStamp;}

    public String getExpect() { return expect;}

    public String getOpenCode() { return openCode;}

    public String getOpenTime() { return openTime;}

    public String getOpenTimeStamp() { return openTimeStamp;}
}
