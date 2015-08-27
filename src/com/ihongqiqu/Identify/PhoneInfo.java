package com.ihongqiqu.Identify;

/**
 * 手机归属地信息
 * <p/>
 * Created by zhenguo on 8/25/15.
 */
public class PhoneInfo {

    /**
     * telString : 15846530170 province : 黑龙江 carrier : 黑龙江移动
     */
    private String telString;
    private String province;
    private String carrier;

    public void setTelString(String telString) { this.telString = telString;}

    public void setProvince(String province) { this.province = province;}

    public void setCarrier(String carrier) { this.carrier = carrier;}

    public String getTelString() { return telString;}

    public String getProvince() { return province;}

    public String getCarrier() { return carrier;}
}
