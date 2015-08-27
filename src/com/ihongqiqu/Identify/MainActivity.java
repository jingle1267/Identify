package com.ihongqiqu.Identify;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_id:
                IdActivity.launch(MainActivity.this);
                break;
            case R.id.tv_phone:
                PhoneActivity.launch(MainActivity.this);
                break;
        }
    }

}
