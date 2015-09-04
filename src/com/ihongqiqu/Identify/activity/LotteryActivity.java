package com.ihongqiqu.Identify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ihongqiqu.Identify.entity.LotteryInfo;
import com.ihongqiqu.Identify.widget.DividerItemDecoration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 彩票查询
 * <p/>
 * Created by zhenguo on 9/4/15.
 */
public class LotteryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static void launch(@NonNull Activity from) {
        Intent intent = new Intent(from, LotteryActivity.class);
        from.startActivity(intent);
    }

    private final String URL_LOTTERY_LIST = "http://apis.baidu.com/apistore/lottery/lotterylist";

    @Bind(R.id.rv_lottery)
    RecyclerView rvLottery;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("查询彩票信息");
        }

        swipeRefreshLayout.setOnRefreshListener(LotteryActivity.this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light, R.color.app_color);

        rvLottery.setLayoutManager(new LinearLayoutManager(LotteryActivity.this));
        rvLottery.addItemDecoration(new DividerItemDecoration(LotteryActivity.this, DividerItemDecoration.VERTICAL_LIST));

        swipeRefreshLayout.setRefreshing(true);
        requestLotteryList();
    }

    private void requestLotteryList() {
        // showProgressDialog();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
            URL_LOTTERY_LIST + "?lotterytype=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LotteryActivity", "Volley : " + response);
                try {
                    JSONObject retData = new JSONObject(response);
                    String msg = retData.optString("retMsg");
                    if (TextUtils.isEmpty(msg)) {
                        msg = "接口请求出错";
                    }
                    if (retData.optInt("errNum") == 0) {
                        if (BuildConfig.DEBUG)
                            Log.d("LotteryActivity", "retData : " + retData.optString("retData"));
                        if (!TextUtils.isEmpty(retData.optString("retData"))) {
                            JSONArray array = retData.optJSONArray("retData");
                            ArrayList<LotteryInfo> lotteryInfos = new ArrayList<LotteryInfo>();
                            Gson gson = new Gson();
                            for (int i = 0; i < array.length(); i++) {
                                LotteryInfo lotteryInfo =
                                    gson.fromJson(array.getJSONObject(i).toString(), LotteryInfo.class);
                                lotteryInfos.add(lotteryInfo);
                            }
                            if (lotteryInfos.size() > 0) {
                                LotteryAdapter lotteryAdapter = new LotteryAdapter(lotteryInfos);
                                rvLottery.setAdapter(lotteryAdapter);
                            } else {
                                Toast.makeText(LotteryActivity.this, "无数据", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LotteryActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LotteryActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LotteryActivity", "Volley : " + error.getMessage(), error);
                // hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
            }

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("apikey", "0d38f4d9aa9ab1d420816f84c18d3a73");
                if (BuildConfig.DEBUG)
                    Log.d("LotteryActivity", "apikey : 0d38f4d9aa9ab1d420816f84c18d3a73");
                return map;
            }

        };

        mQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        requestLotteryList();
    }

    class LotteryAdapter extends RecyclerView.Adapter<LotteryAdapter.LotteryViewHolder> {

        ArrayList<LotteryInfo> lotteryInfos = new ArrayList<LotteryInfo>();

        public LotteryAdapter(ArrayList<LotteryInfo> lotteryInfos) {
            this.lotteryInfos = lotteryInfos;
        }

        @Override
        public LotteryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LotteryViewHolder holder =
                new LotteryViewHolder(LayoutInflater.from(LotteryActivity.this).inflate(R.layout.item_lottery, viewGroup, false));
            holder.tvLottery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = v.getTag().toString();
                    String lotteryName = ((TextView) v).getText().toString();
                    if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(lotteryName)) {
                        LotteryDetailActivity.launch(LotteryActivity.this, code, lotteryName);
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(LotteryViewHolder myViewHolder, int i) {
            myViewHolder.tvLottery.setText(lotteryInfos.get(i).getLotteryName());
            myViewHolder.tvLottery.setTag(lotteryInfos.get(i).getLotteryCode());
        }

        @Override
        public int getItemCount() {
            return lotteryInfos.size();
        }

        class LotteryViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_lottery)
            TextView tvLottery;

            public LotteryViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                // tvLottery = (TextView) view.findViewById(R.id.tv_lottery);
            }
        }
    }

}
