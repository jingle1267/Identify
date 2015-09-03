package com.ihongqiqu.Identify.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.R;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import java.io.File;

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
            case R.id.tv_homepage:
                HtmlActivity.launch(SettingActivity.this, "http://ihongqiqu.com/");
                break;
            case R.id.tv_share:
                share();
                break;
            case R.id.tv_update:
                checkVersion();
                break;
        }
    }

    private void checkVersion() {
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case 0: // has update
                        if (updateInfo != null) {
                            showUpdateDialog(updateInfo);
                        } else {
                            Toast.makeText(SettingActivity.this, "服务器出小差", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1: // has no update
                        Toast.makeText(SettingActivity.this, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                        break;
                    case 2: // none wifi
                        Toast.makeText(SettingActivity.this, "网络连接出现问题", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: // time out
                        Toast.makeText(SettingActivity.this, "请求超时", Toast.LENGTH_SHORT).show();
                        break;
                    default:

                        break;
                }
            }
        });
        UmengUpdateAgent.update(this);
    }

    private void showUpdateDialog(final UpdateResponse updateInfo) {
        AlertDialog.Builder updateAlertDialog = new AlertDialog.Builder(this);
        updateAlertDialog.setIcon(R.drawable.ic_launcher);
        updateAlertDialog.setTitle("发现新版本");
        updateAlertDialog.setMessage(updateInfo.updateLog);
        updateAlertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    if (BuildConfig.DEBUG)
                        Log.d("MainActivity", "onClick");
                        /*startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse(updateInfo.path)));*/
                    UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {
                        @Override
                        public void OnDownloadStart() {
                            if (BuildConfig.DEBUG)
                                Log.d("MainActivity", "OnDownloadStart");
                        }

                        @Override
                        public void OnDownloadUpdate(int i) {
                            if (BuildConfig.DEBUG)
                                Log.d("MainActivity", "");
                        }

                        @Override
                        public void OnDownloadEnd(int i, String s) {
                            if (BuildConfig.DEBUG)
                                Log.d("MainActivity", "OnDownloadEnd");

                            UmengUpdateAgent.startInstall(SettingActivity.this, new File(s));
                        }
                    });
                    UmengUpdateAgent.startDownload(SettingActivity.this, updateInfo);
                } catch (Exception ex) {

                }
            }
        }).setNegativeButton("取消", null);

        if (!isFinishing())
            updateAlertDialog.show();
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
