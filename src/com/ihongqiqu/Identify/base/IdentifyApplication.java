package com.ihongqiqu.Identify.base;

import android.app.Application;
import android.util.Log;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.utils.ChannelUtil;
import com.umeng.analytics.AnalyticsConfig;

/**
 * 全局内容
 *
 * Created by zhenguo on 8/25/15.
 */
public class IdentifyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 设置友盟统计
        AnalyticsConfig.setAppkey("55e156b8e0f55a423500168c");
        AnalyticsConfig.setChannel(ChannelUtil.getChannel(this));

        if (BuildConfig.DEBUG)
            Log.d("IdentifyApplication", ChannelUtil.getChannel(this));
    }

}
