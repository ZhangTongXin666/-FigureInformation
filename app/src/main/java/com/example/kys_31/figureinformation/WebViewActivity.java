package com.example.kys_31.figureinformation;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Observable;

import util.SVProgressHUDUtil;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by kys_7 on 2017/11/18.
 */

public class WebViewActivity extends BaseActivity{
    private TextView titleName_tv;
    private LinearLayout titleBack_persionMessage_ll;
    private WebView webView;
    private WebSettings webSettings;
    private String url;
    @Override
    protected int getLayoutID() {
        return R.layout.webview;
    }

    @Override
    protected void initControl() {
        titleName_tv=(TextView)findViewById(R.id.titleName_tv);
        titleName_tv.setText("馆藏搜索");
        titleBack_persionMessage_ll=(LinearLayout)findViewById(R.id.titleBack_persionMessage_ll);
        String message=getIntent().getStringExtra("message");
        String type=getIntent().getStringExtra("type");
        url="http://smartsearch.nstl.gov.cn/search.html?t="+type+"&q="+message;
        webView=(WebView)findViewById(R.id.webView);
        webSettings=webView.getSettings();
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        webView.onResume();
        webView.canGoBack();
        webView.canGoForward();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e("加载进度",newProgress+"%");
            }
        });
        String user_agent = "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like Gecko) Safari/125.1";
        webSettings.setUserAgentString(user_agent);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);

        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (!SVProgressHUDUtil.isShowing(WebViewActivity.this)){
                    SVProgressHUDUtil.showWithStatus(WebViewActivity.this,newProgress+"%");
                }
                SVProgressHUDUtil.setText(WebViewActivity.this,newProgress+"%");
                if (newProgress==100&&SVProgressHUDUtil.isShowing(WebViewActivity.this)){
                    SVProgressHUDUtil.dismiss(WebViewActivity.this);
                }
            }

        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setControlListener() {
        titleBack_persionMessage_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
