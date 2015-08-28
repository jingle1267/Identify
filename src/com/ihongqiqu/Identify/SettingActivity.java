package com.ihongqiqu.Identify;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * 设置
 * <p/>
 * Created by zhenguo on 8/29/15.
 */
public class SettingActivity extends BaseActivity {

    public static void launch(Activity from) {
        Intent intent = new Intent(from, SettingActivity.class);
        from.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("设置");
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_developer:
                sendMail();
                break;
            case R.id.tv_contact:
                browserHomePage();
                break;
            case R.id.tv_share:
                share();
                break;
        }
    }

    private void share() {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "强烈推荐");
        intent.putExtra(Intent.EXTRA_TEXT, "分享一款ID查询工具应用，下载地址 http://fir.im/idtool");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "ID工具箱分享"));
    }

    private void browserHomePage() {Uri uri = Uri.parse("https://github.com/jingle1267");
        Intent intent = new  Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void sendMail() {Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:jingle1267@163.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "ID查询工具箱");
        data.putExtra(Intent.EXTRA_TEXT, "我正在使用ID查询工具箱，我想要...");
        startActivity(data);
    }

}
