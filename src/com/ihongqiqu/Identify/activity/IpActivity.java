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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 顶级域名判断；如果要忽略大小写，可以直接在传入参数的时候toLowerCase()再做判断
     * @param str
     * @return
     */
    public static boolean isTopURL(String str){
        //转换为小写
        str = str.toLowerCase();
        String domainRules = "com.cn|net.cn|org.cn|gov.cn|com.hk|公司|中国|网络|com|net|org|int|edu|gov|mil|arpa|Asia|biz|info|name|pro|coop|aero|museum|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cf|cg|ch|ci|ck|cl|cm|cn|co|cq|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|es|et|ev|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gh|gi|gl|gm|gn|gp|gr|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|in|io|iq|ir|is|it|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|ml|mm|mn|mo|mp|mq|mr|ms|mt|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nt|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|pt|pw|py|qa|re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sy|sz|tc|td|tf|tg|th|tj|tk|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|va|vc|ve|vg|vn|vu|wf|ws|ye|yu|za|zm|zr|zw";
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
            + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
            + "|" // 允许IP和DOMAIN（域名）
            + "(([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]+\\.)?" // 域名- www.
            + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
            + "("+domainRules+"))" // first level domain- .com or .museum
            + "(:[0-9]{1,4})?" // 端口- :80
            + "((/?)|" // a slash isn't required if there is no file name
            + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher isUrl = pattern.matcher(str);
        return isUrl.matches();
    }

}
