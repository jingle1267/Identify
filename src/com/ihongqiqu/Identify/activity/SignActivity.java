package com.ihongqiqu.Identify.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.R;
import com.ihongqiqu.Identify.entity.SignInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.Locale;

/**
 * 每日一签
 * <p/>
 * Created by zhenguo on 9/8/15.
 */
public class SignActivity extends BaseActivity {

    public static void launch(@NonNull Activity from) {
        Intent intent = new Intent(from, SignActivity.class);
        from.startActivity(intent);
    }

    private final String URL_SIGN = "http://open.iciba.com/dsapi/";

    @Bind(R.id.iv_picture)
    ImageView ivPicture;
    @Bind(R.id.iv_bg_bottom)
    ImageView ivBgBottom;
    @Bind(R.id.iv_bg_top)
    ImageView ivBgTop;
    @Bind(R.id.tv_day)
    TextView tvDay;
    @Bind(R.id.tv_year)
    TextView tvYear;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_note)
    TextView tvNote;
    @Bind(R.id.tv_translation)
    TextView tvTranslation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);

        toolbar.setVisibility(View.GONE);
        requestSignInfo();
    }

    private void requestSignInfo() {
        showProgressDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(SignActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SIGN, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (BuildConfig.DEBUG)
                    Log.d("SignActivity", "onResponse : " + s);
                if (!TextUtils.isEmpty(s)) {
                    SignInfo signInfo = new Gson().fromJson(s, SignInfo.class);
                    if (signInfo != null) {
                        if (!TextUtils.isEmpty(signInfo.getContent())) {
                            tvContent.setText(signInfo.getContent());
                        }
                        if (!TextUtils.isEmpty(signInfo.getNote())) {
                            tvNote.setText(signInfo.getNote());
                        }
                        if (!TextUtils.isEmpty(signInfo.getTranslation())) {
                            tvTranslation.setText(signInfo.getTranslation());
                        }
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        if (!TextUtils.isEmpty(signInfo.getDateline())) {
                            try {
                                date = dateFormat.parse(signInfo.getDateline());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        dateFormat = new SimpleDateFormat("MMM.yyyy", Locale.US);
                        String yearAndMonth = dateFormat.format(date);
                        dateFormat = new SimpleDateFormat("dd");
                        String day = dateFormat.format(date);
                        if (BuildConfig.DEBUG) {
                            Log.d("SignActivity", "dat : " + day);
                            Log.d("SignActivity", "yearAndMonth : " + yearAndMonth);
                            Log.d("SignActivity", "dateline : " + signInfo.getDateline());

                            Log.d("SignActivity",
                                "date : " + date.getYear() + "-" + date.getMonth() + "-"
                                    + date.getDay());
                        }
                        tvDay.setText(day);
                        tvYear.setText(yearAndMonth);
                        String imgUrl = null;
                        if (!TextUtils.isEmpty(signInfo.getPicture2())) {
                            imgUrl = signInfo.getPicture2();
                        }
                        if (TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(signInfo.getPicture())) {
                            imgUrl = signInfo.getPicture();
                        }
                        if (!TextUtils.isEmpty(imgUrl)) {
                            Glide.with(SignActivity.this).load(imgUrl).into(ivPicture);
                        }
                    } else {
                        Toast.makeText(SignActivity.this, "接口返回出错", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignActivity.this, "接口返回出错", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SignActivity.this, "接口返回出错", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });

        requestQueue.add(stringRequest);
    }

}
