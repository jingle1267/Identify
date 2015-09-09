package com.ihongqiqu.Identify.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.R;
import com.ihongqiqu.Identify.utils.NavigationUtils;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;

/**
 * 基础Activity类
 * <p/>
 * Created by zhenguo on 8/25/15.
 */
public class BaseActivity extends AppCompatActivity {

    SlidingPaneLayout slidingPaneLayout;
    LinearLayout layoutRoot;
    public Toolbar toolbar;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasNavagationBar = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (NavigationUtils.checkDeviceHasNavigationBar(BaseActivity.this)) {
                hasNavagationBar = true;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_base);
        slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.slidingPaneLayout);
        layoutRoot = (LinearLayout) findViewById(R.id.layout_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (hasNavagationBar) {
            int navigationBarHeight = NavigationUtils.getNavigationBarHeight(BaseActivity.this);
            if (BuildConfig.DEBUG)
                Log.d("BaseActivity", "navigationBarHeight : " + navigationBarHeight);
            layoutRoot.setPadding(0, 0, 0, navigationBarHeight);
            if (isWindowTranslucentNavigation()) {
                layoutRoot.setBackgroundColor(getResources().getColor(R.color.app_color));
            } else {
                layoutRoot.setBackgroundColor(getResources().getColor(android.R.color.black));
            }
        }

        if (BuildConfig.DEBUG)
            Log.d("BaseActivity", "onCreate");

        // 通过反射来改变SlidingPanelayout的值
        try {
            //属性
            Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            f_overHang.setAccessible(true);
            f_overHang.set(slidingPaneLayout, 0);

            slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View view, float v) {
                    if (BuildConfig.DEBUG)
                        Log.d("BaseActivity", "onPanelSlide");
                    hideKeyboard();
                }

                @Override
                public void onPanelOpened(View view) {
                    if (BuildConfig.DEBUG)
                        Log.d("BaseActivity", "onPanelOpened");
                    //菜单打开后，我们结束掉这个Activity
                    finish();
                    BaseActivity.this.overridePendingTransition(0, R.anim.sliding_out_right);
                }

                @Override
                public void onPanelClosed(View view) {
                    if (BuildConfig.DEBUG)
                        Log.d("BaseActivity", "onPanelClosed");
                }
            });
            slidingPaneLayout.setSliderFadeColor(Color.TRANSPARENT);
            // slidingPaneLayout.setCoveredFadeColor(getResources().getColor(R.color.divider_color));
            slidingPaneLayout.setShadowResourceLeft(R.drawable.drawer_shadow);
        } catch (Exception e) {
            e.printStackTrace();
            if (BuildConfig.DEBUG)
                Log.d("BaseActivity", "反射失败");
        }

        // ButterKnife.bind(this);
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags =
                (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setSupportActionBar(toolbar);

    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (layoutRoot == null) {
            return;
        }
        layoutRoot.addView(view, params);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransition(R.anim.sliding_in_right, R.anim.sliding_out_right);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.sliding_in_right, R.anim.sliding_out_right);
    }

    public boolean isWindowTranslucentNavigation() {
        return true;
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("正在加载，请稍等...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && getCurrentFocus().getApplicationWindowToken() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.sliding_out_right);
    }

}
