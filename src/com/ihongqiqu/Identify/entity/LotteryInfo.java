package com.ihongqiqu.Identify.entity;

/**
 * 彩票种类信息
 * <p/>
 * Created by zhenguo on 9/4/15.
 */
public class LotteryInfo extends BaseEntity {
    /**
     * lotteryCode : fc3d lotteryName : 福彩3d
     */

    private String lotteryCode;
    private String lotteryName;

    public void setLotteryCode(String lotteryCode) { this.lotteryCode = lotteryCode;}

    public void setLotteryName(String lotteryName) { this.lotteryName = lotteryName;}

    public String getLotteryCode() { return lotteryCode;}

    public String getLotteryName() { return lotteryName;}
}
