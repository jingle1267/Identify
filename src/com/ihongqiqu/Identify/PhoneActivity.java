package com.ihongqiqu.Identify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 手机号码查询
 * <p/>
 * Created by zhenguo on 8/25/15.
 */
public class PhoneActivity extends BaseActivity {

    private final String URL_PHONE_INFO =
        "http://apis.baidu.com/apistore/mobilephoneservice/mobilephone";

    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_phone_invalid)
    TextView tvPhoneInvalid;
    @Bind(R.id.iv_phone_clear)
    ImageView ivPhoneClear;
    @Bind(R.id.btn_query)
    Button btnQuery;
    @Bind(R.id.tv_carrier)
    TextView tvCarrier;
    @Bind(R.id.tv_province)
    TextView tvProvince;

    public static void launch(Activity from) {
        Intent intent = new Intent(from, PhoneActivity.class);
        from.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_query:
                requestPhoneInfo(etPhone.getText().toString().trim());
                break;
        }
    }

    private void requestPhoneInfo(final String tel) {
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PhoneActivity", "Volley : " + error.getMessage(), error);
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