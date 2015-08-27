package com.ihongqiqu.Identify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 身份证查询
 *
 * Created by zhenguo on 8/25/15.
 */
public class IdActivity extends BaseActivity {

    public static void launch(Activity from) {
        Intent intent = new Intent(from, IdActivity.class);
        from.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
    }

}
