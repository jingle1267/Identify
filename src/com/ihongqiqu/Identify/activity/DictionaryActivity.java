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
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.ihongqiqu.Identify.BuildConfig;
import com.ihongqiqu.Identify.R;
import com.ihongqiqu.Identify.entity.TranslationInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 翻译
 * <p/>
 * Created by zhenguo on 9/4/15.
 */
public class DictionaryActivity extends BaseActivity {

    public static void launch(@NonNull Activity from) {
        Intent intent = new Intent(from, DictionaryActivity.class);
        from.startActivity(intent);
    }

    private final String URL_TRANSLATE =
        "http://fanyi.youdao.com/openapi.do?keyfrom=ihongqiqu&key=1259015515&type=data&doctype=json&version=1.1&q=";

    @Bind(R.id.et_key)
    EditText etKey;
    @Bind(R.id.btn_translate)
    Button btnTranslate;
    @Bind(R.id.et_translate_result)
    TextView etTranslateResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("翻译");
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 只有调用了该方法，TextView才能不依赖于ScrollView而实现滚动的效果。
         * 要在XML中设置TextView的textcolor，否则，当TextView被触摸时，会灰掉。
         */
        // etTranslateResult.setMovementMethod(ScrollingMovementMethod.getInstance());

        // etTranslateResult.setFocusable(false);
        // etTranslateResult.setEnabled(false);
        // etTranslateResult.setFocusableInTouchMode(false);

        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etTranslateResult.setText("");
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_translate) {
            if (TextUtils.isEmpty(etKey.getText().toString().trim())) {
                Toast.makeText(this, "请输入需要翻译的文本", Toast.LENGTH_SHORT).show();
                return;
            }
            etTranslateResult.setText("");
            requestTranslation(etKey.getText().toString().trim());
        }
    }

    private void requestTranslation(String key) {
        RequestQueue requestQueue = Volley.newRequestQueue(DictionaryActivity.this);
        StringRequest stringRequest = null;
        try {
            stringRequest = new StringRequest(Request.Method.GET,
                URL_TRANSLATE + URLEncoder.encode(key, "utf-8"), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (BuildConfig.DEBUG)
                        Log.d("DictionaryActivity", "onResponse : " + s);
                    if (TextUtils.isEmpty(s)) {
                        Toast.makeText(DictionaryActivity.this, "返回数据有误", Toast.LENGTH_SHORT).show();
                    } else {
                        TranslationInfo translationInfo =
                            new Gson().fromJson(s, TranslationInfo.class);
                        if (translationInfo != null && translationInfo.getErrorCode() == 0
                            && translationInfo.getTranslation() != null
                            && translationInfo.getTranslation().size() > 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (String s1 : translationInfo.getTranslation()) {
                                stringBuilder.append(s1);
                                stringBuilder.append("\n");
                            }
                            etTranslateResult.setText(stringBuilder.toString());
                        } else {
                            String errMsg = null;
                            if (translationInfo != null) {
                                switch (translationInfo.getErrorCode()) {
                                    case 20:
                                        errMsg = "要翻译的文本过长";
                                        break;
                                    case 30:
                                        errMsg = "无法进行有效的翻译";
                                        break;
                                    case 40:
                                        errMsg = "不支持的语言类型";
                                        break;
                                    case 50:
                                        errMsg = "无效的key";
                                        break;
                                    case 60:
                                        errMsg = "无词典结果，仅在获取词典结果生效";
                                        break;
                                }
                            }
                            if (TextUtils.isEmpty(errMsg)) {
                                errMsg = "翻译出现问题，是不是输入了什么火星文？";
                            }
                            Toast.makeText(DictionaryActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(DictionaryActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        requestQueue.add(stringRequest);
    }

}
