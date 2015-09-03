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
import com.ihongqiqu.Identify.entity.LotteryDetailInfo;
import com.ihongqiqu.Identify.widget.DividerItemDecoration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 彩票中奖详情
 * <p/>
 * Created by zhenguo on 9/4/15.
 */
public class LotteryDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static void launch(@NonNull Activity from, @NonNull String code, @NonNull String lotteryName) {
        Intent intent = new Intent(from, LotteryDetailActivity.class);
        intent.putExtra("code", code);
        intent.putExtra("lotteryName", lotteryName);
        from.startActivity(intent);
    }

    private final String URL_LOTTERY_DETAIL = "http://apis.baidu.com/apistore/lottery/lotteryquery";

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.rv_lottery)
    RecyclerView rvLottery;

    String code;
    String lotteryName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_detail);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        code = getIntent().getStringExtra("code");
        lotteryName = getIntent().getStringExtra("lotteryName");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(lotteryName + "中奖信息");
        }

        swipeRefreshLayout.setOnRefreshListener(LotteryDetailActivity.this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light, R.color.app_color);

        rvLottery.setLayoutManager(new LinearLayoutManager(LotteryDetailActivity.this));
        rvLottery.addItemDecoration(new DividerItemDecoration(LotteryDetailActivity.this, DividerItemDecoration.VERTICAL_LIST));

        swipeRefreshLayout.setRefreshing(true);
        requestLotterDetail(code);
    }
    
    private void requestLotterDetail(String code) {
        // showProgressDialog();
        
        RequestQueue mQueue = Volley.newRequestQueue(this);
        
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
            URL_LOTTERY_DETAIL + "?recordcnt=20&lotterycode="
                + code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LotteryDetailActivity", "Volley : " + response);
                try {
                    JSONObject retData = new JSONObject(response);
                    String msg = retData.optString("retMsg");
                    if (TextUtils.isEmpty(msg)) {
                        msg = "接口请求出错";
                    }
                    if (retData.optInt("errNum") == 0) {
                        if (BuildConfig.DEBUG)
                            Log.d("LotteryDetailActivity",
                                "retData : " + retData.optString("retData"));
                        if (!TextUtils.isEmpty(retData.optString("retData"))
                            && retData.optJSONObject("retData").has("data")) {
                            JSONArray array = retData.optJSONObject("retData").optJSONArray("data");
                            ArrayList<LotteryDetailInfo> lotteryDetailInfos =
                                new ArrayList<LotteryDetailInfo>();
                            Gson gson = new Gson();
                            for (int i = 0; i < array.length(); i++) {
                                LotteryDetailInfo lotteryDetailInfo =
                                    gson.fromJson(array.getJSONObject(i).toString(), LotteryDetailInfo.class);
                                lotteryDetailInfos.add(lotteryDetailInfo);
                            }
                            if (lotteryDetailInfos.size() > 0) {
                                LotteryDetailAdapter LotteryDetailAdapter =
                                    new LotteryDetailAdapter(lotteryDetailInfos);
                                rvLottery.setAdapter(LotteryDetailAdapter);
                            } else {
                                Toast.makeText(LotteryDetailActivity.this, "无数据", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LotteryDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LotteryDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                swipeRefreshLayout.setRefreshing(false);
                // hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LotteryDetailActivity", "Volley : " + error.getMessage(), error);
                swipeRefreshLayout.setRefreshing(false);
                // hideProgressDialog();
            }
            
        }) {
            
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("apikey", "0d38f4d9aa9ab1d420816f84c18d3a73");
                if (BuildConfig.DEBUG)
                    Log.d("LotteryDetailActivity", "apikey : 0d38f4d9aa9ab1d420816f84c18d3a73");
                return map;
            }
            
        };
        
        mQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        requestLotterDetail(code);
    }
    
    class LotteryDetailAdapter extends RecyclerView.Adapter<LotteryDetailAdapter.LotteryDetailViewHolder> {
        
        ArrayList<LotteryDetailInfo> lotteryDetailInfos = new ArrayList<LotteryDetailInfo>();
        
        public LotteryDetailAdapter(ArrayList<LotteryDetailInfo> lotteryDetailInfos) {
            this.lotteryDetailInfos = lotteryDetailInfos;
        }
        
        @Override
        public LotteryDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LotteryDetailViewHolder holder =
                new LotteryDetailViewHolder(LayoutInflater.from(LotteryDetailActivity.this).inflate(R.layout.item_lottery_detail, viewGroup, false));
            return holder;
        }
        
        @Override
        public void onBindViewHolder(LotteryDetailViewHolder myViewHolder, int i) {
            myViewHolder.tvLotteryExpect.setText(
                "第 " + lotteryDetailInfos.get(i).getExpect() + " 期");
            myViewHolder.tvLotteryTime.setText("开奖: " + lotteryDetailInfos.get(i).getOpenTime());
            myViewHolder.tvLotteryResult.setText(lotteryDetailInfos.get(i).getOpenCode());
        }
        
        @Override
        public int getItemCount() {
            return lotteryDetailInfos.size();
        }
        
        class LotteryDetailViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_lottery_type)
            TextView tvLotteryType;
            @Bind(R.id.tv_lottery_expect)
            TextView tvLotteryExpect;
            @Bind(R.id.tv_lottery_time)
            TextView tvLotteryTime;
            @Bind(R.id.tv_lottery_result)
            TextView tvLotteryResult;

            public LotteryDetailViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
    
}
