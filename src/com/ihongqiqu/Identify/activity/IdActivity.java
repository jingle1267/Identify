package com.ihongqiqu.Identify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.gson.Gson;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.R;
import com.ihongqiqu.Identify.base.BaseActivity;
import com.ihongqiqu.Identify.entity.IdInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 身份证查询
 * <p/>
 * Created by zhenguo on 8/25/15.
 */
public class IdActivity extends BaseActivity {

    public static void launch(@NonNull Activity from) {
        Intent intent = new Intent(from, IdActivity.class);
        from.startActivity(intent);
    }

    private final String URL_QUERY_ID = "http://apis.baidu.com/apistore/idservice/id";

    @Bind(R.id.et_id)
    EditText etId;
    @Bind(R.id.tv_id_invalid)
    TextView tvIdInvalid;
    @Bind(R.id.iv_id_clear)
    ImageView ivIdClear;
    @Bind(R.id.btn_query)
    Button btnQuery;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
        ButterKnife.bind(this);

        setTitle("查询身份证信息");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        etId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    ivIdClear.setVisibility(View.VISIBLE);
                    tvIdInvalid.setVisibility(View.INVISIBLE);
                } else {
                    ivIdClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_query:
                queryId(etId.getText().toString().trim());
                break;
            case R.id.iv_id_clear:
                etId.setText("");
                break;
        }
    }

    private void queryId(String id) {
        if (TextUtils.isEmpty(id) || id.length() != 18) {
            tvIdInvalid.setVisibility(View.VISIBLE);
            return;
        } else {
            tvIdInvalid.setVisibility(View.INVISIBLE);
        }
        hideKeyboard();
        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
            URL_QUERY_ID + "?id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (BuildConfig.DEBUG)
                    Log.d("IdActivity", "onResponse : " + s);
                try {
                    JSONObject result = new JSONObject(s);
                    String msg = result.optString("retMsg");
                    if (TextUtils.isEmpty(msg)) {
                        msg = "请求出错";
                    }

                    if (result.optInt("errNum") == 0) {
                        if (!TextUtils.isEmpty(result.optString("retData"))) {
                            IdInfo idInfo =
                                new Gson().fromJson(result.optJSONObject("retData").toString(), IdInfo.class);
                            if (idInfo != null) {
                                tvSex.setText("性别：" + ("M".equals(idInfo.getSex()) ? "男" : "女"));
                                tvAddress.setText("地址：" + idInfo.getAddress());
                                tvBirthday.setText("生日：" + idInfo.getBirthday());
                            }
                        } else {
                            Toast.makeText(IdActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(IdActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("apikey", "0d38f4d9aa9ab1d420816f84c18d3a73");
                return param;
            }
        };

        addRequest(stringRequest);
    }

}
