package com.ihongqiqu.Identify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.R;
import com.ihongqiqu.Identify.entity.IpInfo;

/**
 * IP地址信息查询
 * <p/>
 * Created by zhenguo on 8/30/15.
 */
public class IpActivity extends BaseActivity {

    private final String URL_IP_INFO = "http://whois.pconline.com.cn/ipJson.jsp?json=true";

    @Bind(R.id.et_ip)
    EditText etIp;
    @Bind(R.id.tv_ip_invalid)
    TextView tvIpInvalid;
    @Bind(R.id.iv_ip_clear)
    ImageView ivIpClear;
    @Bind(R.id.tv_ip)
    TextView tvIp;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_postcode)
    TextView tvPostcode;

    public static void launch(Activity from) {
        Intent intent = new Intent(from, IpActivity.class);
        from.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("查询IP地址信息");
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etIp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    ivIpClear.setVisibility(View.VISIBLE);
                    tvIpInvalid.setVisibility(View.INVISIBLE);
                } else {
                    ivIpClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_query:
                requestIpInfo(etIp.getText().toString());
                break;
            case R.id.iv_id_clear:
                etIp.setText("");
                break;
        }
    }

    private void requestIpInfo(String ip) {
        if (!TextUtils.isEmpty(ip)) {
            if (!isIp(ip)) {
                tvIpInvalid.setVisibility(View.VISIBLE);
                return;
            } else {
                tvIpInvalid.setVisibility(View.INVISIBLE);
            }
        } else {
            tvIpInvalid.setVisibility(View.INVISIBLE);
        }
        showProgressDialog();

        String url = URL_IP_INFO;
        if (!TextUtils.isEmpty(ip)) {
            url += "&ip=" + ip;
        }

        if (BuildConfig.DEBUG)
            Log.d("IpActivity", "url : " + url);

        RequestQueue mQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
            url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (BuildConfig.DEBUG)
                    Log.d("IpActivity", ("onResponse response : " + response));
                String msg = "接口请求出错";
                if (BuildConfig.DEBUG)
                    Log.d("IpActivity", "response : " + response);
                if (!TextUtils.isEmpty(response)) {
                    IpInfo ipInfo = new Gson().fromJson(response, IpInfo.class);
                    if (ipInfo != null) {
                        String ipStr = "IP：" + ipInfo.getIp();
                        if (TextUtils.isEmpty(etIp.getText().toString())) {
                            ipStr += "(本机IP)";
                        }
                        tvIp.setText(ipStr);
                        tvAddress.setText("地址：" + ipInfo.getAddr());
                        tvPostcode.setText("邮编：" + ipInfo.getRegionCode());
                    }
                } else {
                    Toast.makeText(IpActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (BuildConfig.DEBUG)
                    Log.d("IpActivity", ("onErrorResponse errMsg : " + error.getMessage()));
                hideProgressDialog();
                Toast.makeText(IpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        // System.setProperty("http.keepAlive", "false");
        mQueue.add(stringRequest);
    }

    private boolean isIp(String ip) {
        if (!TextUtils.isEmpty(ip) && ip.length() <= 15 && ip.length() >= 7) {
            int count = 0;
            char[] chars = ip.toCharArray();
            for (char aChar : chars) {
                if (aChar == '.') {
                    count++;
                }
            }
            return count == 3;
        } else {
            return false;
        }
    }

}
