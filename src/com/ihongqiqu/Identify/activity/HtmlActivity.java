package com.ihongqiqu.Identify.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.ihongqiqu.Identify.R;
import com.ihongqiqu.Identify.base.BaseActivity;

/**
 * 通用网页展示页面
 * <p/>
 * Created by zhenguo on 9/3/15.
 */
public class HtmlActivity extends BaseActivity {

    public static void launch(@NonNull Activity activity, @NonNull String url) {
        Intent intent = new Intent(activity, HtmlActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.webView)
    WebView webView;

    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setTitle("加载中...");

        init();
    }

    private void init() {
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        webView.requestFocusFromTouch();
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webView.loadUrl(url);

        webView.setScrollbarFadingEnabled(true);
        /*webView.setOnTouchListener(new View.OnTouchListener() {

            float startY;
            float dY;
            boolean isShow = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dY = event.getY() - startY;
                        if (dY < -10) {
                            if (isShow) {
                                hideToolbar();
                                isShow = false;
                            }
                        } else if (dY > 10) {
                            if (!isShow) {
                                showToolbar();
                                isShow = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                    default:

                        break;
                }
                return false;
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(10);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    };

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (getSupportActionBar() != null) {
                if (!TextUtils.isEmpty(title)) {
                    String tmpTitle;
                    if (title.length() > 15) {
                        tmpTitle = title.substring(0, 15);
                    } else {
                        tmpTitle = title;
                    }
                    getSupportActionBar().setTitle(tmpTitle);
                }
            }
        }
    };
}
