package com.ihongqiqu.Identify.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.R;
import com.ihongqiqu.Identify.entity.SwitchInfo;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import java.io.File;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    private final String URL_SWITCHES = "https://raw.githubusercontent.com/jingle1267/Identify/master/api/switch.json";

    @Bind(R.id.ll_sign)
    LinearLayout llSign;
    @Bind(R.id.ll_translation)
    LinearLayout llTranslation;
    @Bind(R.id.ll_lottery)
    LinearLayout llLottery;
    @Bind(R.id.ll_phone)
    LinearLayout llPhone;
    @Bind(R.id.ll_id)
    LinearLayout llId;
    @Bind(R.id.ll_ip)
    LinearLayout llIp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("查询工具列表");
        }
        // toolbar.setNavigationIcon(R.drawable.ic_launcher);

        slidingPaneLayout.removeViewAt(0);

        requestSwitch();

        checkVersion();
    }

    private void requestSwitch() {
        showProgressDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SWITCHES, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                SwitchInfo switchInfo = new Gson().fromJson(s, SwitchInfo.class);
                if (switchInfo != null) {
                    if (!switchInfo.getIsShowSign()) {
                        llSign.setVisibility(View.GONE);
                    } else {
                        llSign.setVisibility(View.VISIBLE);
                    }

                    if (!switchInfo.getIsShowTranslate()) {
                        llTranslation.setVisibility(View.GONE);
                    } else {
                        llTranslation.setVisibility(View.VISIBLE);
                    }
                    if (!switchInfo.getIsShowLottery()) {
                        llLottery.setVisibility(View.GONE);
                    } else {

                    }
                    if (!switchInfo.getIsShowPhone()) {
                        llPhone.setVisibility(View.GONE);
                    } else {
                        llPhone.setVisibility(View.VISIBLE);
                    }
                    if (!switchInfo.getIsShowID()) {
                        llId.setVisibility(View.GONE);
                    } else {
                        llId.setVisibility(View.VISIBLE);
                    }
                    if (!switchInfo.getIsShowIP()) {
                        llIp.setVisibility(View.GONE);
                    } else {
                        llIp.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "接口返回出错", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideProgressDialog();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void checkVersion() {
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                if (updateStatus == 0 && updateInfo != null) {
                    showUpdateDialog(updateInfo);
                }
                // case 0: // has update
                // case 1: // has no update
                // case 2: // none wifi
                // case 3: // time out
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

                            UmengUpdateAgent.startInstall(MainActivity.this, new File(s));
                        }
                    });
                    UmengUpdateAgent.startDownload(MainActivity.this, updateInfo);
                } catch (Exception ex) {

                }
            }
        }).setNegativeButton("取消", null);

        if (!isFinishing())
            updateAlertDialog.show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_id:
                IdActivity.launch(MainActivity.this);
                break;
            case R.id.tv_phone:
                PhoneActivity.launch(MainActivity.this);
                break;
            case R.id.tv_ip:
                IpActivity.launch(MainActivity.this);
                break;
            case R.id.tv_lottery:
                LotteryActivity.launch(MainActivity.this);
                break;
            case R.id.tv_translation:
                DictionaryActivity.launch(MainActivity.this);
                break;
            case R.id.tv_sign:
                SignActivity.launch(MainActivity.this);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SettingActivity.launch(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("温馨提示");
        // builder.setCustomTitle(title);
        builder.setMessage("您确定要退出ID查询吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                overridePendingTransition(0, R.anim.sliding_out_right);
                // MobclickAgent.onKillProcess(context)
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
