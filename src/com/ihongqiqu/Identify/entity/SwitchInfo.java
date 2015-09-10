package com.ihongqiqu.Identify.entity;

/**
 * 全局开关
 * <p/>
 * Created by zhenguo on 9/10/15.
 */
public class SwitchInfo extends BaseEntity {

    /**
     * isShowSign : true isShowTranslate : true isShowLottery : true isShowPhone : true isShowID :
     * true isShowIP : true
     */

    private boolean isShowSign;
    private boolean isShowTranslate;
    private boolean isShowLottery;
    private boolean isShowPhone;
    private boolean isShowID;
    private boolean isShowIP;

    public void setIsShowSign(boolean isShowSign) { this.isShowSign = isShowSign;}

    public void setIsShowTranslate(boolean isShowTranslate) {
        this.isShowTranslate = isShowTranslate;
    }

    public void setIsShowLottery(boolean isShowLottery) { this.isShowLottery = isShowLottery;}

    public void setIsShowPhone(boolean isShowPhone) { this.isShowPhone = isShowPhone;}

    public void setIsShowID(boolean isShowID) { this.isShowID = isShowID;}

    public void setIsShowIP(boolean isShowIP) { this.isShowIP = isShowIP;}

    public boolean getIsShowSign() { return isShowSign;}

    public boolean getIsShowTranslate() { return isShowTranslate;}

    public boolean getIsShowLottery() { return isShowLottery;}

    public boolean getIsShowPhone() { return isShowPhone;}

    public boolean getIsShowID() { return isShowID;}

    public boolean getIsShowIP() { return isShowIP;}
}
