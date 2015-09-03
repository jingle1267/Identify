package com.ihongqiqu.Identify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.R;
import com.ihongqiqu.Identify.entity.PhoneInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机号码查询
 * <p/>
 * Created by zhenguo on 8/25/15.
 */
public class PhoneActivity extends BaseActivity {

    public static void launch(@NonNull Activity from) {
        Intent intent = new Intent(from, PhoneActivity.class);
        from.startActivity(intent);
    }

    private final String URL_PHONE_INFO =
        "http://apis.baidu.com/apistore/mobilephoneservice/mobilephone";

    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_phone_invalid)
    TextView tvPhoneInvalid;
    @Bind(R.id.iv_phone_clear)
    ImageView ivPhoneClear;
    @Bind(R.id.btn_query)
    AppCompatButton btnQuery;
    @Bind(R.id.tv_carrier)
    TextView tvCarrier;
    @Bind(R.id.tv_province)
    TextView tvProvince;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("查询手机号码归属地");
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (etPhone == null) {
            Log.e("PhoneActivity", "etPhone is null");
        } else {
            Log.e("PhoneActivity", "etPhone is not null");
        }

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    ivPhoneClear.setVisibility(View.VISIBLE);
                    tvPhoneInvalid.setVisibility(View.INVISIBLE);
                } else {
                    ivPhoneClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_query:
                requestPhoneInfo(etPhone.getText().toString().trim());
                break;
            case R.id.iv_phone_clear:
                etPhone.setText("");
                break;
        }
    }

    private void requestPhoneInfo(final String tel) {
        if (TextUtils.isEmpty(tel) || !tel.startsWith("1") || tel.length() != 11) {
            tvPhoneInvalid.setVisibility(View.VISIBLE);
            return;
        } else {
            tvPhoneInvalid.setVisibility(View.INVISIBLE);
        }

        hideKeyboard();
        showProgressDialog();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
            URL_PHONE_INFO + "?tel=" + tel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PhoneActivity", "Volley : " + response);
                try {
                    JSONObject retData = new JSONObject(response);
                    String msg = retData.optString("errMsg");
                    if (TextUtils.isEmpty(msg)) {
                        msg = "接口请求出错";
                    }
                    if (retData.optInt("errNum") == 0) {
                        if (BuildConfig.DEBUG)
                            Log.d("PhoneActivity", "retData : " + retData.optString("retData"));
                        if (!TextUtils.isEmpty(retData.optString("retData"))) {
                            PhoneInfo phoneInfo =
                                new Gson().fromJson(retData.optJSONObject("retData").toString(), PhoneInfo.class);
                            if (phoneInfo != null) {
                                tvCarrier.setText("运营商：" + phoneInfo.getCarrier());
                                tvProvince.setText("省  份：" + phoneInfo.getProvince());
                            }
                        } else {
                            Toast.makeText(PhoneActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PhoneActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PhoneActivity", "Volley : " + error.getMessage(), error);
                hideProgressDialog();
            }

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("apikey", "0d38f4d9aa9ab1d420816f84c18d3a73");
                if (BuildConfig.DEBUG)
                    Log.d("PhoneActivity", "apikey : 0d38f4d9aa9ab1d420816f84c18d3a73");
                return map;
            }

        };

        mQueue.add(stringRequest);

    }

}
