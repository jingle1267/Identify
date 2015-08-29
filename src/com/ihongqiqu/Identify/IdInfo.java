package com.ihongqiqu.Identify;

/**
 * 身份证信息
 *
 * Created by zhenguo on 8/27/15.
 */
public class IdInfo extends BaseEntity{

    /**
     * birthday : 1987-04-20
     * sex : M
     * address : 湖北省孝感市汉川市
     */
    private String birthday;
    private String sex;
    private String address;

    public void setBirthday(String birthday) { this.birthday = birthday;}

    public void setSex(String sex) { this.sex = sex;}

    public void setAddress(String address) { this.address = address;}

    public String getBirthday() { return birthday;}

    public String getSex() { return sex;}

    public String getAddress() { return address;}
}
