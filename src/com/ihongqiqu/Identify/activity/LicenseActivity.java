package com.ihongqiqu.Identify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import com.ihongqiqu.Identify.R;
import com.ihongqiqu.Identify.base.BaseActivity;

/**
 * 用户协议
 * <p/>
 * Created by zhenguo on 9/10/15.
 */
public class LicenseActivity extends BaseActivity {

    public static void launch(@NonNull Activity from) {
        Intent intent = new Intent(from, LicenseActivity.class);
        from.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        setTitle("用户协议");
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }
}
